package com.ssafy.enjoytrip.features.plan.domain;

import com.ssafy.enjoytrip.features.image.domain.Image;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class Plan {
    private PlanId id;
    private Uid uid;
    private String title;
    private String description;
    //TODO 대표 이미지 관련 처리 추가(추가, 수정 시)
    private Image image;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Waypoint> waypoints;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;
}
