package com.ssafy.enjoytrip.infrastructure.mybatis.support;

import com.ssafy.enjoytrip.common.dto.PageDto;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface PageableSupport<T, C> {
    List<T> selectPage(int limit, int offset, C criteria);
    Long selectTotalCount(C criteria);

    default PageDto<T> toPage(int page, int size, C criteria) {
        Objects.requireNonNull(criteria, "criteria must not be null");
        int offset = (page-1) * size;
        List<T> content = selectPage(size, offset, criteria);
        Long totalElements = selectTotalCount(criteria);
        int totalPages = (int) Math.ceil((double) totalElements/size);
        boolean hasPrevious = page > 0;
        boolean hasNext = page+1 < totalPages;
        return new PageDto<>(
                content,
                page,
                size,
                totalPages,
                totalElements,
                hasPrevious,
                hasNext
        );
    }
}
