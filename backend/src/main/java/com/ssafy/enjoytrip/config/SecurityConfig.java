package com.ssafy.enjoytrip.config;

import com.ssafy.enjoytrip.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final DefaultOAuth2UserService defaultOAuth2UserService;
    private final SimpleUrlAuthenticationSuccessHandler simpleUrlAuthenticationSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //세션 사용 시 이 필터를 사용
//    @Bean
//    public JsonUsernamePasswordAuthenticationFilter jsonAuthFilter(AuthenticationManager authManager) {
//        JsonUsernamePasswordAuthenticationFilter filter = new JsonUsernamePasswordAuthenticationFilter(authManager);
//
//        filter.setAuthenticationSuccessHandler((req, res, auth) -> {
//            //시큐리티 컨텍스트 홀더에 인증 정보를 넣고
//            SecurityContext context = SecurityContextHolder.createEmptyContext();
//            context.setAuthentication(auth);
//            SecurityContextHolder.setContext(context);
//
//            //세션에도 저장
//            HttpSession session = req.getSession(true);
//            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
//            res.setStatus(HttpServletResponse.SC_OK);
//        });
//
//        filter.setAuthenticationFailureHandler((req, res, auth) -> {
//            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//        });
//        return filter;
//    }

    //아래는 세션 사용 시 사용
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http,
//                                                   JsonUsernamePasswordAuthenticationFilter jsonAuthFilter) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/api/v1/auth/**").permitAll()
//                        .requestMatchers("/api/v1/users").permitAll()
//                        .anyRequest().authenticated())
//                .addFilterAt(jsonAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .logout(logout -> logout
//                        .logoutUrl("/api/v1/auth/logout")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                        .logoutSuccessHandler((req, res, auth) -> res.setStatus(HttpStatus.OK.value())))
//                .authenticationProvider(authenticationProvider());
//        return http.build();
//    }

    //TODO 시큐리티 테스트 필요
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JWTAuthenticationFilter authFilter,
            JWTVerificationFilter jwtVerifyFilter,
            SecurityExceptionHandlingFilter exceptionFilter) throws Exception {
                http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers("/api/v1/users").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtVerifyFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(authFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionFilter, JWTVerificationFilter.class)
                .authenticationProvider(authenticationProvider())
                .oauth2Login(oauth2 -> oauth2
                    .userInfoEndpoint(userInfo -> userInfo
                        .userService(defaultOAuth2UserService)).defaultSuccessUrl("/")
                    .successHandler(simpleUrlAuthenticationSuccessHandler)
                );
        return http.build();
    }
}
