package com.ssafy.enjoytrip.features.plan.application.port.out;

import com.ssafy.enjoytrip.features.plan.domain.Waypoint;

import java.util.List;

public interface CreateWaypointPort {
    void createWaypoints(List<Waypoint> waypointList);
}
