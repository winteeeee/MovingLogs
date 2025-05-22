package com.ssafy.enjoytrip.features.user.adapter.in.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateUserRequest {
    private String pw;
    private String name;
    private String email;
}
