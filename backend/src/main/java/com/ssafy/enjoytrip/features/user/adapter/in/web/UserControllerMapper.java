package com.ssafy.enjoytrip.features.user.adapter.in.web;

import com.ssafy.enjoytrip.features.user.adapter.in.web.request.CreateUserRequest;
import com.ssafy.enjoytrip.features.user.adapter.in.web.request.UpdateUserRequest;
import com.ssafy.enjoytrip.features.user.adapter.in.web.response.CreateUserResponse;
import com.ssafy.enjoytrip.features.user.adapter.in.web.response.DeleteUserResponse;
import com.ssafy.enjoytrip.features.user.adapter.in.web.response.SearchUserResponse;
import com.ssafy.enjoytrip.features.user.adapter.in.web.response.UpdateUserResponse;
import com.ssafy.enjoytrip.features.user.application.port.in.CreateUserUseCase;
import com.ssafy.enjoytrip.features.user.application.port.in.DeleteUserUseCase;
import com.ssafy.enjoytrip.features.user.application.port.in.SearchUserUseCase;
import com.ssafy.enjoytrip.features.user.application.port.in.UpdateUserUseCase;

public class UserControllerMapper {
    public static CreateUserUseCase.Command toCreateUserCommand(CreateUserRequest createUserRequest) {
        return CreateUserUseCase.Command.builder()
                .id(createUserRequest.getId())
                .pw(createUserRequest.getPw())
                .name(createUserRequest.getName())
                .email(createUserRequest.getEmail())
                .build();
    }

    public static CreateUserResponse toCreateUserResponse(CreateUserUseCase.Result createUserResult) {
        return CreateUserResponse.builder()
                .created(createUserResult.isCreated())
                .build();
    }

    public static DeleteUserUseCase.Command toDeleteUserCommand(String id) {
        return DeleteUserUseCase.Command.builder()
                .id(id)
                .build();
    }

    public static DeleteUserResponse toDeleteUserResponse(DeleteUserUseCase.Result deleteUserResult) {
        return DeleteUserResponse.builder()
                .deleted(deleteUserResult.isDeleted())
                .build();
    }

    public static SearchUserUseCase.Command toSearchUserCommand(String id) {
        return SearchUserUseCase.Command.builder()
                .id(id)
                .build();
    }

    public static SearchUserResponse toSearchUserResponse(SearchUserUseCase.Result searchUserResult) {
        return SearchUserResponse.builder()
                .id(searchUserResult.getId())
                .name(searchUserResult.getName())
                .email(searchUserResult.getEmail())
                .role(searchUserResult.getRole())
                .createdAt(searchUserResult.getCreatedAt())
                .updatedAt(searchUserResult.getUpdatedAt())
                .build();
    }

    public static UpdateUserUseCase.Command toUpdateUserCommand(String id, UpdateUserRequest updateUserRequest) {
        return UpdateUserUseCase.Command.builder()
                .id(id)
                .name(updateUserRequest.getName())
                .email(updateUserRequest.getEmail())
                .build();
    }

    public static UpdateUserResponse toUpdateUserResponse(UpdateUserUseCase.Result updateUserResult) {
        return UpdateUserResponse.builder()
                .updated(updateUserResult.isUpdated())
                .build();
    }
}
