package com.ssafy.enjoytrip.features.tripost.application.port.out;

import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.features.tripost.domain.TripostLike;
import com.ssafy.enjoytrip.features.user.domain.Uid;

import java.util.List;

public interface TripostLikePort {
    boolean check(TripostId tripostId, Uid uid);
    int createAll(List<TripostLike> tripostLikes);
    int deleteAll(List<TripostLike> tripostLikes);
    long countByTripostId(TripostId tripostId);
}
