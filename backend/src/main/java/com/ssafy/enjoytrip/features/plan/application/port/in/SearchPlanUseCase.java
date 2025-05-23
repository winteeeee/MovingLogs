package com.ssafy.enjoytrip.features.plan.application.port.in;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.plan.domain.Waypoint;
import com.ssafy.enjoytrip.features.plan.domain.PlanId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SearchPlanUseCase {
    PageDto<Result> searchPlans(Command command);

    @Getter
    @Builder
    @AllArgsConstructor
    class Command {
        private String uid;
        private Integer page;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    class Result {
        private PlanId id;
        private String title;
        private List<Waypoint> waypoints;
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalDateTime updatedAt;
    }
}
