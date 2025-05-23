package com.ssafy.enjoytrip.features.plan.adapter.in.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CreatePlanRequest {
    private String title;
    private String desc;
    private String thumbnailUrl; //프론트에서 이미지 url 하나 선택해서 넘겨주기
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> attractionIds;
}
