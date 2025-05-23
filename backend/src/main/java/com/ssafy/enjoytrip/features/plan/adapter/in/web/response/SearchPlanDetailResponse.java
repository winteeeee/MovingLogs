package com.ssafy.enjoytrip.features.plan.adapter.in.web.response;

import com.ssafy.enjoytrip.features.plan.domain.Waypoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SearchPlanDetailResponse {
    private String id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Waypoint> waypointList;
}
