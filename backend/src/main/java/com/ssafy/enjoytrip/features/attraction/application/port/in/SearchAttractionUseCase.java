package com.ssafy.enjoytrip.features.attraction.application.port.in;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.attraction.domain.AttractionId;
import com.ssafy.enjoytrip.features.attraction.domain.ContentTypeId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface SearchAttractionUseCase {
    PageDto<Result> searchAttractions(Long contentTypeId, Long areaCode, Long siGunGuCode, Integer page);

    @Getter
    @Builder
    @AllArgsConstructor
    class Result {
        private AttractionId id;
        private ContentTypeId contentId;
        private String title;
        private String contentTypeName;
        private String sidoName;
        private String gugunName;
        private String firstImage1;
        private String firstImage2;
        private Long mapLevel;
        private Double latitude;
        private Double longitude;
        private String tel;
        private String addr1;
        private String addr2;
        private String homepage;
        private String overview;
    }
}
