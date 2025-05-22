package com.ssafy.enjoytrip.features.ai.application.port.in;

import com.ssafy.enjoytrip.features.ai.domain.AttractionRecommendation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


public interface AttractionRecommendationUseCase {
    AttractionRecommendation recommendAttractions(Command command);

    @Getter
    @Builder
    @AllArgsConstructor
    class Command {
        private Long areaCode;
        private Long guGunCode;
        private Long contentTypeCode;
    }
}
