package com.ssafy.enjoytrip.features.plan.application;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.common.util.UuidFactory;
import com.ssafy.enjoytrip.features.plan.application.port.in.*;
import com.ssafy.enjoytrip.features.plan.application.port.out.*;
import com.ssafy.enjoytrip.features.plan.domain.Plan;
import com.ssafy.enjoytrip.features.plan.domain.PlanId;
import com.ssafy.enjoytrip.features.plan.domain.Waypoint;
import com.ssafy.enjoytrip.features.plan.domain.WaypointId;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService implements SearchMyPlansUseCase, CreatePlanUseCase, UpdatePlanUseCase, DeletePlanUseCase {
    @Value("${paging.size}")
    private Integer pageSize;
    private final CreatePlanPort createPlanPort;
    private final DeletePlanPort deletePlanPort;
    private final SearchPlanPort searchPlanPort;
    private final UpdatePlanPort updatePlanPort;
    private final CountPlanPort countPlanPort;
    private final CreateWaypointPort createWaypointPort;
    private final DeleteWaypointPort deleteWaypointPort;

    @Override
    @Transactional
    public CreatePlanUseCase.Result createPlan(CreatePlanUseCase.Command command) {
        //커맨드에서 도메인 객체로 변환
        Plan plan = PlanServiceMapper.toPlan(command);
        plan.setCreateStatus(UuidFactory.newId(PlanId::new));
        //이후 포트를 통해 플랜을 생성
        createPlanPort.createPlan(plan);

        //plan에서 pk값을 가져와서 할당하고 웨이 포인트들을 추가적으로 삽입
        CreatePlanUseCase.Result result = PlanServiceMapper.toCreatePlanUseCaseResult(plan.getId());
        List<Waypoint> waypoints = plan.getWaypoints();
        for (Waypoint waypoint : waypoints) {
            waypoint.setPlanId(result.getPlanId());
            waypoint.setCreateStatus(UuidFactory.newId(WaypointId::new));
        }
        createWaypointPort.createWaypoints(plan.getWaypoints());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public PageDto<SearchMyPlansUseCase.Result> searchMyPlans(SearchMyPlansUseCase.Command command) {
        List<Plan> planList = searchPlanPort.searchMyPlans(command.getUid(), command.getPage());
        List<SearchMyPlansUseCase.Result> content = PlanServiceMapper.toSearchPlanUserCaseResultList(planList);
        Long totalElements = countPlanPort.countPlanByUid(command.getUid());
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        return new PageDto<>(
                content,
                command.getPage(),
                pageSize,
                totalPages,
                totalElements,
                command.getPage() > 0,
                command.getPage() + 1 < totalPages
        );
    }

    @Override
    @Transactional
    public void updatePlan(UpdatePlanUseCase.Command command) {
        //원래 있던 웨이 포인트는 모두 삭제
        deleteWaypointPort.deleteWaypointsByPlanId(command.getId());
        //새롭게 입력된 웨이 포인트로 다시 삽입
        List<Waypoint> waypoints = WaypointServiceMapper.toWaypointList(command.getAttractionIds());
        for (Waypoint waypoint : waypoints) {
            waypoint.setPlanId(command.getId());
            waypoint.setCreateStatus(UuidFactory.newId(WaypointId::new));
        }
        createWaypointPort.createWaypoints(waypoints);
        //이후 여행 계획 업데이트
        Plan plan = PlanServiceMapper.toPlan(command);
        plan.setUpdateStatus();
        updatePlanPort.updatePlan(plan);
    }

    @Override
    public void deletePlan(DeletePlanUseCase.Command command) {
        deletePlanPort.deletePlan(command.getId(), LocalDateTime.now());
    }
}
