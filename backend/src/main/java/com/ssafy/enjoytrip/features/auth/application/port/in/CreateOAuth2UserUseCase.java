package com.ssafy.enjoytrip.features.auth.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface CreateOAuth2UserUseCase {
    void createOAuth2User(Command command);

    @Getter
    @Builder
    @AllArgsConstructor
    class Command {
        private String id;
        private String name;
    }
}
