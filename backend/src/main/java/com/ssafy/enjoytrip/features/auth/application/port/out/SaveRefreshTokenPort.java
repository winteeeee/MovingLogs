package com.ssafy.enjoytrip.features.auth.application.port.out;

public interface SaveRefreshTokenPort {
    void saveRefreshToken(String accessToken, String refreshToken);
}
