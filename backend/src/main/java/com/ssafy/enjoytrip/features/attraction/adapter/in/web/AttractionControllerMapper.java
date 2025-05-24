package com.ssafy.enjoytrip.features.attraction.adapter.in.web;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.attraction.adapter.in.web.response.AttractionResponse;
import com.ssafy.enjoytrip.features.attraction.adapter.in.web.response.ContentTypeResponse;
import com.ssafy.enjoytrip.features.attraction.adapter.in.web.response.GugunResponse;
import com.ssafy.enjoytrip.features.attraction.adapter.in.web.response.SidoResponse;
import com.ssafy.enjoytrip.features.attraction.application.port.in.SearchAttractionUseCase;
import com.ssafy.enjoytrip.features.attraction.application.port.in.SearchContentTypeUseCase;
import com.ssafy.enjoytrip.features.attraction.application.port.in.SearchGugunUseCase;
import com.ssafy.enjoytrip.features.attraction.application.port.in.SearchSidoUseCase;

import java.util.List;

public class AttractionControllerMapper {
    public static ContentTypeResponse toContentTypeResponse(SearchContentTypeUseCase.Result result) {
        return ContentTypeResponse.builder()
                .contentTypeId(result.getId().getId())
                .contentTypeName(result.getContentTypeName())
                .build();
    }

    public static SidoResponse toSidoResponse(SearchSidoUseCase.Result result) {
        return SidoResponse.builder()
                .sidoCode(result.getId().getId())
                .sidoName(result.getSidoName())
                .build();
    }

    public static GugunResponse toGugunResponse(SearchGugunUseCase.Result result) {
        return GugunResponse.builder()
                .gugunCode(result.getGugunCode())
                .gugunName(result.getGugunName())
                .build();
    }

    public static AttractionResponse toAttractionResponse(SearchAttractionUseCase.Result result) {
        return AttractionResponse.builder()
                .id(result.getId().getId())
                .contentId(result.getContentId().getId())
                .title(result.getTitle())
                .contentTypeName(result.getContentTypeName())
                .sidoName(result.getSidoName())
                .gugunName(result.getGugunName())
                .firstImage1(result.getFirstImage1())
                .firstImage2(result.getFirstImage2())
                .mapLevel(result.getMapLevel())
                .latitude(result.getLatitude())
                .longitude(result.getLongitude())
                .tel(result.getTel())
                .addr1(result.getAddr1())
                .addr2(result.getAddr2())
                .homepage(result.getHomepage())
                .overview(result.getOverview())
                .build();
    }

    public static List<ContentTypeResponse> toContentTypeResponseList(List<SearchContentTypeUseCase.Result> results) {
        return results.stream().map(AttractionControllerMapper::toContentTypeResponse).toList();
    }

    public static List<SidoResponse> toSidoResponseList(List<SearchSidoUseCase.Result> results) {
        return results.stream().map(AttractionControllerMapper::toSidoResponse).toList();
    }

    public static List<GugunResponse> toGuGunResponseList(List<SearchGugunUseCase.Result> results) {
        return results.stream().map(AttractionControllerMapper::toGugunResponse).toList();
    }

    public static List<AttractionResponse> toAttractionResponseList(List<SearchAttractionUseCase.Result> results) {
        return results.stream().map(AttractionControllerMapper::toAttractionResponse).toList();
    }

    public static PageDto<AttractionResponse> toAttractionResponsePageDto(PageDto<SearchAttractionUseCase.Result> result) {
        List<AttractionResponse> content = toAttractionResponseList(result.getContent());
        return new PageDto<>(
                content,
                result.getPage(),
                result.getSize(),
                result.getTotalPages(),
                result.getTotalElements(),
                result.getHasPrevious(),
                result.getHasNext()
        );
    }
}
