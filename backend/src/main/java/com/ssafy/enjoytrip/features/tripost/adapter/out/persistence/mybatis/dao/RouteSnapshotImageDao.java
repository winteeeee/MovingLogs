package com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.tripost.domain.component.RouteSnapshotId;
import com.ssafy.enjoytrip.features.tripost.domain.component.RouteSnapshotImage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RouteSnapshotImageDao {
    int bulkUpsert(RouteSnapshotId routeSnapshotId, List<RouteSnapshotImage> routeSnapshotImages);
    int bulkDeleteBySeqGreaterThan(List<Param> params);

    record Param(RouteSnapshotId routeSnapshotId, Integer seq) {}
}
