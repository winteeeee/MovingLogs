package com.ssafy.enjoytrip.features.plan.adapter.in.web;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.plan.adapter.in.web.request.CreatePlanRequest;
import com.ssafy.enjoytrip.features.plan.adapter.in.web.request.UpdatePlanRequest;
import com.ssafy.enjoytrip.features.plan.adapter.in.web.response.CreatePlanResponse;
import com.ssafy.enjoytrip.features.plan.adapter.in.web.response.SearchPlanDetailResponse;
import com.ssafy.enjoytrip.features.plan.adapter.in.web.response.SearchPlanResponse;
import com.ssafy.enjoytrip.features.plan.application.port.in.*;
import com.ssafy.enjoytrip.features.plan.domain.PlanId;
import com.ssafy.enjoytrip.features.user.domain.Uid;

import java.util.List;

public class PlanControllerMapper {
    public static CreatePlanUseCase.Command toCreatePlanUseCaseCommand(CreatePlanRequest request, String uid) {
        return CreatePlanUseCase.Command.builder()
                .uid(uid)
                .title(request.getTitle())
                .desc(request.getDesc())
                .thumbnailUrl(request.getThumbnailUrl())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .attractionIds(request.getAttractionIds())
                .build();
    }

    public static SearchMyPlansUseCase.Command toSearchPlanUseCaseCommand(String uid, Integer page, Integer pageSize) {
        return SearchMyPlansUseCase.Command.builder()
                .uid(new Uid(uid))
                .page(page)
                .pageSize(pageSize)
                .build();
    }

    public static UpdatePlanUseCase.Command toUpdatedPlanUseCaseCommand(UpdatePlanRequest request) {
        return UpdatePlanUseCase.Command.builder()
                .id(new PlanId(request.getId()))
                .title(request.getTitle())
                .desc(request.getDesc())
                .thumbnailUrl(request.getThumbnailUrl())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .attractionIds(request.getAttractionIds())
                .build();
    }

    public static DeletePlanUseCase.Command toDeletePlanUseCaseCommand(String id) {
        return DeletePlanUseCase.Command.builder()
                .id(new PlanId(id))
                .build();
    }

    public static CreatePlanResponse toCreatePlanResponse(CreatePlanUseCase.Result result) {
        return CreatePlanResponse.builder()
                .id(result.getPlanId().getId())
                .build();
    }

    public static SearchPlanResponse toSearchPlanResponse(SearchMyPlansUseCase.Result result) {
        return SearchPlanResponse.builder()
                .id(result.getId().getId())
                .title(result.getTitle())
                .desc(result.getDescription())
                .thumbnailUrl(result.getThumbnailUrl())
                .startDate(result.getStartDate())
                .endDate(result.getEndDate())
                .updatedAt(result.getUpdatedAt())
                .dDay(result.getDDay())
                .build();
    }

    public static List<SearchPlanResponse> toSearchPlanResponseList(List<SearchMyPlansUseCase.Result> results) {
        return results.stream().map(PlanControllerMapper::toSearchPlanResponse).toList();
    }

    public static PageDto<SearchPlanResponse> toSearchPlanResponsePageDto(PageDto<SearchMyPlansUseCase.Result> result) {
        List<SearchPlanResponse> content = toSearchPlanResponseList(result.getContent());
        return new PageDto<>(
                content,
                result.getPage(),
                result.getSize(),
                result.getTotalPages(),
                result.getTotalElements(),
                result.getHasPrevious(),
                result.getHasNext()
        );
    }

    public static SearchPlanDetailUseCase.Command toSearchPlanDetailUseCaseCommand(String id) {
        return SearchPlanDetailUseCase.Command.builder()
                .id(new PlanId(id))
                .build();
    }

    public static SearchPlanDetailResponse toSearchPlanDetailResponse(SearchPlanDetailUseCase.Result result) {
        return SearchPlanDetailResponse.builder()
                .id(result.getId().getId())
                .title(result.getTitle())
                .description(result.getDescription())
                .startDate(result.getStartDate())
                .endDate(result.getEndDate())
                .waypointList(result.getAttractions())
                .build();
    }
}
