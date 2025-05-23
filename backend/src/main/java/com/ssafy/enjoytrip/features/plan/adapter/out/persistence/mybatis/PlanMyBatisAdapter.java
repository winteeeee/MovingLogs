package com.ssafy.enjoytrip.features.plan.adapter.out.persistence.mybatis;

import com.ssafy.enjoytrip.features.plan.adapter.out.persistence.mybatis.dao.PlanDao;
import com.ssafy.enjoytrip.features.plan.application.port.out.*;
import com.ssafy.enjoytrip.features.plan.domain.Plan;
import com.ssafy.enjoytrip.features.plan.domain.PlanId;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public Plan searchPlan(PlanId id) {
        Map<String, Object> resultMap = planDao.findById(id.getId());
        return Plan.builder()
                .id(new PlanId((String) resultMap.get("id")))
                .uid(new Uid((String) resultMap.get("uid")))
                .title((String) resultMap.get("title"))
                .description((String) resultMap.get("description"))
                .thumbnailUrl((String) resultMap.get("thumbnail_url"))
                .startDate(((Date) resultMap.get("start_date")).toLocalDate())
                .endDate(((Date) resultMap.get("end_date")).toLocalDate())
                .createdAt((LocalDateTime) resultMap.get("created_at"))
                .updatedAt((LocalDateTime) resultMap.get("updated_at"))
                .isDeleted((Integer) resultMap.get("is_deleted") == 1)
                .build();
    }

    @Override
    public List<Plan> searchMyPlans(Uid uid, Integer page) {
        List<Map<String, Object>> resultMaps = planDao.findByUid(uid.getId(), pageSize, (long) (page - 1) * pageSize);
        List<Plan> plans = new ArrayList<>();
        for (Map<String, Object> resultMap : resultMaps) {
            plans.add(Plan.builder()
                            .id(new PlanId((String) resultMap.get("id")))
                            .title((String) resultMap.get("title"))
                            .description((String) resultMap.get("description"))
                            .thumbnailUrl((String) resultMap.get("thumbnail_url"))
                            .startDate(((Date) resultMap.get("start_date")).toLocalDate())
                            .endDate(((Date) resultMap.get("end_date")).toLocalDate())
                            .updatedAt((LocalDateTime) resultMap.get("updated_at"))
                    .build());
        }
        return plans;
    }

    @Override
    public Long countPlanByUid(Uid uid) {
        return planDao.countByUid(uid.getId());
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
