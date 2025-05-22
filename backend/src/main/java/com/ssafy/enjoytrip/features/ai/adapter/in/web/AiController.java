package com.ssafy.enjoytrip.features.ai.adapter.in.web;

import com.ssafy.enjoytrip.features.ai.adapter.in.web.request.AttractionRecommendationRequest;
import com.ssafy.enjoytrip.features.ai.domain.AttractionRecommendation;
import com.ssafy.enjoytrip.features.ai.application.port.in.AttractionRecommendationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${api.prefix}/${api.version}/ai")
@RequiredArgsConstructor
@Tag(name = "AiController", description = "AI 관련 API 제공")
public class AiController {
    private final AttractionRecommendationUseCase attractionRecommendationUseCase;

    @PostMapping("/attractions/recommendations")
    @Operation(summary = "AI 기반 지역 관광지 추천", description = "사용자가 입력한 정보를 바탕으로 AI가 관광지를 추천합니다")
    public ResponseEntity<AttractionRecommendation> attractionRecommend(@RequestBody AttractionRecommendationRequest req) {
        AttractionRecommendationUseCase.Command command = AiControllerMapper.toAttractionRecommendationUseCaseCommend(req);
        AttractionRecommendation response =  attractionRecommendationUseCase.recommendAttractions(command);
        return ResponseEntity.ok(response);
    }
}
