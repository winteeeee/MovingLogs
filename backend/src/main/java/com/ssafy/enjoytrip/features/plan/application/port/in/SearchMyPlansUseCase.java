package com.ssafy.enjoytrip.features.plan.application.port.in;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.plan.domain.Waypoint;
import com.ssafy.enjoytrip.features.plan.domain.PlanId;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SearchMyPlansUseCase {
    PageDto<Result> searchMyPlans(Command command);

    @Getter
    @Builder
    @AllArgsConstructor
    class Command {
        private Uid uid;
        private Integer page;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    class Result {
        private PlanId id;
        private String title;
        private String description;
        private String thumbnailUrl;
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalDateTime updatedAt;
        private Long dDay;
    }
}
