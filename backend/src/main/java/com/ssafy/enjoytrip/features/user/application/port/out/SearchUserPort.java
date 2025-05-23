package com.ssafy.enjoytrip.features.user.application.port.out;

import com.ssafy.enjoytrip.features.user.domain.Uid;
import com.ssafy.enjoytrip.features.user.domain.User;

import java.util.Optional;

public interface SearchUserPort {
    Optional<User> searchUserByUid(Uid id);
    Optional<User> searchUser(String id);
}
