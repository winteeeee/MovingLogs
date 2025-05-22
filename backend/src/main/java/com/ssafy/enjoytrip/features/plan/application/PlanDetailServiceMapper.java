package com.ssafy.enjoytrip.features.plan.application;

import com.ssafy.enjoytrip.features.attraction.domain.AttractionId;
import com.ssafy.enjoytrip.features.plan.domain.PlanDetail;

import java.util.ArrayList;
import java.util.List;

public class PlanDetailServiceMapper {
    public static List<PlanDetail> toPlanDetailList(List<String> attractionIds) {
        List<PlanDetail> list = new ArrayList<>();
        for (int i = 0; i < attractionIds.size(); i++) {
            PlanDetail planDetail = PlanDetail.builder()
                    .attractionId(new AttractionId(attractionIds.get(i)))
                    .seq(i)
                    .build();
            list.add(planDetail);
        }
        return list;
    }
}
