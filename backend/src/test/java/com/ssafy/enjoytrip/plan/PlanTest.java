package com.ssafy.enjoytrip.plan;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.plan.adapter.in.web.PlanController;
import com.ssafy.enjoytrip.features.plan.adapter.in.web.request.CreatePlanRequest;
import com.ssafy.enjoytrip.features.plan.adapter.in.web.request.UpdatePlanRequest;
import com.ssafy.enjoytrip.features.plan.adapter.in.web.response.SearchPlanDetailResponse;
import com.ssafy.enjoytrip.features.plan.adapter.in.web.response.SearchPlanResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class PlanTest {
    @Autowired
    PlanController planController;

    @Test
    void 계획_조회_테스트() {
        ResponseEntity<PageDto<SearchPlanResponse>> response = planController.findPlans(1, 5);
        System.out.println(response);
    }

    @Test
    void 상세_계획_조회_테스트() {
        ResponseEntity<SearchPlanDetailResponse> response = planController.findPlanDetail("8d967abe-4917-4b94-8fc8-a60c9344da9f");
        System.out.println(response);
    }

    @Test
    void 계획_생성_테스트() {
        List<String> attractions = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            attractions.add(String.valueOf(100010 + i));
        }

        CreatePlanRequest request = CreatePlanRequest.builder()
                .title("테스트 제목3")
                .desc("테스트 설명3")
                .thumbnailUrl("http://localhost:8080/asd3.jpg")
                .startDate(LocalDate.of(2025, 5, 23))
                .endDate(LocalDate.of(2025, 5, 25))
                .attractionIds(attractions)
                .build();

        planController.insertPlan(request);
    }

    @Test
    void 계획_수정_테스트() {
        List<String> attractions = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            attractions.add(String.valueOf(100000 + i));
        }

        UpdatePlanRequest request = UpdatePlanRequest.builder()
                .id("0b41a106-cdc4-47e2-8d6f-b3d6a7570c87")
                .title("업데이트된 테스트 제목2")
                .desc("설명도 업데이트")
                .thumbnailUrl(null)
                .startDate(null)
                .endDate(null)
                .attractionIds(attractions)
                .build();

        planController.updatePlan(request);
    }

    @Test
    void 계획_삭제_테스트() {
        planController.deletePlan("8d967abe-4917-4b94-8fc8-a60c9344da9f");
    }
}
