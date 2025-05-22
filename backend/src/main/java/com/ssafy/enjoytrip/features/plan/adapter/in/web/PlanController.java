package com.ssafy.enjoytrip.features.plan.adapter.in.web;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.common.util.SecurityUtils;
import com.ssafy.enjoytrip.features.plan.adapter.in.web.request.CreatePlanRequest;
import com.ssafy.enjoytrip.features.plan.adapter.in.web.request.UpdatePlanRequest;
import com.ssafy.enjoytrip.features.plan.adapter.in.web.response.CreatePlanResponse;
import com.ssafy.enjoytrip.features.plan.adapter.in.web.response.SearchPlanResponse;
import com.ssafy.enjoytrip.features.plan.application.port.in.CreatePlanUseCase;
import com.ssafy.enjoytrip.features.plan.application.port.in.DeletePlanUseCase;
import com.ssafy.enjoytrip.features.plan.application.port.in.SearchPlanUseCase;
import com.ssafy.enjoytrip.features.plan.application.port.in.UpdatePlanUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${api.prefix}/${api.version}/plans")
@RequiredArgsConstructor
@Tag(name = "PlanController", description = "여행 계획 API 제공")
public class PlanController {
    private final CreatePlanUseCase createPlanUseCase;
    private final DeletePlanUseCase deletePlanUseCase;
    private final SearchPlanUseCase searchPlanUseCase;
    private final UpdatePlanUseCase updatePlanUseCase;

    @GetMapping
    @Operation(summary = "여행 계획 조회", description = "특정 회원의 여행 계획을 조회한다.")
    public ResponseEntity<PageDto<SearchPlanResponse>> findPlans(@RequestParam Integer page) {
        Long uid = SecurityUtils.getUserUidBySecurityContextHolder();
        SearchPlanUseCase.Command command = PlanControllerMapper.toSearchPlanUseCaseCommand(uid, page);
        PageDto<SearchPlanUseCase.Result> result = searchPlanUseCase.searchPlans(command);
        PageDto<SearchPlanResponse> response = PlanControllerMapper.toSearchPlanResponsePageDto(result);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "여행 계획 생성", description = "여행 계획을 생성한다.")
    public ResponseEntity<CreatePlanResponse> insertPlan(@RequestBody CreatePlanRequest createPlanRequest) {
        Long uid = SecurityUtils.getUserUidBySecurityContextHolder();
        CreatePlanUseCase.Command command = PlanControllerMapper.toCreatePlanUseCaseCommand(createPlanRequest, uid);
        CreatePlanUseCase.Result result = createPlanUseCase.createPlan(command);
        CreatePlanResponse response = PlanControllerMapper.toCreatePlanResponse(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    @Operation(summary = "여행 계획 수정", description = "특정 여행 계획을 수정한다.")
    public ResponseEntity<Void> updatePlan(@RequestBody UpdatePlanRequest updatePlanRequest) {
        UpdatePlanUseCase.Command command = PlanControllerMapper.toUpdatedPlanUseCaseCommand(updatePlanRequest);
        updatePlanUseCase.updatePlan(command);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "여행 계획 삭제", description = "특정 여행 계획을 삭제한다.")
    public ResponseEntity<Void> deletePlan(@PathVariable String id) {
        DeletePlanUseCase.Command command = PlanControllerMapper.toDeletePlanUseCaseCommand(id);
        deletePlanUseCase.deletePlan(command);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}