package com.ssafy.enjoytrip.features.auth.adapter.out.token.redis;

import com.ssafy.enjoytrip.features.auth.application.port.out.DeleteRefreshTokenPort;
import com.ssafy.enjoytrip.features.auth.application.port.out.SaveRefreshTokenPort;
import com.ssafy.enjoytrip.features.auth.application.port.out.SearchRefreshTokenPort;
import com.ssafy.enjoytrip.infrastructure.redis.helper.MultiKeyHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokenRedisAdapter implements
        MultiKeyHelper,
        SaveRefreshTokenPort,
        SearchRefreshTokenPort,
        DeleteRefreshTokenPort {
    @Value("${jwt.refresh-expmin}")
    private long refreshExpMin;
    private final StringRedisTemplate stringRedisTemplate;
    private final String PREFIX = "auth:token:access:";

    @Override
    public void saveRefreshToken(String accessToken, String refreshToken) {
        stringRedisTemplate.opsForValue().set(createKey(PREFIX, accessToken), refreshToken, Duration.ofMinutes(refreshExpMin));
    }

    @Override
    public Optional<String> searchRefreshToken(String accessToken) {
        return Optional.ofNullable(stringRedisTemplate.opsForValue().get(createKey(PREFIX, accessToken)));
    }

    @Override
    public void deleteRefreshToken(String accessToken) {
        stringRedisTemplate.delete(createKey(PREFIX, accessToken));
    }
}
