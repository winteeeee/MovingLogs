package com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.tripost.domain.TripostComment;
import com.ssafy.enjoytrip.features.tripost.domain.TripostCommentId;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.infrastructure.mybatis.support.PageableSupport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface TripostCommentDao extends PageableSupport<Map<String, Object>, TripostCommentDao.Criteria> {
    int insert(@Param("tripostComment") TripostComment tripostComment);
    int update(@Param("tripostComment") TripostComment tripostComment);
    Map<String, Object> findById(@Param("tripostCommentId") TripostCommentId tripostCommentId);
    record Criteria(@Param("tripostId") TripostId tripostId) {  }
}
