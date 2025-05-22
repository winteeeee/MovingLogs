package com.ssafy.enjoytrip.features.auth.adapter.in.web;

import com.ssafy.enjoytrip.features.auth.adapter.in.web.request.PasswordChangeRequest;
import com.ssafy.enjoytrip.features.auth.adapter.in.web.response.PasswordChangeResponse;
import com.ssafy.enjoytrip.features.auth.application.port.in.PasswordChangeUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${api.prefix}/${api.version}/auth")
@RequiredArgsConstructor
@Tag(name = "AuthController", description = "회원 인증 관련 API 제공")
public class AuthController {
    private final PasswordChangeUseCase passwordChangeUseCase;

    @PostMapping("/password-change")
    @Operation(summary = "비밀번호 변경", description = "ID에 대응하는 비밀번호를 변경한다.")
    public ResponseEntity<PasswordChangeResponse> doPasswordFind(@RequestBody PasswordChangeRequest request) {
        PasswordChangeUseCase.Command command = AuthControllerMapper.toPasswordChangeCommand(request);
        PasswordChangeUseCase.Result result = passwordChangeUseCase.passwordChange(command);
        PasswordChangeResponse response = AuthControllerMapper.toPasswordChangeResponse(result);
        return ResponseEntity.ok(response);
    }

    //TODO POST /refresh API 구현
    //TODO POST /logout API 구현
}

