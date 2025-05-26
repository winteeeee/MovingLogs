package com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostComment.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class CreateTripostCommentResponse {
    private String id;
    private String authorName;
    private String content;
    private LocalDateTime createdAt;
    private boolean isAuthor;
}
