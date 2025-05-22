package com.ssafy.enjoytrip.ai;

import com.ssafy.enjoytrip.features.ai.adapter.in.web.AiController;
import com.ssafy.enjoytrip.features.ai.adapter.in.web.request.AttractionRecommendationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AiServiceTest {
    @Autowired
    AiController aiController;

    @Test
    void attractionRecommendTest() {
        AttractionRecommendationRequest req = AttractionRecommendationRequest.builder()
                .contentTypeCode(12L)
                .areaCode(7L)
                .guGunCode(1L)
                .build();
        var result = aiController.attractionRecommend(req);
        System.out.println(result);
    }
}
