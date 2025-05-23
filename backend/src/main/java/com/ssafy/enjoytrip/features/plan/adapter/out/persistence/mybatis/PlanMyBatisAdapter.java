package com.ssafy.enjoytrip.features.plan.adapter.out.persistence.mybatis;

import com.ssafy.enjoytrip.features.plan.adapter.out.persistence.mybatis.dao.PlanDao;
import com.ssafy.enjoytrip.features.plan.application.port.out.*;
import com.ssafy.enjoytrip.features.plan.domain.Plan;
import com.ssafy.enjoytrip.features.plan.domain.PlanId;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlanMyBatisAdapter implements CreatePlanPort, DeletePlanPort, SearchPlanPort, UpdatePlanPort, CountPlanPort {
    @Value("${paging.size}")
    private Integer pageSize;
    private final PlanDao planDao;

    @Override
    public void createPlan(Plan plan) {
        planDao.insert(plan);
    }

    @Override
    public List<Plan> searchPlanByUid(String uid, Integer page) {
        /*
        어댑터에서 다 들고와서
        썸네일 url 채우고 웨이포인트 리스트 채우기
         */
        return planDao.findByUid(uid, pageSize, (long) (page - 1) * pageSize);
    }

    @Override
    public Long countPlanByUid(String uid) {
        //TODO 쿼리 작성
        return 0L;
    }

    @Override
    public void updatePlan(Plan plan) {
        planDao.update(plan);
    }

    @Override
    public void deletePlan(PlanId id, LocalDateTime updatedAt) {
        planDao.delete(id.getId(), updatedAt);
    }
}
