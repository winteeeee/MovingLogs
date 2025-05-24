package com.ssafy.enjoytrip.features.attraction.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public interface SearchGugunUseCase {
    List<Result> searchGuguns(Long sidoCode);

    @Getter
    @Builder
    @AllArgsConstructor
    class Result {
        private Long gugunCode;
        private String gugunName;
    }
}
