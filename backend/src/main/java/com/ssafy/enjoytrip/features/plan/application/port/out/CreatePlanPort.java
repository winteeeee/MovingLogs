package com.ssafy.enjoytrip.features.plan.application.port.out;

import com.ssafy.enjoytrip.features.plan.domain.Plan;

public interface CreatePlanPort {
    void createPlan(Plan plan);
}
