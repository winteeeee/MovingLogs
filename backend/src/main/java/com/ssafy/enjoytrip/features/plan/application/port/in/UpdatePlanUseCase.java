package com.ssafy.enjoytrip.features.plan.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public interface UpdatePlanUseCase {
    void updatePlan(Command command);

    @Getter
    @Builder
    @AllArgsConstructor
    class Command {
        private String id;
        private String title;
        private String desc;
        private String imgId;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<String> attractionIds;
    }
}
