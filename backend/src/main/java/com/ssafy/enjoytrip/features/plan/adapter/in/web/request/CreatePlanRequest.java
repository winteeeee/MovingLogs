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
    private String imgId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> attractionIds;
}
