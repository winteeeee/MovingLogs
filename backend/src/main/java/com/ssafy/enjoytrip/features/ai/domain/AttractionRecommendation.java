package com.ssafy.enjoytrip.features.ai.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.enjoytrip.features.attraction.domain.AttractionId;
import lombok.*;

import java.util.List;

@Setter
@Builder
@AllArgsConstructor
@ToString
public class AttractionRecommendation {
    @JsonProperty("recommendations")
    List<AttractionRecommendationVO> recommendations;

    @Setter
    static class AttractionRecommendationVO {
        private Long id;
        private String name;
        private String reason;
    }
}
