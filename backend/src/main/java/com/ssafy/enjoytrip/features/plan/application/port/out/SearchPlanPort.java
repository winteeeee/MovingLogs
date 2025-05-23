package com.ssafy.enjoytrip.features.plan.application.port.out;

import com.ssafy.enjoytrip.features.plan.domain.Plan;
import com.ssafy.enjoytrip.features.user.domain.Uid;

import java.util.List;

public interface SearchPlanPort {
    List<Plan> searchMyPlans(Uid uid, Integer page);
}
