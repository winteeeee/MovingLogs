package com.ssafy.enjoytrip.features.auth;

import com.ssafy.enjoytrip.features.auth.application.exception.UserValidationFailedException;
import com.ssafy.enjoytrip.features.auth.application.port.in.CreateOAuth2UserUseCase;
import com.ssafy.enjoytrip.features.auth.application.port.in.PasswordChangeUseCase;
import com.ssafy.enjoytrip.features.user.application.exception.UserNotFoundException;
import com.ssafy.enjoytrip.features.user.application.port.out.CreateUserPort;
import com.ssafy.enjoytrip.features.user.application.port.out.SearchUserPort;
import com.ssafy.enjoytrip.features.user.application.port.out.UpdateUserPort;
import com.ssafy.enjoytrip.features.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class AuthService implements PasswordChangeUseCase, CreateOAuth2UserUseCase {
    //TODO 래핑 객체로 변경
    private final PasswordEncoder passwordEncoder;
    private final SearchUserPort searchUserPort;
    private final UpdateUserPort updateUserPort;
    private final CreateUserPort createUserPort;

    @Override
    public PasswordChangeUseCase.Result passwordChange(PasswordChangeUseCase.Command command) {
        User user = searchUserPort.searchUser(command.getId())
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다: " + command.getId()));
        if (user.isValid(command.getPassword(), passwordEncoder)) {
            throw new UserValidationFailedException("비밀번호가 일치하지 않습니다.");
        }

        user.changePw(command.getPassword(), passwordEncoder);
        Boolean isUpdated = updateUserPort.updateUser(user);
        return AuthServiceMapper.toPasswordChangeResult(isUpdated);
    }

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
}
