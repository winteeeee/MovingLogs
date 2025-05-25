package com.ssafy.enjoytrip.features.ai.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @Getter
    static class AttractionRecommendationVO {
        private String id;
        private String title;
        private String firstImage1;
        private Double latitude;
        private Double longitude;
        private String addr1;
        private String reason;
    }
}
