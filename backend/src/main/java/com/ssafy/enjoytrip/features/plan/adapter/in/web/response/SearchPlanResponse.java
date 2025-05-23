package com.ssafy.enjoytrip.features.plan.adapter.in.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class SearchPlanResponse {
    private String id;
    private String title;
    private String desc;
    private String thumbnailUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime updatedAt;
    private Long dDay;
}
