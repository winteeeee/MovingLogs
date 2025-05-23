package com.ssafy.enjoytrip.features.plan.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.plan.domain.Waypoint;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WayPointDao {
    List<Map<String, Object>> findByPlanId(String planId);
    void insertAll(List<Waypoint> waypoints);
    void deleteByPlanId(String planId);
}
