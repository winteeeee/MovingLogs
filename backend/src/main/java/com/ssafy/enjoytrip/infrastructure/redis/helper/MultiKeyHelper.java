package com.ssafy.enjoytrip.infrastructure.redis.helper;

public interface MultiKeyHelper {
    default String createKey(String prefix, String... keys) {
        return prefix + String.join(":", keys);
    }

    default String[] parseKey(String prefix, String key) {
        return key.replace(prefix, "").split(":");
    }

    default String createMapKey(String... keys) {
        return String.join(":", keys);
    }

    default String[] parseHashKey(String key) {
        return key.split(":");
    }
}
