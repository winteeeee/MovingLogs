package com.ssafy.enjoytrip.features.plan.application;

import com.ssafy.enjoytrip.features.plan.application.port.in.CreatePlanUseCase;
import com.ssafy.enjoytrip.features.plan.application.port.in.SearchMyPlansUseCase;
import com.ssafy.enjoytrip.features.plan.application.port.in.UpdatePlanUseCase;
import com.ssafy.enjoytrip.features.plan.domain.Plan;
import com.ssafy.enjoytrip.features.plan.domain.Waypoint;
import com.ssafy.enjoytrip.features.plan.domain.PlanId;
import com.ssafy.enjoytrip.features.user.domain.Uid;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PlanServiceMapper {
    public static Plan toPlan(CreatePlanUseCase.Command command) {
        List<Waypoint> waypointList = WaypointServiceMapper.toWaypointList(command.getAttractionIds());
       return Plan.builder()
               .title(command.getTitle())
               .description(command.getDesc())
               .thumbnailUrl(command.getThumbnailUrl())
               .startDate(command.getStartDate())
               .endDate(command.getEndDate())
               .uid(new Uid(String.valueOf(command.getUid())))
               .waypoints(waypointList)
               .build();
    }

    public static Plan toPlan(UpdatePlanUseCase.Command command) {
        List<Waypoint> waypointList = WaypointServiceMapper.toWaypointList(command.getAttractionIds());
        return Plan.builder()
                .id(command.getId())
                .title(command.getTitle())
                .description(command.getDesc())
                .thumbnailUrl(command.getThumbnailUrl())
                .startDate(command.getStartDate())
                .endDate(command.getEndDate())
                .waypoints(waypointList)
                .build();
    }

    public static CreatePlanUseCase.Result toCreatePlanUseCaseResult(PlanId planId) {
        return CreatePlanUseCase.Result.builder()
                .planId(planId)
                .build();
    }

    public static SearchMyPlansUseCase.Result toSearchPlanUseCaseResult(Plan plan) {
        LocalDate today = LocalDate.now();
        LocalDate target = plan.getUpdatedAt().toLocalDate();
        return SearchMyPlansUseCase.Result.builder()
                .id(plan.getId())
                .title(plan.getTitle())
                .description(plan.getDescription())
                .thumbnailUrl(plan.getThumbnailUrl())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .updatedAt(plan.getUpdatedAt())
                .dDay(ChronoUnit.DAYS.between(today, target))
                .build();
    }

    public static List<SearchMyPlansUseCase.Result> toSearchPlanUserCaseResultList(List<Plan> plans) {
        return plans.stream().map(PlanServiceMapper::toSearchPlanUseCaseResult).toList();
    }
}
