package com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostComment.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CreateTripostCommentRequest {
    private String tripostId;
    private String content;
}
