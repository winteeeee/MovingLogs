package com.ssafy.enjoytrip.features.user.adapter.out.persistence.mybatis;

import com.ssafy.enjoytrip.features.user.adapter.out.persistence.mybatis.dao.UserDao;
import com.ssafy.enjoytrip.features.user.application.port.out.CreateUserPort;
import com.ssafy.enjoytrip.features.user.application.port.out.DeleteUserPort;
import com.ssafy.enjoytrip.features.user.application.port.out.SearchUserPort;
import com.ssafy.enjoytrip.features.user.application.port.out.UpdateUserPort;
import com.ssafy.enjoytrip.features.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
//TODO 쿼리 구현
public class UserMyBatisAdapter implements CreateUserPort, DeleteUserPort, SearchUserPort, UpdateUserPort {
    private final UserDao userDao;

    @Override
    public Boolean createUser(User user) {
        return null;
    }

    @Override
    public Boolean deleteUser(User user) {
        return null;
    }

    @Override
    public Optional<User> searchUser(String id) {
        return Optional.ofNullable(null);
    }

    @Override
    public Boolean updateUser(User user) {
        return null;
    }
}
