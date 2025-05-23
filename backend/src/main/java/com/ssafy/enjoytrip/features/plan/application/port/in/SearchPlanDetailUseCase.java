package com.ssafy.enjoytrip.features.plan.application.port.in;

import com.ssafy.enjoytrip.features.plan.domain.PlanId;
import com.ssafy.enjoytrip.features.plan.domain.Waypoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public interface SearchPlanDetailUseCase {
    Result searchPlanDetail(Command command);

    @Getter
    @Builder
    @AllArgsConstructor
    class Command {
        private PlanId id;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    class Result {
        private PlanId id;
        private String title;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<Waypoint> waypoints;
    }
}
