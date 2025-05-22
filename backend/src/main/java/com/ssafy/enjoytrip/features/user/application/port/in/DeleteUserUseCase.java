package com.ssafy.enjoytrip.features.user.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface DeleteUserUseCase {
    Result deleteUser(Command command);

    @Getter
    @Builder
    @AllArgsConstructor
    class Command {
        private String id;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    class Result {
        private boolean deleted;
    }
}
