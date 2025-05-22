package com.ssafy.enjoytrip.features.plan.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.plan.domain.Plan;
import com.ssafy.enjoytrip.features.plan.domain.PlanId;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlanDao {
    List<Plan> findByUid(Long uid, Integer limit, Long offset);
    PlanId insert(Plan plan);
    int update(Long uid, Plan plan);
    int delete(Long id);
}
