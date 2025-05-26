package com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostLike.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CheckTripostLikedResponse {
    private boolean liked;
}
