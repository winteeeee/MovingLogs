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
    private Long id;
    private String title;
    private String imageUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer planDetailLength;
    private LocalDateTime updatedAt;
    private Integer dDay;
}
