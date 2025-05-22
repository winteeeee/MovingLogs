package com.ssafy.enjoytrip.infrastructure.redis;

import org.springframework.data.redis.core.RedisTemplate;


public class PrefixingRedisTemplate {
    private final RedisTemplate<String, Object> redisTemplate;
    private final String prefix;

    public PrefixingRedisTemplate(RedisTemplate<String, Object> redisTemplate, String prefix) {
        this.redisTemplate = redisTemplate;
        this.prefix = prefix.endsWith(":") ? prefix : prefix + ":";
    }

    private String prefix(String key) {
        return prefix + key;
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(prefix(key), value);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(prefix(key));
    }

    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(prefix(key));
    }

    public Boolean isMember(String key, Object member) {
        return redisTemplate.opsForSet().isMember(prefix(key), member);
    }

    public void addToSet(String key, Object member) {
        redisTemplate.opsForSet().add(prefix(key), member);
        redisTemplate.opsForHash().put(prefix(key), member, key);
    }

    public void removeFromSet(String key, Object member) {
        redisTemplate.opsForSet().remove(prefix(key), member);
    }
}
