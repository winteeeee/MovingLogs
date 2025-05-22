package com.ssafy.enjoytrip.features.tripost.adapter.out.cache.redis;

import com.ssafy.enjoytrip.features.tripost.application.port.out.*;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.features.tripost.domain.TripostLike;
import com.ssafy.enjoytrip.features.user.domain.Uid;

import java.util.List;
import java.util.Optional;

public class TripostRedisAdapter implements
        CachedTripostLikePort,
        CachedTripostViewCountPort {
    @Override
    public Optional<Boolean> checkLiked(TripostId tripostId, Uid uid) {
        return Optional.empty();
    }

    @Override
    public int createLike(TripostLike tripostLike) {
        return 0;
    }

    @Override
    public int deleteLike(TripostLike tripostLike) {
        return 0;
    }

    @Override
    public List<TripostLikeState> popAllLikeState() {
        return List.of();
    }

    @Override
    public Optional<Long> getLikeCount(TripostId tripostId) {
        return Optional.empty();
    }


    @Override
    public Optional<Long> createTripostViewCount(TripostId tripostId, Long viewCount) {
        return Optional.empty();
    }

    @Override
    public Optional<Long> incrementViewCount(TripostId tripostId) {
        return Optional.empty();
    }

    @Override
    public List<TripostPort.TripostViewCount> popAllCachedViewCount() {
        return List.of();
    }
}
