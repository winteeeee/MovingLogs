package com.ssafy.enjoytrip.features.ai.application;

import com.ssafy.enjoytrip.features.ai.domain.AttractionRecommendation;
import com.ssafy.enjoytrip.features.ai.application.port.in.AttractionRecommendationUseCase;
import com.ssafy.enjoytrip.features.ai.application.port.out.RecommendAttractionPort;
import com.ssafy.enjoytrip.features.attraction.application.port.out.SearchAttractionPort;
import com.ssafy.enjoytrip.features.attraction.domain.Attraction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiService implements AttractionRecommendationUseCase {
    private final RecommendAttractionPort recommendAttractionPort;
    private final SearchAttractionPort searchAttractionPort;

    @Override
    public AttractionRecommendation recommendAttractions(AttractionRecommendationUseCase.Command req) {
        StringBuilder sb = new StringBuilder();
        List<Attraction> attractions = searchAttractionPort.searchAttractionsWithLimit(
                req.getContentTypeCode(),
                req.getAreaCode(),
                req.getGuGunCode(),
                100L
        );

        sb.append("아래부터는 사용자의 DB입니다. 아래의 내용 중 상위 3개를 선정 이유와 함께 제공하세요.\n");
        for (Attraction attraction : attractions) {
            sb.append(attraction.toString()).append('\n');
        }
        return recommendAttractionPort.attractionRecommend(sb.toString());
    }
}
