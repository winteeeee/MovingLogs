package com.ssafy.enjoytrip.features.tripost.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LatestTripostDto {
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private long commentCount;
    private long viewCount;
    private LocalDate createdAt;
}
