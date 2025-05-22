package com.ssafy.enjoytrip.features.auth.adapter.in.web;

import com.ssafy.enjoytrip.features.auth.adapter.in.web.request.PasswordChangeRequest;
import com.ssafy.enjoytrip.features.auth.adapter.in.web.response.PasswordChangeResponse;
import com.ssafy.enjoytrip.features.auth.application.port.in.PasswordChangeUseCase;

public class AuthControllerMapper {
    public static PasswordChangeUseCase.Command toPasswordChangeCommand(PasswordChangeRequest request) {
        return PasswordChangeUseCase.Command.builder()
                .id(request.getId())
                .password(request.getPassword())
                .build();
    }

    public static PasswordChangeResponse toPasswordChangeResponse(PasswordChangeUseCase.Result result) {
        return PasswordChangeResponse.builder()
                .changed(result.getChanged())
                .build();
    }
}
