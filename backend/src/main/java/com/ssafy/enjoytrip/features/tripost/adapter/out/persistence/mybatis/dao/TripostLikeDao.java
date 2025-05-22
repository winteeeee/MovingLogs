package com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.features.tripost.domain.TripostLike;
import com.ssafy.enjoytrip.features.user.domain.Uid;

import java.util.List;

public interface TripostLikeDao {
    boolean check(TripostId tripostId, Uid uid);
    long countByTripostId(TripostId tripostId);
    int bulkInsert(List<TripostLike> tripostLikes);
    int bulkDelete(List<TripostLike> tripostLikes);
}
