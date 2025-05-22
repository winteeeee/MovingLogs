package com.ssafy.enjoytrip.features.auth.adapter.in.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${api.prefix}/${api.version}/auth")
@RequiredArgsConstructor
@Tag(name = "AuthController", description = "회원 인증 관련 API 제공")
public class AuthController {
    //TODO POST /refresh API 구현
    //TODO POST /logout API 구현
}

