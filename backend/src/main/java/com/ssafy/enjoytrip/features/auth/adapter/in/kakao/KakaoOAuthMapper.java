package com.ssafy.enjoytrip.features.auth.adapter.in.kakao;

import com.ssafy.enjoytrip.features.auth.application.port.in.CreateOAuth2UserUseCase;

public class KakaoOAuthMapper {
    public static CreateOAuth2UserUseCase.Command toCreateOAuth2UserUseCaseCommand(String id) {
        return CreateOAuth2UserUseCase.Command.builder()
                .id(id)
                .build();
    }
}
