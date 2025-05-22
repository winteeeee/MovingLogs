package com.ssafy.enjoytrip.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.enjoytrip.common.util.JwtTokenProvider;
import com.ssafy.enjoytrip.common.util.JwtUtils;
import com.ssafy.enjoytrip.features.auth.application.port.out.SaveRefreshTokenPort;
import com.ssafy.enjoytrip.features.user.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final SaveRefreshTokenPort saveRefreshTokenPort;
    private final JwtTokenProvider jwtTokenProvider;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, SaveRefreshTokenPort saveRefreshTokenPort) {
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
        this.saveRefreshTokenPort = saveRefreshTokenPort;
        this.setFilterProcessesUrl("/api/v1/auth/login");
        this.setUsernameParameter("uid");
    }

    //JWT 토큰 발행 후 reponse를 통해 토큰 전달
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        User user = userDetails.getUser();
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);
        saveRefreshTokenPort.saveRefreshToken(user.getUid(), refreshToken);
        JwtUtils.writeJwtTokensToResponse(response, accessToken, refreshToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        throw failed;
    }
}
