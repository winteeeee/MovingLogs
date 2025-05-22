package com.ssafy.enjoytrip.features.auth;

import com.ssafy.enjoytrip.features.auth.application.port.in.CreateOAuth2UserUseCase;
import com.ssafy.enjoytrip.features.auth.application.port.in.DeleteRefreshTokenUseCase;
import com.ssafy.enjoytrip.features.auth.application.port.out.DeleteRefreshTokenPort;
import com.ssafy.enjoytrip.features.user.application.port.out.CreateUserPort;
import com.ssafy.enjoytrip.features.user.application.port.out.SearchUserPort;
import com.ssafy.enjoytrip.features.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class AuthService implements CreateOAuth2UserUseCase, DeleteRefreshTokenUseCase {
    private final SearchUserPort searchUserPort;
    private final CreateUserPort createUserPort;
    private final DeleteRefreshTokenPort deleteRefreshTokenPort;

    @Override
    public void createOAuth2User(CreateOAuth2UserUseCase.Command command) {
        Optional<User> user = searchUserPort.searchUser(command.getId());
        if (user.isEmpty()) {
            //현재 우리 DB에 없는 사용자라면 저장함
            User newUser = AuthServiceMapper.toUser(command.getId());
            newUser.setUserRole();
            newUser.setCreateStatus();
            createUserPort.createUser(newUser);
        }
    }

    @Override
    public void deleteRefreshToken(DeleteRefreshTokenUseCase.Command command) {
        deleteRefreshTokenPort.deleteRefreshToken(command.getAccessToken());
    }
}
