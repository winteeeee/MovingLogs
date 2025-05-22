package com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis.dao;


import com.ssafy.enjoytrip.features.tripost.application.port.in.SearchTripostPagedQuery;
import com.ssafy.enjoytrip.features.tripost.application.port.out.TripostPort;
import com.ssafy.enjoytrip.features.tripost.domain.Tripost;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.infrastructure.mybatis.support.PageableSupport;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TripostDao extends PageableSupport<Map<String, Object>, TripostDao.Criteria> {
    int insert(Tripost tripost);
    int update(Tripost tripost);
    int updateAllViewCount(List<TripostPort.TripostViewCount> tripostViewCountList);
    int recountAllLikeCountById(List<TripostId> tripostIds);
    int recountCommentCountById(TripostId tripostId);
    Map<String, Object> findById(TripostId tripostId);

    Map<String, Object> findTripostDetailById(TripostId tripostId);

    record Criteria(SearchTripostPagedQuery.QueryType type, String query) {  }
}
