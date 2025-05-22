package com.ssafy.enjoytrip.features.user.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface CreateUserUseCase {
    Result createUser(Command command);

    @Getter
    @Builder
    @AllArgsConstructor
    class Command {
        private String id;
        private String name;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    class Result {
        private boolean created;
    }
}
