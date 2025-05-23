package com.ssafy.enjoytrip.plan;

import com.ssafy.enjoytrip.features.plan.adapter.in.web.PlanController;
import com.ssafy.enjoytrip.features.plan.adapter.in.web.request.CreatePlanRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class PlanTest {
    @Autowired
    PlanController planController;

    @Test
    void 계획_생성_테스트() {
        List<String> attractions = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            attractions.add(String.valueOf(100000 + i));
        }

        CreatePlanRequest request = CreatePlanRequest.builder()
                .title("테스트 제목")
                .desc("테스트 설명")
                .thumbnailUrl("http://localhost:8080/asd.jpg")
                .startDate(LocalDate.of(2025, 5, 23))
                .endDate(LocalDate.of(2025, 5, 25))
                .attractionIds(attractions)
                .build();

        planController.insertPlan(request);
    }
}
