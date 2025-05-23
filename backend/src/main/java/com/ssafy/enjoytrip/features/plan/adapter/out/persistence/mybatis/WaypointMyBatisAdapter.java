package com.ssafy.enjoytrip.features.plan.adapter.out.persistence.mybatis;

import com.ssafy.enjoytrip.features.attraction.domain.AttractionId;
import com.ssafy.enjoytrip.features.plan.adapter.out.persistence.mybatis.dao.WayPointDao;
import com.ssafy.enjoytrip.features.plan.application.port.out.CreateWaypointPort;
import com.ssafy.enjoytrip.features.plan.application.port.out.DeleteWaypointPort;
import com.ssafy.enjoytrip.features.plan.application.port.out.SearchWaypointPort;
import com.ssafy.enjoytrip.features.plan.domain.PlanId;
import com.ssafy.enjoytrip.features.plan.domain.Waypoint;
import com.ssafy.enjoytrip.features.plan.domain.WaypointId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WaypointMyBatisAdapter implements SearchWaypointPort, CreateWaypointPort, DeleteWaypointPort {
    private final WayPointDao wayPointDao;

    @Override
    public List<Waypoint> searchWaypointsByPlanId(PlanId planId) {
        List<Map<String, Object>> resultMaps = wayPointDao.findByPlanId(planId.getId());
        List<Waypoint> waypoints = new ArrayList<>();
        for (Map<String, Object> resultMap : resultMaps) {
            waypoints.add(Waypoint.builder()
                            .id(new WaypointId((String) resultMap.get("id")))
                            .planId(new PlanId((String) resultMap.get("plan_id")))
                            .attractionId(new AttractionId((String) resultMap.get("attraction_id")))
                            .seq((Integer) resultMap.get("seq"))
                    .build());
        }
        return waypoints;
    }

    @Override
    public void createWaypoints(List<Waypoint> waypointList) {
        wayPointDao.insertAll(waypointList);
    }

    @Override
    public void deleteWaypointsByPlanId(PlanId planId) {
        wayPointDao.deleteByPlanId(planId.getId());
    }
}
