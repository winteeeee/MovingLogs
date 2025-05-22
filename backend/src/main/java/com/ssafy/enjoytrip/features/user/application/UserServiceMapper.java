package com.ssafy.enjoytrip.features.user.application;

import com.ssafy.enjoytrip.features.user.application.port.in.CreateUserUseCase;
import com.ssafy.enjoytrip.features.user.application.port.in.DeleteUserUseCase;
import com.ssafy.enjoytrip.features.user.application.port.in.SearchUserUseCase;
import com.ssafy.enjoytrip.features.user.application.port.in.UpdateUserUseCase;
import com.ssafy.enjoytrip.features.user.domain.User;

public class UserServiceMapper {
    public static User toUser(CreateUserUseCase.Command command) {
        User user = User.builder()
                .id(command.getId())
                .name(command.getName())
                .activated(true)
                .build();
        user.activate();
        user.setUserRole();
        user.setCreateStatus();
        return user;
    }

    public static User toUser(UpdateUserUseCase.Command command) {
        User user = User.builder()
                .id(command.getId())
                .name(command.getName())
                .build();
        user.setUpdateStatus();
        return user;
    }

    public static User toUser(DeleteUserUseCase.Command command) {
        User user = User.builder()
                .id(command.getId())
                .build();
        user.setUpdateStatus();
        return user;
    }

    public static SearchUserUseCase.Result toSearchUserResult(User user) {
        return SearchUserUseCase.Result.builder()
                .id(user.getId())
                .name(user.getName())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static CreateUserUseCase.Result toCreateUserResult(Boolean isCreated) {
        return CreateUserUseCase.Result.builder()
                .created(isCreated)
                .build();
    }

    public static DeleteUserUseCase.Result toDeleteUserResult(Boolean isDelete) {
        return DeleteUserUseCase.Result.builder()
                .deleted(isDelete)
                .build();
    }

    public static UpdateUserUseCase.Result toUpdateUserResult(Boolean isUpdated) {
        return UpdateUserUseCase.Result.builder()
                .updated(isUpdated)
                .build();
    }
}
