package com.ssafy.enjoytrip.features.tripost.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class TripostListItemDto {
    // TODO: persistence 레이어에서는 Page 클래스를 사용하므로, 영속레이어 어뎁터에서 적절히 매핑해줘야 함. 헥사고날에 위배되지 않음.
    private String id;
    private String title;
    private String description;
    private String uid;
    private String name;
    private LocalDateTime createdAt;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private List<WaypointSnapshotDto> waypoints;
    private List<WaypointImageDto> images;
}
