package com.ssafy.enjoytrip.features.plan.application.port.out;

public interface DeleteWaypointPort {
    void deleteWaypointsByPlanId(String planId);
}
