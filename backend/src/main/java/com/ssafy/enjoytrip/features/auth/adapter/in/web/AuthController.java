package com.ssafy.enjoytrip.features.auth.adapter.in.web;

import com.ssafy.enjoytrip.common.util.WebUtils;
import com.ssafy.enjoytrip.features.auth.application.port.in.DeleteRefreshTokenUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${api.prefix}/${api.version}/auth")
@RequiredArgsConstructor
@Tag(name = "AuthController", description = "회원 인증 관련 API 제공")
public class AuthController {
    private final DeleteRefreshTokenUseCase deleteRefreshTokenUseCase;

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String accessToken = WebUtils.extractToken(request);
        DeleteRefreshTokenUseCase.Command command = new DeleteRefreshTokenUseCase.Command(accessToken);
        deleteRefreshTokenUseCase.deleteRefreshToken(command);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

