package com.ssafy.enjoytrip.features.tripost.adapter.out.cache.redis;

import com.ssafy.enjoytrip.features.tripost.application.port.out.*;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.features.tripost.domain.TripostLike;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import com.ssafy.enjoytrip.infrastructure.redis.helper.MultiKeyHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TripostLikeRedisAdapter implements
        CachedTripostLikePort,
        MultiKeyHelper {
    private final StringRedisTemplate stringRedisTemplate;
    private final String PREFIX = "tripost:like:";

    @Override
    public Optional<Boolean> checkLiked(TripostId tripostId, Uid uid) {
        return Optional.empty();
    }

    @Override
    public void liked(TripostLike tripostLike) {
        HashOperations<String, String, String> ops = stringRedisTemplate.opsForHash();
        ops.put(createLikeStateKey(), createLikedMapKey(tripostLike), String.valueOf(true));
    }

    @Override
    public void unliked(TripostLike tripostLike) {
        HashOperations<String, String, String> ops = stringRedisTemplate.opsForHash();
        ops.put(createLikeStateKey(), createLikedMapKey(tripostLike), String.valueOf(false));
    }

    @Override
    public void setLikeCount(TripostId tripostId, Long likeCount) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(createLikeCountKey(tripostId), String.valueOf(likeCount));
    }

    @Override
    public List<TripostLikeState> popAllLikeState() {
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(createLikeStateKey());
        List<TripostLikeState> states = new ArrayList<>();

        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            String[] ids = parseHashKey(entry.getKey().toString());
            if (ids.length != 2) continue;

            TripostId tripostId = new TripostId(ids[0]);
            Uid uid = new Uid(ids[1]);
            boolean liked = Boolean.parseBoolean(entry.getValue().toString());

            states.add(new TripostLikeState(tripostId, uid, liked));
        }

        stringRedisTemplate.delete(createKey(PREFIX, "liked"));
        return states;
    }

    @Override
    public void incrementLikeCount(TripostId tripostId) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.increment(createLikeCountKey(tripostId));
    }

    @Override
    public void decrementLikeCount(TripostId tripostId) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.decrement(createLikeCountKey(tripostId));
    }

    @Override
    public Optional<Long> getLikeCount(TripostId tripostId) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String likeCount = ops.get(createLikeCountKey(tripostId));
        return Optional.ofNullable(likeCount)
                .map(Long::valueOf);
    }


    private String createLikedMapKey(TripostLike tripostLike) {
        return createMapKey(
                tripostLike.getTripostId().getId(),
                tripostLike.getUid().getId()
        );
    }

    private String createLikeStateKey() {
        return createKey(
                PREFIX,
                "state"
        );
    }

    private String createLikeCountKey(TripostId tripostId) {
        return createKey(
                PREFIX,
                "count",
                tripostId.getId()
        );
    }
}
