package com.ssafy.enjoytrip.security;

import com.ssafy.enjoytrip.common.util.JwtTokenProvider;
import com.ssafy.enjoytrip.common.util.JwtUtils;
import com.ssafy.enjoytrip.common.util.WebUtils;
import com.ssafy.enjoytrip.features.auth.application.port.out.SaveRefreshTokenPort;
import com.ssafy.enjoytrip.features.auth.application.port.out.SearchRefreshTokenPort;
import com.ssafy.enjoytrip.features.user.application.exception.UserNotFoundException;
import com.ssafy.enjoytrip.features.user.application.port.out.SearchUserPort;
import com.ssafy.enjoytrip.features.user.domain.Uid;
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
    private final JwtUtils jwtUtils;

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
                logger.debug("토큰 재발급");
                //리프레시 토큰이 만료되지 않았다면 둘 다 재발급
                Map<String, Object> claims = jwtTokenProvider.getClaims(refreshToken.get());
                String uid = (String) claims.get("uid");
                User user = searchUserPort.searchUserByUid(new Uid(uid))
                        .orElseThrow(() -> new UserNotFoundException("uid에 대응하는 유저가 없습니다."));
                String accessToken = jwtTokenProvider.createAccessToken(user);
                String newRefreshToken = jwtTokenProvider.createRefreshToken(user);

                //레디스에 리프레시 토큰 저장, 컨텍스트 홀더에 유저 디테일 저장, 액세스 토큰 응답으로 전송
                saveRefreshTokenPort.saveRefreshToken(accessToken, newRefreshToken);
                saveUserDetailsToSecurityContextHolder(uid);

                /*
                이 시점에서는 클라이언트의 API가 axios로 요청된 경우.
                로그인 버튼을 눌렀을 때는 axios가 아니라 windows.loaction을 직접 바꿔서 접근하기 때문에
                리다이렉트로 토큰을 수신할 수 있지만
                axios는 redirect 요청을 정상적으로 받지 않으므로 다른 방법으로 요청해야함
                따라서 액세스 토큰을 JSON의 형태로 담아 클라이언트로 보내고
                axios의 인터셉터를 이용하여 응답에 액세스 토큰이 있다면 토큰을 클라이언트측에 저장하고
                다시 재요청을 보내도록 구현
                또한 여기서 값이 반환되므로 현재 들어온 요청이 이 필터를 통과하면 안됨
                따라서 doFilter 하지 않고 그대로 반환
                 */
                jwtUtils.responseWithJwtToken(response, accessToken, user.getName());
                return;
            }
        }

        filterChain.doFilter(request, response);
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
