package com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CreateTripostRequest {
    private String title;
    private String description;
    private String content;
    private List<WaypointVo> waypoints;

    public record WaypointVo(
            String attractionId,
            List<WaypointImageVo> images,
            Integer seq
    ) {  }

    public record WaypointImageVo(
            String imageId,
            Integer seq
    ) {  }
}
