package com.ssafy.enjoytrip.features.user.application;

import com.ssafy.enjoytrip.features.user.application.port.in.CreateUserUseCase;
import com.ssafy.enjoytrip.features.user.application.port.in.DeleteUserUseCase;
import com.ssafy.enjoytrip.features.user.application.port.in.SearchUserUseCase;
import com.ssafy.enjoytrip.features.user.application.port.in.UpdateUserUseCase;
import com.ssafy.enjoytrip.features.user.domain.Role;
import com.ssafy.enjoytrip.features.user.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class UserServiceMapper {
    public static User toUser(CreateUserUseCase.Command command, PasswordEncoder passwordEncoder) {
        return User.builder()
                .id(command.getId())
                .pw(passwordEncoder.encode(command.getPw()))
                .name(command.getName())
                .email(command.getEmail())
                .role(Role.ROLE_USER)
                .activated(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static User toUser(UpdateUserUseCase.Command command) {
        return User.builder()
                .id(command.getId())
                .name(command.getName())
                .email(command.getEmail())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static User toUser(DeleteUserUseCase.Command command) {
        return User.builder()
                .id(command.getId())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static SearchUserUseCase.Result toSearchUserResult(User user) {
        return SearchUserUseCase.Result.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
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
