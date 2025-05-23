package com.ssafy.enjoytrip.features.plan.application.port.out;

import com.ssafy.enjoytrip.features.plan.domain.PlanId;

public interface DeletePlanPort {
    void deletePlan(PlanId id);
}
