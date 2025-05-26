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
        HashOperations<String, String, String> ops = stringRedisTemplate.opsForHash();
        String res = ops.get(createLikeStateKey(), createLikedMapKey(tripostId, uid));
        return Optional.ofNullable(
                res!=null? Boolean.parseBoolean(res):null
        );
    }

    @Override
    public void like(TripostLike tripostLike) {
        HashOperations<String, String, String> ops = stringRedisTemplate.opsForHash();
        ops.put(createLikeStateKey(), createLikedMapKey(tripostLike), String.valueOf(true));
    }

    @Override
    public void unlike(TripostLike tripostLike) {
        HashOperations<String, String, String> ops = stringRedisTemplate.opsForHash();
        ops.put(createLikeStateKey(), createLikedMapKey(tripostLike), String.valueOf(false));
    }

    @Override
    public void setLikeCount(TripostId tripostId, Long likeCount) {
        HashOperations<String, String, String> ops = stringRedisTemplate.opsForHash();
        ops.put(createLikeCountKey(), tripostId.getId(), String.valueOf(likeCount));
    }

    @Override
    public void removeAllLikeCount() {
        stringRedisTemplate.delete(createLikeCountKey());
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

        stringRedisTemplate.delete(createLikeStateKey());
        return states;
    }

    @Override
    public void incrementLikeCount(TripostId tripostId) {
        HashOperations<String, String, String> ops = stringRedisTemplate.opsForHash();
        ops.increment(createLikeCountKey(), tripostId.getId(), 1);
    }

    @Override
    public void decrementLikeCount(TripostId tripostId) {
        HashOperations<String, String, String> ops = stringRedisTemplate.opsForHash();
        ops.increment(createLikeCountKey(), tripostId.getId(), -1);
    }

    @Override
    public Optional<Long> getLikeCount(TripostId tripostId) {
        HashOperations<String, String, String> ops = stringRedisTemplate.opsForHash();
        String likeCount = ops.get(createLikeCountKey(), tripostId.getId());
        return Optional.ofNullable(likeCount)
                .map(Long::valueOf);
    }


    private String createLikedMapKey(TripostLike tripostLike) {
        return createLikedMapKey(
                tripostLike.getTripostId(),
                tripostLike.getUid()
        );
    }
    private String createLikedMapKey(TripostId tripostId, Uid uid) {
        return createMapKey(
                tripostId.getId(),
                uid.getId()
        );
    }

    private String createLikeStateKey() {
        return createKey(
                PREFIX,
                "state"
        );
    }

    private String createLikeCountKey() {
        return createKey(
                PREFIX,
                "count"
        );
    }
}
