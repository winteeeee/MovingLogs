package com.ssafy.enjoytrip.features.auth.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface PasswordChangeUseCase {
    Result passwordChange(Command command);

    @Getter
    @Builder
    @AllArgsConstructor
    class Command {
        private String id;
        private String password;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    class Result {
        private Boolean changed;
    }
}
