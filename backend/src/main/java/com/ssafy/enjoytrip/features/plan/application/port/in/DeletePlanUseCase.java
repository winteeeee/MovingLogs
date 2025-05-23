package com.ssafy.enjoytrip.features.plan.application.port.in;

import com.ssafy.enjoytrip.features.plan.domain.PlanId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface DeletePlanUseCase {
    void deletePlan(Command command);

    @Getter
    @Builder
    @AllArgsConstructor
    class Command {
        private PlanId id;
    }
}
