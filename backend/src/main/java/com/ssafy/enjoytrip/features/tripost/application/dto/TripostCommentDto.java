package com.ssafy.enjoytrip.features.tripost.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class TripostCommentDto {
    private String id;
    private String uid;
    private String authorName;
    private String content;
    private List<TripostCommentDto> replies;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isAuthor;
}
