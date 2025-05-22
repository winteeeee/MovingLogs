package com.ssafy.enjoytrip.features.auth.adapter.out.token.redis;

import com.ssafy.enjoytrip.features.auth.application.port.out.SaveRefreshTokenPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class TokenRedisAdapter implements SaveRefreshTokenPort {
    @Value("${jwt.refresh-expmin}")
    private long refreshExpMin;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void saveRefreshToken(String accessToken, String refreshToken) {
        stringRedisTemplate.opsForValue().set(accessToken, refreshToken, Duration.ofMinutes(refreshExpMin));
    }
}
