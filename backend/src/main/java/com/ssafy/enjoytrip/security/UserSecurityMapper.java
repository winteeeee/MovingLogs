package com.ssafy.enjoytrip.security;

import com.ssafy.enjoytrip.features.user.application.port.in.SearchUserUseCase;
import com.ssafy.enjoytrip.features.user.domain.Role;
import com.ssafy.enjoytrip.features.user.domain.User;

public class UserSecurityMapper {
    public static SearchUserUseCase.Command toSearchUserCommand(String id) {
        return SearchUserUseCase.Command.builder()
                .id(id)
                .build();
    }

    public static User toUser(SearchUserUseCase.Result searchUserResult) {
        return User.builder()
                .id(searchUserResult.getId())
                .role(Role.valueOf(searchUserResult.getRole()))
                .build();
    }
}
