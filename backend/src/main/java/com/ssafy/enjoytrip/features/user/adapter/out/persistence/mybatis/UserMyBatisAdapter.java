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
public class UserMyBatisAdapter implements CreateUserPort, DeleteUserPort, SearchUserPort, UpdateUserPort {
    private final UserDao userDao;

    @Override
    public Boolean createUser(User user) {
        return userDao.insert(user) > 0;
    }

    @Override
    public Boolean deleteUser(User user) {
        return userDao.delete(user) > 0;
    }

    @Override
    public Optional<User> searchUser(String id) {
        return Optional.ofNullable(userDao.findById(id));
    }

    @Override
    public Boolean updateUser(User user) {
        return userDao.update(user) > 0;
    }
}
