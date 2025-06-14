package com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostComment.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SearchTripostCommentRequest {
    private Integer page;
    private Integer size;
}
