package com.ssafy.enjoytrip.features.plan.domain;

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
    private String thumbnailUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Waypoint> waypoints;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;

    public void setCreateStatus(PlanId id) {
        this.id = id;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        isDeleted = false;
    }

    public void setUpdateStatus() {
        updatedAt = LocalDateTime.now();
    }

    public void setDeleteStats() {
        updatedAt = LocalDateTime.now();
        isDeleted = true;
    }

    public void injectWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }
}
