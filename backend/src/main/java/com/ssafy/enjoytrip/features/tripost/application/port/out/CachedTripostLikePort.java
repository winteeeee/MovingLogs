package com.ssafy.enjoytrip.features.tripost.application.port.out;

import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.features.tripost.domain.TripostLike;
import com.ssafy.enjoytrip.features.user.domain.Uid;

import java.util.List;
import java.util.Optional;

public interface CachedTripostLikePort {
    void like(TripostLike tripostLike);
    void unlike(TripostLike tripostLike);
    void setLikeCount(TripostId tripostId, Long likeCount);
    Optional<Long> getLikeCount(TripostId tripostId);
    Optional<Boolean> checkLiked(TripostId tripostId, Uid uid);
    List<TripostLikeState> popAllLikeState();

    void incrementLikeCount(TripostId tripostId);
    void decrementLikeCount(TripostId tripostId);

    record TripostLikeState(TripostId tripostId, Uid uid, boolean state) {}
}
