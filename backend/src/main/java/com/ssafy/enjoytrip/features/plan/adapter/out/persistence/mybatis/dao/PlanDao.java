package com.ssafy.enjoytrip.features.plan.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.plan.domain.Plan;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlanDao {
    List<Plan> findByUid(String uid, Integer limit, Long offset);
    int insert(Plan plan);
    int update(Plan plan);
    int delete(Long id);
}
