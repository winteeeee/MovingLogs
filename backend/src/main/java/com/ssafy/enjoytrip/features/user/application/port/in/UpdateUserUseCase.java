package com.ssafy.enjoytrip.features.user.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface UpdateUserUseCase {
    Result updateUser(Command command);

    @Getter
    @Builder
    @AllArgsConstructor
    class Command {
        private String id;
        private String name;
        private String email;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    class Result {
        boolean updated;
    }
}
