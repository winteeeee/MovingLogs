package com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis.dao;


import com.ssafy.enjoytrip.features.tripost.application.port.in.SearchTripostPagedQuery;
import com.ssafy.enjoytrip.features.tripost.application.port.out.TripostPort;
import com.ssafy.enjoytrip.features.tripost.domain.Tripost;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.infrastructure.mybatis.support.PageableSupport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface TripostDao extends PageableSupport<Map<String, Object>, TripostDao.Criteria> {
    int insert(@Param("tripost") Tripost tripost);
    int update(@Param("tripost") Tripost tripost);
    int updateAllViewCount(@Param("tripostViewCountList") List<TripostPort.TripostViewCount> tripostViewCountList);
    int recountAllLikeCountById(@Param("tripostIds") List<TripostId> tripostIds);
    int recountCommentCountById(@Param("tripostId") TripostId tripostId);
    Map<String, Object> findById(@Param("tripostId") TripostId tripostId);

    Map<String, Object> findTripostDetailById(@Param("tripostId") TripostId tripostId);
    List<Map<String, Object>> findLatestTripostDetail(Integer size);
    List<Map<String, Object>> findHotTripostDetail(Integer size);
    List<String> getRelatedAttractionNames(String id);

    record Criteria(SearchTripostPagedQuery.QueryType type, String query) {  }
}
