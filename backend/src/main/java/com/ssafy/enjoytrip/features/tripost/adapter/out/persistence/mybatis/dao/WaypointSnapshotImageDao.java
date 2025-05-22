package com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.tripost.domain.component.WaypointSnapshotId;
import com.ssafy.enjoytrip.features.tripost.domain.component.WaypointSnapshotImage;

import java.util.List;

public interface WaypointSnapshotImageDao {
    int bulkUpsert(WaypointSnapshotId waypointSnapshotId, List<WaypointSnapshotImage> waypointSnapshotImages);
    int bulkDeleteBySeqGreaterThan(List<Param> params);

    record Param(WaypointSnapshotId waypointSnapshotId, Integer seq) {}
}
