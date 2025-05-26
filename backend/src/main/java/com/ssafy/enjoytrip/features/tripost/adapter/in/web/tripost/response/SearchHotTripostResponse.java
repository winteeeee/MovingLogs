package com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SearchHotTripostResponse {
    private String id;
    private String title;
    private String author;
    private String startWaypoint;
    private String endWaypoint;
    private String imageUrl;
    private Integer waypointLength;
    private Integer likeCount;
}
