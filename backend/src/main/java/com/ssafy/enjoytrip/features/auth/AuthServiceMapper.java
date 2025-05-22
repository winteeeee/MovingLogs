package com.ssafy.enjoytrip.features.auth;

import com.ssafy.enjoytrip.features.user.domain.User;

public class AuthServiceMapper {
    public static User toUser(String id) {
        return User.builder()
                .id(id)
                .build();
    }
}
