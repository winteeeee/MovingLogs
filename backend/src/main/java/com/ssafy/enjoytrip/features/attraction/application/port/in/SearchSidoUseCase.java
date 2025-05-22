package com.ssafy.enjoytrip.features.attraction.application.port.in;

import com.ssafy.enjoytrip.features.attraction.domain.SidoId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public interface SearchSidoUseCase {
    List<Result> searchSidos();

    @Getter
    @Builder
    @AllArgsConstructor
    class Result {
        private SidoId id;
        private String sidoName;
    }
}
