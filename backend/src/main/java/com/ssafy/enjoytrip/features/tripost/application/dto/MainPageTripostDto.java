package com.ssafy.enjoytrip.features.tripost.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class MainPageTripostDto {
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private Integer commentCount;
    private Integer viewCount;
    private LocalDate createdAt;
}
