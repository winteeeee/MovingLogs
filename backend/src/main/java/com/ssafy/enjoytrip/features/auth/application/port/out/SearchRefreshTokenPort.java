package com.ssafy.enjoytrip.features.auth.application.port.out;

import java.util.Optional;

public interface SearchRefreshTokenPort {
    Optional<String> searchRefreshToken(String accessToken);
}
