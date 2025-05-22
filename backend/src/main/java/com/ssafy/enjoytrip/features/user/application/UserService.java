package com.ssafy.enjoytrip.features.user.application;

import com.ssafy.enjoytrip.features.user.application.exception.UserNotFoundException;
import com.ssafy.enjoytrip.features.user.application.port.in.CreateUserUseCase;
import com.ssafy.enjoytrip.features.user.application.port.in.DeleteUserUseCase;
import com.ssafy.enjoytrip.features.user.application.port.in.SearchUserUseCase;
import com.ssafy.enjoytrip.features.user.application.port.in.UpdateUserUseCase;
import com.ssafy.enjoytrip.features.user.application.port.out.CreateUserPort;
import com.ssafy.enjoytrip.features.user.application.port.out.DeleteUserPort;
import com.ssafy.enjoytrip.features.user.application.port.out.SearchUserPort;
import com.ssafy.enjoytrip.features.user.application.port.out.UpdateUserPort;
import com.ssafy.enjoytrip.features.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UserService implements CreateUserUseCase, DeleteUserUseCase, SearchUserUseCase, UpdateUserUseCase {
    private final CreateUserPort createUserPort;
    private final DeleteUserPort deleteUserPort;
    private final SearchUserPort searchUserPort;
    private final UpdateUserPort updateUserPort;

    @Override
    public SearchUserUseCase.Result searchUser(SearchUserUseCase.Command command) {
        User user = searchUserPort.searchUser(command.getId()).orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다: " + command.getId()));
        return UserServiceMapper.toSearchUserResult(user);
    }

    @Override
    public CreateUserUseCase.Result createUser(CreateUserUseCase.Command command) {
        Boolean isCreated = createUserPort.createUser(UserServiceMapper.toUser(command));
        return UserServiceMapper.toCreateUserResult(isCreated);
    }

    @Override
    public DeleteUserUseCase.Result deleteUser(DeleteUserUseCase.Command command) {
        Boolean isDelete = deleteUserPort.deleteUser(UserServiceMapper.toUser(command));
        return UserServiceMapper.toDeleteUserResult(isDelete);
    }

    @Override
    public UpdateUserUseCase.Result updateUser(UpdateUserUseCase.Command command) {
        Boolean isUpdated = updateUserPort.updateUser(UserServiceMapper.toUser(command));
        return UserServiceMapper.toUpdateUserResult(isUpdated);
    }
}
