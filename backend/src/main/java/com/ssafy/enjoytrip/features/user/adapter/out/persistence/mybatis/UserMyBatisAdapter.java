package com.ssafy.enjoytrip.features.user.adapter.out.persistence.mybatis;

import com.ssafy.enjoytrip.features.user.adapter.out.persistence.mybatis.dao.UserDao;
import com.ssafy.enjoytrip.features.user.application.port.out.CreateUserPort;
import com.ssafy.enjoytrip.features.user.application.port.out.DeleteUserPort;
import com.ssafy.enjoytrip.features.user.application.port.out.SearchUserPort;
import com.ssafy.enjoytrip.features.user.domain.Role;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import com.ssafy.enjoytrip.features.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserMyBatisAdapter implements CreateUserPort, DeleteUserPort, SearchUserPort {
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
        Map<String, Object> resultMap = userDao.findById(id);
        return Optional.ofNullable(makeUser(resultMap));
    }

    @Override
    public Optional<User> searchUserByUid(Uid id) {
        Map<String, Object> resultMap = userDao.findByUid(id.getId());
        return Optional.ofNullable(makeUser(resultMap));
    }

    private User makeUser(Map<String, Object> resultMap) {
        User user = null;
        if (resultMap != null) {
            user = User.builder()
                    .uid(new Uid(String.valueOf(resultMap.get("uid"))))
                    .id(String.valueOf(resultMap.get("id")))
                    .name(String.valueOf(resultMap.get("name")))
                    .role(Role.valueOf(String.valueOf(resultMap.get("role"))))
                    .activated((Integer) resultMap.get("activated") == 1)
                    .createdAt((LocalDateTime) resultMap.get("created_at"))
                    .updatedAt((LocalDateTime) resultMap.get("updated_at"))
                    .build();
        }
        return user;
    }
}
