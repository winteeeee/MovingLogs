package com.ssafy.enjoytrip.features.plan.application;

import com.ssafy.enjoytrip.features.attraction.domain.AttractionId;
import com.ssafy.enjoytrip.features.plan.domain.Waypoint;

import java.util.ArrayList;
import java.util.List;

public class WaypointServiceMapper {
    public static List<Waypoint> toWaypointList(List<String> attractionIds) {
        List<Waypoint> list = new ArrayList<>();
        for (int i = 0; i < attractionIds.size(); i++) {
            Waypoint waypoint = Waypoint.builder()
                    .attractionId(new AttractionId(attractionIds.get(i)))
                    .seq(i)
                    .build();
            list.add(waypoint);
        }
        return list;
    }
}
