package com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostComment.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class DeleteTripostCommentResponse {
    private boolean deleted;
}
