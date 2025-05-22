package com.ssafy.enjoytrip.features.user.adapter.in.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateUserResponse {
    boolean updated;
}
