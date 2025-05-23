package com.ssafy.enjoytrip.features.plan.application.port.out;

import com.ssafy.enjoytrip.features.plan.domain.Plan;

import java.util.List;

public interface SearchPlanPort {
    List<Plan> searchPlanByUid(String uid, Integer page);
}
