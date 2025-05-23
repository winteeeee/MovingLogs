package com.ssafy.enjoytrip.features.plan.application.port.out;

import com.ssafy.enjoytrip.features.user.domain.Uid;

public interface CountPlanPort {
    Long countPlanByUid(Uid uid);
}
