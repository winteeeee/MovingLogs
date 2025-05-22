package com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.features.tripost.domain.TripostLike;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TripostLikeDao {
    boolean check(TripostId tripostId, Uid uid);
    long countByTripostId(TripostId tripostId);
    int bulkInsert(List<TripostLike> tripostLikes);
    int bulkDelete(List<TripostLike> tripostLikes);
}
