package com.ssafy.enjoytrip.features.tripost.application.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RouteDto {
    private String id;
    private String attractionName;
    // TODO 지도에 경로를 표현하기 위한 값 등등 필요

    private List<RouteImageDto> images;
}
