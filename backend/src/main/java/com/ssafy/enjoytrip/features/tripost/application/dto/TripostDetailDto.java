package com.ssafy.enjoytrip.features.tripost.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TripostDetailDto {
    private String id;
    private String uid;
    private String name;
    private String title;
    private String content;
    private List<WaypointSnapshotDto> waypoints;
    private long commentCount;
    private long likeCount;
    private long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
