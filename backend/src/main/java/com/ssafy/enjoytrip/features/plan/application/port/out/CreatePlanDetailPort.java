package com.ssafy.enjoytrip.features.plan.application.port.out;

import com.ssafy.enjoytrip.features.plan.domain.PlanDetail;

import java.util.List;

public interface CreatePlanDetailPort {
    void createPlanDetails(List<PlanDetail> planDetailList);
}
