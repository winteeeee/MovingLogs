package com.ssafy.enjoytrip.user;

import com.ssafy.enjoytrip.common.util.UuidFactory;
import com.ssafy.enjoytrip.features.user.adapter.in.web.UserController;
import com.ssafy.enjoytrip.features.user.adapter.in.web.response.SearchUserResponse;
import com.ssafy.enjoytrip.features.user.application.port.out.CreateUserPort;
import com.ssafy.enjoytrip.features.user.domain.Role;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import com.ssafy.enjoytrip.features.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@SpringBootTest
public class UserTest {
    @Autowired
    UserController userController;
    @Autowired
    CreateUserPort createUserPort;

    @Test
    void 회원조회() {
        String id = "ssafy2";
        ResponseEntity<SearchUserResponse> response = userController.findById(id);
        System.out.println(response.getBody());
    }

    @Test
    void 회원삭제() {
        String id = "ssafy2";
        userController.delete(id);
    }

    @Test
    void 회원삽입() {
        User user = User.builder()
                .uid(UuidFactory.newId(Uid::new))
                .id("ssafy3")
                .name("박싸피")
                .role(Role.ROLE_USER)
                .activated(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        createUserPort.createUser(user);
    }
}
