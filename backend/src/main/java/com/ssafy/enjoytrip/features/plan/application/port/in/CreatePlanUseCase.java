package com.ssafy.enjoytrip.features.plan.application.port.in;

import com.ssafy.enjoytrip.features.plan.domain.PlanId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public interface CreatePlanUseCase {
    Result createPlan(Command command);

    @Getter
    @Builder
    @AllArgsConstructor
    class Command {
        private String uid;
        private String desc;
        private String title;
        private String thumbnailUrl;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<String> attractionIds;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    class Result {
        private PlanId planId;
    }
}
