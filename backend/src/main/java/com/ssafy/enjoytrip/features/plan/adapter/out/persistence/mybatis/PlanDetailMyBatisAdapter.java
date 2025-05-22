package com.ssafy.enjoytrip.features.plan.adapter.out.persistence.mybatis;

import com.ssafy.enjoytrip.features.plan.adapter.out.persistence.mybatis.dao.PlanDetailDao;
import com.ssafy.enjoytrip.features.plan.application.port.out.CreatePlanDetailPort;
import com.ssafy.enjoytrip.features.plan.application.port.out.DeletePlanDetailPort;
import com.ssafy.enjoytrip.features.plan.domain.PlanDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PlanDetailMyBatisAdapter implements CreatePlanDetailPort, DeletePlanDetailPort {
    private final PlanDetailDao planDetailDao;

    @Override
    public void createPlanDetails(List<PlanDetail> planDetailList) {
        //TODO 쿼리 작성
    }

    @Override
    public void deletePlanDetailsByPlanId(String planId) {
        //TODO 쿼리 작성
    }
}
