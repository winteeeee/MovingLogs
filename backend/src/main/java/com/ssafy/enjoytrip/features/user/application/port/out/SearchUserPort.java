package com.ssafy.enjoytrip.features.user.application.port.out;

import com.ssafy.enjoytrip.features.user.domain.User;

import java.util.Optional;

public interface SearchUserPort {
    Optional<User> searchUser(String id);
}
