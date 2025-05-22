package com.ssafy.enjoytrip.features.plan.application;

import com.ssafy.enjoytrip.features.plan.application.port.in.CreatePlanUseCase;
import com.ssafy.enjoytrip.features.plan.application.port.in.SearchPlanUseCase;
import com.ssafy.enjoytrip.features.plan.application.port.in.UpdatePlanUseCase;
import com.ssafy.enjoytrip.features.plan.domain.Plan;
import com.ssafy.enjoytrip.features.plan.domain.PlanDetail;
import com.ssafy.enjoytrip.features.plan.domain.PlanId;
import com.ssafy.enjoytrip.features.user.domain.Uid;

import java.time.LocalDateTime;
import java.util.List;

public class PlanServiceMapper {
    public static Plan toPlan(CreatePlanUseCase.Command command) {
        List<PlanDetail> planDetailList = PlanDetailServiceMapper.toPlanDetailList(command.getAttractionIds());
       return Plan.builder()
               .title(command.getTitle())
               .description(command.getDesc())
               //TODO .image()
               .startDate(command.getStartDate())
               .endDate(command.getEndDate())
               .uid(new Uid(String.valueOf(command.getUid())))
               .planDetails(planDetailList)
               .createdAt(LocalDateTime.now())
               .updatedAt(LocalDateTime.now())
               .isDeleted(false)
               .build();
    }

    public static Plan toPlan(UpdatePlanUseCase.Command command) {
        List<PlanDetail> planDetailList = PlanDetailServiceMapper.toPlanDetailList(command.getAttractionIds());
        return Plan.builder()
                .title(command.getTitle())
                .description(command.getDesc())
                //TODO .image()
                .startDate(command.getStartDate())
                .endDate(command.getEndDate())
                .planDetails(planDetailList)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static CreatePlanUseCase.Result toCreatePlanUseCaseResult(PlanId planId) {
        return CreatePlanUseCase.Result.builder()
                .planId(planId)
                .build();
    }

    public static SearchPlanUseCase.Result toSearchPlanUseCaseResult(Plan plan) {
        return SearchPlanUseCase.Result.builder()
                .id(plan.getId())
                .title(plan.getTitle())
                .planDetails(plan.getPlanDetails())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .updatedAt(plan.getUpdatedAt())
                .build();
    }

    public static List<SearchPlanUseCase.Result> toSearchPlanUserCaseResultList(List<Plan> plans) {
        return plans.stream().map(PlanServiceMapper::toSearchPlanUseCaseResult).toList();
    }
}
