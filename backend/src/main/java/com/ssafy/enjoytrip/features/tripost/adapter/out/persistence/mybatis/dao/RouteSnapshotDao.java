package com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.tripost.domain.component.RouteSnapshot;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RouteSnapshotDao {
    int bulkUpsert(TripostId tripostId, List<RouteSnapshot> routeSnapshots);
    int deleteBySeqGreaterThan(TripostId tripostId, Integer seq);
}
