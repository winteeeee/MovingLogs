package com.ssafy.enjoytrip.features.plan.adapter.out.persistence.mybatis;

import com.ssafy.enjoytrip.features.plan.adapter.out.persistence.mybatis.dao.WayPointDao;
import com.ssafy.enjoytrip.features.plan.application.port.out.CreateWaypointPort;
import com.ssafy.enjoytrip.features.plan.application.port.out.DeleteWaypointPort;
import com.ssafy.enjoytrip.features.plan.domain.PlanId;
import com.ssafy.enjoytrip.features.plan.domain.Waypoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WaypointMyBatisAdapter implements CreateWaypointPort, DeleteWaypointPort {
    private final WayPointDao wayPointDao;

    @Override
    public void createWaypoints(List<Waypoint> waypointList) {
        wayPointDao.insertAll(waypointList);
    }

    @Override
    public void deleteWaypointsByPlanId(PlanId planId) {
        wayPointDao.deleteByPlanId(planId.getId());
    }
}
