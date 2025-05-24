package com.ssafy.enjoytrip.features.attraction.application;

import com.ssafy.enjoytrip.features.attraction.application.port.in.SearchAttractionUseCase;
import com.ssafy.enjoytrip.features.attraction.application.port.in.SearchContentTypeUseCase;
import com.ssafy.enjoytrip.features.attraction.application.port.in.SearchGugunUseCase;
import com.ssafy.enjoytrip.features.attraction.application.port.in.SearchSidoUseCase;
import com.ssafy.enjoytrip.features.attraction.domain.Attraction;
import com.ssafy.enjoytrip.features.attraction.domain.ContentType;
import com.ssafy.enjoytrip.features.attraction.domain.Gugun;
import com.ssafy.enjoytrip.features.attraction.domain.Sido;

import java.util.List;

public class AttractionServiceMapper {
    public static SearchAttractionUseCase.Result toSearchAttractionUseCaseResult(Attraction attraction) {
        return SearchAttractionUseCase.Result.builder()
                .id(attraction.getId())
                .contentId(attraction.getContentTypeId())
                .contentTypeName(attraction.getContentTypeName())
                .title(attraction.getTitle())
                .sidoName(attraction.getAreaName())
                .gugunName(attraction.getSiGunGuName())
                .firstImage1(attraction.getFirstImage1())
                .firstImage2(attraction.getFirstImage2())
                .mapLevel(attraction.getMapLevel())
                .latitude(attraction.getLatitude())
                .longitude(attraction.getLongitude())
                .tel(attraction.getTel())
                .addr1(attraction.getAddr1())
                .addr2(attraction.getAddr2())
                .homepage(attraction.getHomepage())
                .overview(attraction.getOverview())
                .build();
    }

    public static SearchContentTypeUseCase.Result toSearchContentTypeUseCaseResult(ContentType contentType) {
        return SearchContentTypeUseCase.Result.builder()
                .id(contentType.getId())
                .contentTypeName(contentType.getContentTypeName())
                .build();
    }

    public static SearchGugunUseCase.Result toSearchGugunUseCaseResult(Gugun gugun) {
        return SearchGugunUseCase.Result.builder()
                .gugunCode(gugun.getGugunCode())
                .gugunName(gugun.getGugunName())
                .build();
    }

    public static SearchSidoUseCase.Result toSearchSidoUseCaseResult(Sido sido) {
        return SearchSidoUseCase.Result.builder()
                .id(sido.getId())
                .sidoName(sido.getSidoName())
                .build();
    }

    public static List<SearchAttractionUseCase.Result> toSearchAttractionUseCaseResultList(List<Attraction> attractions) {
        return attractions.stream().map(AttractionServiceMapper::toSearchAttractionUseCaseResult).toList();
    }

    public static List<SearchContentTypeUseCase.Result> toSearchContentTypeUseCaseResultList(List<ContentType> contentTypes) {
        return contentTypes.stream().map(AttractionServiceMapper::toSearchContentTypeUseCaseResult).toList();
    }

    public static List<SearchGugunUseCase.Result> toSearchGugunUseCaseResultList(List<Gugun> guguns) {
        return guguns.stream().map(AttractionServiceMapper::toSearchGugunUseCaseResult).toList();
    }

    public static List<SearchSidoUseCase.Result> toSearchSidoUseCaseResultList(List<Sido> sidos) {
        return sidos.stream().map(AttractionServiceMapper::toSearchSidoUseCaseResult).toList();
    }
}
