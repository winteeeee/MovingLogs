package com.ssafy.enjoytrip.features.auth.adapter.in.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PasswordChangeRequest {
    private String id;
    private String password;
}
