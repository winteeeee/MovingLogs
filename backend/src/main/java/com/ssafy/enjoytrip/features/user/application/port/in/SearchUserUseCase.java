package com.ssafy.enjoytrip.features.user.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public interface SearchUserUseCase {
    Result searchUser(Command command);

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
        private String id;
        private String pw;
        private String name;
        private String email;
        private String role;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
