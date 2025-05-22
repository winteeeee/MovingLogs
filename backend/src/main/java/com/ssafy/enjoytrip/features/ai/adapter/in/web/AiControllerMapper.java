package com.ssafy.enjoytrip.features.ai.adapter.in.web;

import com.ssafy.enjoytrip.features.ai.adapter.in.web.request.AttractionRecommendationRequest;
import com.ssafy.enjoytrip.features.ai.application.port.in.AttractionRecommendationUseCase;

public class AiControllerMapper {
    public static AttractionRecommendationUseCase.Command toAttractionRecommendationUseCaseCommend(AttractionRecommendationRequest request) {
        return AttractionRecommendationUseCase.Command.builder()
                .areaCode(request.getAreaCode())
                .guGunCode(request.getGuGunCode())
                .contentTypeCode(request.getContentTypeCode())
                .build();
    }
}
