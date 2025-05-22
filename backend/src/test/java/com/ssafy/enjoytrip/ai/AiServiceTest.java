package com.ssafy.enjoytrip.ai;

import com.ssafy.enjoytrip.features.ai.adapter.in.web.request.AttractionRecommendationRequest;
import com.ssafy.enjoytrip.features.ai.application.AiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AiServiceTest {
    @Autowired
    AiService aiService;

    @Test
    void attractionRecommendTest() {
        AttractionRecommendationRequest req = AttractionRecommendationRequest.builder()
                .contentTypeCode(32L)
                .areaCode(1L)
                .guGunCode(1L)
                .build();
        //System.out.println(aiService.attractionRecommend(req));
    }
}
