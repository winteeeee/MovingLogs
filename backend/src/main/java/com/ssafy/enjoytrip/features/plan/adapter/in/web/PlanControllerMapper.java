package com.ssafy.enjoytrip.features.plan.adapter.in.web;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.plan.adapter.in.web.request.CreatePlanRequest;
import com.ssafy.enjoytrip.features.plan.adapter.in.web.request.UpdatePlanRequest;
import com.ssafy.enjoytrip.features.plan.adapter.in.web.response.CreatePlanResponse;
import com.ssafy.enjoytrip.features.plan.adapter.in.web.response.SearchPlanResponse;
import com.ssafy.enjoytrip.features.plan.application.port.in.CreatePlanUseCase;
import com.ssafy.enjoytrip.features.plan.application.port.in.DeletePlanUseCase;
import com.ssafy.enjoytrip.features.plan.application.port.in.SearchPlanUseCase;
import com.ssafy.enjoytrip.features.plan.application.port.in.UpdatePlanUseCase;
import com.ssafy.enjoytrip.features.plan.domain.PlanId;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    public static SearchPlanUseCase.Command toSearchPlanUseCaseCommand(String uid, Integer page) {
        return SearchPlanUseCase.Command.builder()
                .uid(uid)
                .page(page)
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

    public static SearchPlanResponse toSearchPlanResponse(SearchPlanUseCase.Result result) {
        //TODO 여행 디테일에서 어트랙션을 조회해서 해당 어트랙션에 존재하는 url 중 하나를 찾아야 함
        LocalDate updateDate = result.getUpdatedAt().toLocalDate();
        LocalDate today = LocalDate.now();
        long dDay = ChronoUnit.DAYS.between(today, updateDate);
        return SearchPlanResponse.builder()
                .id(Long.parseLong(result.getId().getId()))
                .title(result.getTitle())
                //.imageUrl()
                .startDate(result.getStartDate())
                .endDate(result.getEndDate())
                .updatedAt(result.getUpdatedAt())
                .WaypointLength(result.getWaypoints().size())
                .dDay((int) dDay)
                .build();
    }

    public static List<SearchPlanResponse> toSearchPlanResponseList(List<SearchPlanUseCase.Result> results) {
        return results.stream().map(PlanControllerMapper::toSearchPlanResponse).toList();
    }

    public static PageDto<SearchPlanResponse> toSearchPlanResponsePageDto(PageDto<SearchPlanUseCase.Result> result) {
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
}
