package com.ssafy.enjoytrip.features.attraction.application.port.in;

import com.ssafy.enjoytrip.features.attraction.domain.ContentTypeId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public interface SearchContentTypeUseCase {
    List<Result> searchContentTypes();

    @Getter
    @Builder
    @AllArgsConstructor
    class Result {
        private ContentTypeId id;
        private String contentTypeName;
    }
}
