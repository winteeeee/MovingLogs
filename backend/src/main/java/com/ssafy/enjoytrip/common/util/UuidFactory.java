package com.ssafy.enjoytrip.common.util;

import java.util.UUID;

public class UuidFactory {
    private UuidFactory() {
        // 유틸 클래스이므로 인스턴스화 금지
    }

    /**
     * 새로운 UUID를 생성하여 문자열로 반환합니다.
     *
     * @return 랜덤 UUID 문자열
     */
    public static String newUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 도메인 전용 VO 타입으로 래핑된 UUID를 생성합니다.
     *
     * 예) PostId postId = UuidFactory.newId(PostId::new);
     *
     * @param <T>       VO 타입
     * @param converter VO 생성자 레퍼런스 (예: PostId::new)
     * @return VO로 래핑된 새 UUID
     */
    public static <T> T newId(java.util.function.Function<String, T> converter) {
        String uuid = newUuid();
        return converter.apply(uuid);
    }
}
