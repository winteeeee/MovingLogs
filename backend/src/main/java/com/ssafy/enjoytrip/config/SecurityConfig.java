package com.ssafy.enjoytrip.config;

import com.ssafy.enjoytrip.security.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.NullSecurityContextRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final DefaultOAuth2UserService defaultOAuth2UserService;
    private final SimpleUrlAuthenticationSuccessHandler simpleUrlAuthenticationSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JWTVerificationFilter jwtVerifyFilter) throws Exception {
        //시큐리티 컨텍스트 홀더를 매번 초기화 (stateless)
        //인증, 유저 관련 API만 인증 없이 접근 허용. 나머지는 인증 필요하도록 설정
        //JWT 관련 필터 삽입
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
        .securityContext(context -> context.securityContextRepository(new NullSecurityContextRepository()))
        .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/oauth2/**").permitAll()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/users").permitAll()
                .requestMatchers("/uploads/**").permitAll()
                .anyRequest().authenticated())
        .addFilterBefore(jwtVerifyFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e.authenticationEntryPoint((req, res, ex) -> {
                    res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }))
        .oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfo -> userInfo
                .userService(defaultOAuth2UserService))
            .successHandler(simpleUrlAuthenticationSuccessHandler)
        );
        return http.build();
    }
}
