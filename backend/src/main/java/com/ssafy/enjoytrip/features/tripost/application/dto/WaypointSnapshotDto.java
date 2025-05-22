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
    // TODO 지도에 경로를 표현하기 위한 값 등등 필요

    private List<WaypointImageDto> images;
}
