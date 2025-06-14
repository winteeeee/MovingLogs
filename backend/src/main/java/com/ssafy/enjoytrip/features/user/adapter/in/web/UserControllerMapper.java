package com.ssafy.enjoytrip.features.user.adapter.in.web;

import com.ssafy.enjoytrip.features.user.adapter.in.web.response.DeleteUserResponse;
import com.ssafy.enjoytrip.features.user.adapter.in.web.response.SearchUserResponse;
import com.ssafy.enjoytrip.features.user.application.port.in.DeleteUserUseCase;
import com.ssafy.enjoytrip.features.user.application.port.in.SearchUserUseCase;

public class UserControllerMapper {
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
                .role(searchUserResult.getRole())
                .createdAt(searchUserResult.getCreatedAt())
                .updatedAt(searchUserResult.getUpdatedAt())
                .build();
    }
}
