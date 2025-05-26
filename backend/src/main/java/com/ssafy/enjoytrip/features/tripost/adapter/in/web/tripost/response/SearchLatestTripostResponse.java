package com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
public class SearchLatestTripostResponse {
    private String title;
    private String description;
    private LocalDate createdAt;
    private String imageUrl;
    private Integer viewCount;
    private Integer commentCount;
}
