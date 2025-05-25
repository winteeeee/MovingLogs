package com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.features.tripost.domain.TripostLike;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TripostLikeDao {
    boolean check(@Param("tripostId") TripostId tripostId, @Param("uid") Uid uid);
    long countByTripostId(@Param("tripostId") TripostId tripostId);
    int bulkInsert(@Param("tripostLikes") List<TripostLike> tripostLikes);
    int bulkDelete(@Param("tripostLikes") List<TripostLike> tripostLikes);
}
