package com.ssafy.enjoytrip.features.plan.application.port.out;

import com.ssafy.enjoytrip.features.plan.domain.PlanId;

import java.time.LocalDateTime;

public interface DeletePlanPort {
    void deletePlan(PlanId id, LocalDateTime updatedAt);
}
