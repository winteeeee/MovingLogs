package com.ssafy.enjoytrip.features.plan.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.plan.domain.Plan;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface PlanDao {
    List<Map<String, Object>> findByUid(String uid, Integer limit, Long offset);
    Long countByUid(String uid);
    int insert(Plan plan);
    int update(Plan plan);
    int delete(String id, LocalDateTime updatedAt);
}
