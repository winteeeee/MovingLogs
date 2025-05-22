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
    private List<RouteVo> routes;

    public record RouteVo(
            String attractionId,
            List<RouteImageVo> images,
            Integer seq
    ) {  }

    public record RouteImageVo(
            String imageId,
            Integer seq
    ) {  }
}
