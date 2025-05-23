package com.ssafy.enjoytrip.features.auth;

import com.ssafy.enjoytrip.common.util.UuidFactory;
import com.ssafy.enjoytrip.features.auth.application.port.in.CreateOAuth2UserUseCase;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import com.ssafy.enjoytrip.features.user.domain.User;

public class AuthServiceMapper {
    public static User toUser(CreateOAuth2UserUseCase.Command command) {
        return User.builder()
                .uid(UuidFactory.newId(Uid::new))
                .id(command.getId())
                .name(command.getName())
                .build();
    }
}
