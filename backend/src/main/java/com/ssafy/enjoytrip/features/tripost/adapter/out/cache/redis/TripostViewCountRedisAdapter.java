package com.ssafy.enjoytrip.features.tripost.adapter.out.cache.redis;

import com.ssafy.enjoytrip.features.tripost.application.port.out.CachedTripostViewCountPort;
import com.ssafy.enjoytrip.features.tripost.application.port.out.TripostPort;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.infrastructure.redis.helper.MultiKeyHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TripostViewCountRedisAdapter implements
        CachedTripostViewCountPort,
        MultiKeyHelper {
    private final StringRedisTemplate stringRedisTemplate;
    private final String PREFIX = "tripost:view_count:";

    @Override
    public Optional<Long> createViewCount(TripostId tripostId, Long viewCount) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(createCountKey(tripostId), String.valueOf(viewCount));
        return Optional.of(viewCount);
    }

    @Override
    public Optional<Long> incrementViewCount(TripostId tripostId) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Long incremented = null;
        if (stringRedisTemplate.hasKey(createCountKey(tripostId))) {
            incremented = ops.increment(createCountKey(tripostId));
        }
        return Optional.ofNullable(incremented);
    }

    @Override
    public List<TripostPort.TripostViewCount> popAllCachedViewCount() {
        List<TripostPort.TripostViewCount> results = new ArrayList<>();

        for (String key : stringRedisTemplate.keys(PREFIX + "count:*")) {
            String tripostId = key.substring((PREFIX + "count:").length());
            String value = stringRedisTemplate.opsForValue().get(key);
            if (value == null) continue;

            results.add(new TripostPort.TripostViewCount(
                    new TripostId(tripostId),
                    Integer.parseInt(value)
            ));

            stringRedisTemplate.delete(key);
        }

        return results;
    }

    @Override
    public List<Optional<Long>> getViewCount(List<TripostId> tripostIds) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        List<String> keys = tripostIds.stream().map(this::createCountKey).toList();
        return Objects.requireNonNull(ops.multiGet(keys)).stream()
                .map(s -> Optional.ofNullable(s).map(Long::parseLong))
                .toList();
    }

    private String createCountKey(TripostId tripostId) {
        return createKey(
                PREFIX,
                "count",
                tripostId.getId()
        );
    }
}
