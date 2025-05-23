package com.ssafy.enjoytrip.features.plan.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.plan.domain.Waypoint;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WayPointDao {
    void insertAll(List<Waypoint> waypoints);
}
