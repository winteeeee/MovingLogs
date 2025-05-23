package com.ssafy.enjoytrip.features.plan.application.port.out;

import com.ssafy.enjoytrip.features.plan.domain.PlanId;

public interface DeleteWaypointPort {
    void deleteWaypointsByPlanId(PlanId id);
}
