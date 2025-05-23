package com.ssafy.enjoytrip.features.plan.domain;

import com.ssafy.enjoytrip.features.attraction.domain.AttractionId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class Waypoint {
    private WaypointId id;
    @Setter
    private PlanId planId;
    private AttractionId attractionId;
    private Integer seq;

    public void setCreateStatus(WaypointId id) {
        this.id = id;
    }
}
