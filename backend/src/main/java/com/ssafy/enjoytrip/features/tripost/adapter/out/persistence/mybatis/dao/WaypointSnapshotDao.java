package com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.tripost.domain.component.WaypointSnapshot;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;

import java.util.List;

public interface WaypointSnapshotDao {
    int bulkUpsert(TripostId tripostId, List<WaypointSnapshot> waypointSnapshots);
    int deleteBySeqGreaterThan(TripostId tripostId, Integer seq);
}
