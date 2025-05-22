package com.ssafy.enjoytrip.jwt;

import com.ssafy.enjoytrip.common.util.JwtTokenProvider;
import com.ssafy.enjoytrip.common.util.UuidFactory;
import com.ssafy.enjoytrip.features.auth.application.port.out.SaveRefreshTokenPort;
import com.ssafy.enjoytrip.features.auth.application.port.out.SearchRefreshTokenPort;
import com.ssafy.enjoytrip.features.user.domain.Role;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import com.ssafy.enjoytrip.features.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class JwtTest {
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    SaveRefreshTokenPort saveRefreshTokenPort;
    @Autowired
    SearchRefreshTokenPort searchRefreshTokenPort;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    void 리프레시_토큰_저장() {
        User dummyUser = User.builder()
                .id("ssafy")
                .uid(UuidFactory.newId(Uid::new))
                .name("김싸피")
                .role(Role.ROLE_USER)
                .build();

        String accessToken = jwtTokenProvider.createAccessToken(dummyUser);
        String refreshToken = jwtTokenProvider.createRefreshToken(dummyUser);
        System.out.printf("accessToken: %s, refreshToken: %s%n", accessToken, refreshToken);
        saveRefreshTokenPort.saveRefreshToken(accessToken, refreshToken);
        Assertions.assertEquals(stringRedisTemplate.opsForValue().get(accessToken), refreshToken);
    }

    @Test
    void 리프레시_토큰_찾기() {
        String refreshToken = searchRefreshTokenPort.searchRefreshToken("").orElse(null);
        System.out.println(refreshToken);
    }
}
