package com.ssafy.enjoytrip.features.auth.application.port.out;

public interface DeleteRefreshTokenPort {
    void deleteRefreshToken(String accessToken);
}
