package com.ssafy.enjoytrip.security;

import com.ssafy.enjoytrip.common.util.JwtTokenProvider;
import com.ssafy.enjoytrip.common.util.JwtUtils;
import com.ssafy.enjoytrip.common.util.WebUtils;
import com.ssafy.enjoytrip.features.auth.application.port.out.SaveRefreshTokenPort;
import com.ssafy.enjoytrip.features.auth.application.port.out.SearchRefreshTokenPort;
import com.ssafy.enjoytrip.features.user.application.exception.UserNotFoundException;
import com.ssafy.enjoytrip.features.user.application.port.out.SearchUserPort;
import com.ssafy.enjoytrip.features.user.domain.User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JWTVerificationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final SaveRefreshTokenPort saveRefreshTokenPort;
    private final SearchRefreshTokenPort searchRefreshTokenPort;
    private final SearchUserPort searchUserPort;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        try {
            //토큰을 추출해보고
            token = WebUtils.extractToken(request);
            if (token != null) {
                //토큰이 있고, 유효하다면 유저 정보를 context holder에 저장
                Map<String, Object> claims = jwtTokenProvider.getClaims(token);
                saveUserDetailsToSecurityContextHolder(claims.get("uid").toString());
            }
        } catch (ExpiredJwtException e) {
            //액세스 토큰이 만료되었다면
            //리프레시 토큰을 확인해봄
            Optional<String> refreshToken = searchRefreshTokenPort.searchRefreshToken(token);
            if (refreshToken.isPresent()) {
                //리프레시 토큰이 만료되지 않았다면 둘 다 재발급
                Map<String, Object> claims = jwtTokenProvider.getClaims(refreshToken.get());
                String uid = (String) claims.get("uid");
                User user = searchUserPort.searchUser(uid)
                        .orElseThrow(() -> new UserNotFoundException("uid에 대응하는 유저가 없습니다."));
                String accessToken = jwtTokenProvider.createAccessToken(user);
                String newRefreshToken = jwtTokenProvider.createRefreshToken(user);

                //레디스에 리프레시 토큰 저장, 컨텍스트 홀더에 유저 디테일 저장, 액세스 토큰 응답으로 전송
                saveRefreshTokenPort.saveRefreshToken(accessToken, newRefreshToken);
                saveUserDetailsToSecurityContextHolder(uid);
                JwtUtils.writeJwtTokensToResponse(response, accessToken);
            }
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    private void saveUserDetailsToSecurityContextHolder(String uid) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(uid);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
