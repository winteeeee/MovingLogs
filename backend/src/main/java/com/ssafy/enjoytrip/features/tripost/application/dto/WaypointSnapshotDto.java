package com.ssafy.enjoytrip.features.tripost.application.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WaypointSnapshotDto {
    private String id;
    private String attractionId;
    private String attractionName;
    private Integer seq;
    private Double latitude;
    private Double longitude;
    private List<WaypointImageDto> images;
}
