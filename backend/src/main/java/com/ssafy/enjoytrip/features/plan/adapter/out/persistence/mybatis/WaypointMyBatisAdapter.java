package com.ssafy.enjoytrip.features.plan.adapter.out.persistence.mybatis;

import com.ssafy.enjoytrip.features.plan.adapter.out.persistence.mybatis.dao.PlanDetailDao;
import com.ssafy.enjoytrip.features.plan.application.port.out.CreateWaypointPort;
import com.ssafy.enjoytrip.features.plan.application.port.out.DeleteWaypointPort;
import com.ssafy.enjoytrip.features.plan.domain.Waypoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WaypointMyBatisAdapter implements CreateWaypointPort, DeleteWaypointPort {
    private final PlanDetailDao planDetailDao;

    @Override
    public void createWaypoints(List<Waypoint> waypointList) {
        //TODO 쿼리 작성
    }

    @Override
    public void deleteWaypointsByPlanId(String planId) {
        //TODO 쿼리 작성
    }
}
