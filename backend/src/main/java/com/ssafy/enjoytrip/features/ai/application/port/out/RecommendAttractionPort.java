package com.ssafy.enjoytrip.features.ai.application.port.out;

import com.ssafy.enjoytrip.features.ai.domain.AttractionRecommendation;

public interface RecommendAttractionPort {
    AttractionRecommendation attractionRecommend(String userInput);
}
