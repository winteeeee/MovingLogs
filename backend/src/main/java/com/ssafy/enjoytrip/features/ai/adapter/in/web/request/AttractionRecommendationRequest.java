package com.ssafy.enjoytrip.features.ai.adapter.in.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AttractionRecommendationRequest {
    private Long areaCode;
    private Long guGunCode;
    private Long contentTypeCode;
}
