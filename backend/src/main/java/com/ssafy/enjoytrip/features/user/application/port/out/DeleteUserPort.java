package com.ssafy.enjoytrip.features.user.application.port.out;

import com.ssafy.enjoytrip.features.user.domain.User;

public interface DeleteUserPort {
    Boolean deleteUser(User user);
}
