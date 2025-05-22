package com.ssafy.enjoytrip.features.user.application;

import com.ssafy.enjoytrip.features.user.application.port.in.DeleteUserUseCase;
import com.ssafy.enjoytrip.features.user.application.port.in.SearchUserUseCase;
import com.ssafy.enjoytrip.features.user.domain.User;

public class UserServiceMapper {
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

    public static DeleteUserUseCase.Result toDeleteUserResult(Boolean isDelete) {
        return DeleteUserUseCase.Result.builder()
                .deleted(isDelete)
                .build();
    }
}
