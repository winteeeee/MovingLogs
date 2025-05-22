package com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.tripost.domain.TripostComment;
import com.ssafy.enjoytrip.features.tripost.domain.TripostCommentId;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.infrastructure.mybatis.support.PageableSupport;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface TripostCommentDao extends PageableSupport<Map<String, Object>, TripostCommentDao.Criteria> {
    int insert(TripostComment tripostComment);
    int update(TripostComment tripostComment);
    Map<String, Object> findById(TripostCommentId tripostCommentId);
    record Criteria(TripostId tripostId) {  }
}
