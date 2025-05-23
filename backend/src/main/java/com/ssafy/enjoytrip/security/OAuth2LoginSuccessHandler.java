package com.ssafy.enjoytrip.security;

import com.ssafy.enjoytrip.common.util.JwtTokenProvider;
import com.ssafy.enjoytrip.common.util.JwtUtils;
import com.ssafy.enjoytrip.features.auth.application.port.out.SaveRefreshTokenPort;
import com.ssafy.enjoytrip.features.user.application.exception.UserNotFoundException;
import com.ssafy.enjoytrip.features.user.application.port.out.SearchUserPort;
import com.ssafy.enjoytrip.features.user.domain.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("frontend_url")
    private String frontendUrl;
    private final JwtTokenProvider jwtTokenProvider;
    private final SearchUserPort searchUserPort;
    private final SaveRefreshTokenPort saveRefreshTokenPort;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        User user = searchUserPort.searchUser(String.valueOf((Long) oAuth2User.getAttribute("id")))
                .orElseThrow(() -> new UserNotFoundException("OAuth2User의 id에 대응하는 유저가 없습니다."));

        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);
        saveRefreshTokenPort.saveRefreshToken(accessToken, refreshToken);
        JwtUtils.redirectWithJwtToken(response, accessToken, user.getName());
    }
}
