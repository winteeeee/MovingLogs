package com.ssafy.enjoytrip.features.auth.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface DeleteRefreshTokenUseCase {
    void deleteRefreshToken(Command command);

    @Getter
    @Builder
    @AllArgsConstructor
    class Command {
        private String accessToken;
    }
}
