package com.ssafy.enjoytrip.common.util;

import com.ssafy.enjoytrip.features.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {
    private final SecretKey key;
    @Value("${jwt.access-expmin}")
    private long accessExpMin;
    @Value("${jwt.refresh-expmin}")
    private long refreshExpMin;

    public JwtTokenProvider() {
        key = Jwts.SIG.HS256.key().build();
    }

    public String createAccessToken(User user) {
        return create("accessToken", accessExpMin,
                Map.of("id", user.getId(), "uid" , user.getUid().getId(), "name", user.getName(), "role", user.getRole().name()));
    }

    public String createRefreshToken(User user) {
        return create("refreshToken", refreshExpMin, Map.of("uid", user.getUid().getId()));
    }

    private String create(String subject, long expireMin, Map<String, Object> claims) {
        Date expireDate = new Date(System.currentTimeMillis() + 1000 * 60 * expireMin);
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .expiration(expireDate)
                .signWith(key)
                .compact();
    }

    public Map<String, Object> getClaims(String jwt) {
        JwtParser parser = Jwts.parser()
                .verifyWith(key)
                .build();
        Jws<Claims> jws = parser.parseSignedClaims(jwt);
        return jws.getPayload();
    }
}
