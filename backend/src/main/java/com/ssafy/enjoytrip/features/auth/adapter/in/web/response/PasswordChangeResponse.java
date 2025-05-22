package com.ssafy.enjoytrip.features.auth.adapter.in.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PasswordChangeResponse {
    private Boolean changed;
}
