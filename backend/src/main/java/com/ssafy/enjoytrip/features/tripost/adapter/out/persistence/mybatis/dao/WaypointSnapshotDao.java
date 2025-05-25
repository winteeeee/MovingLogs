package com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.tripost.domain.component.WaypointSnapshot;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WaypointSnapshotDao {
    int bulkUpsert(@Param("tripostId") TripostId tripostId, @Param("waypointSnapshots") List<WaypointSnapshot> waypointSnapshots);
    int deleteBySeqGreaterThan(@Param("tripostId") TripostId tripostId, @Param("seq") Integer seq);
}
