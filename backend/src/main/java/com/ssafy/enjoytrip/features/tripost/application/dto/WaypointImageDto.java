package com.ssafy.enjoytrip.features.tripost.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WaypointImageDto {
    private String imageId;
    private String thumbnailUrl;
    private String imageUrl;
    private Integer seq;
}
