package com.ssafy.enjoytrip.features.plan.application;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.common.util.UuidFactory;
import com.ssafy.enjoytrip.features.attraction.application.port.out.SearchAttractionPort;
import com.ssafy.enjoytrip.features.attraction.domain.Attraction;
import com.ssafy.enjoytrip.features.attraction.domain.AttractionId;
import com.ssafy.enjoytrip.features.plan.application.port.in.*;
import com.ssafy.enjoytrip.features.plan.application.port.out.*;
import com.ssafy.enjoytrip.features.plan.domain.Plan;
import com.ssafy.enjoytrip.features.plan.domain.PlanId;
import com.ssafy.enjoytrip.features.plan.domain.Waypoint;
import com.ssafy.enjoytrip.features.plan.domain.WaypointId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService implements SearchMyPlansUseCase, SearchPlanDetailUseCase, CreatePlanUseCase, UpdatePlanUseCase, DeletePlanUseCase {
    private final CreatePlanPort createPlanPort;
    private final DeletePlanPort deletePlanPort;
    private final SearchPlanPort searchPlanPort;
    private final UpdatePlanPort updatePlanPort;
    private final CountPlanPort countPlanPort;
    private final SearchWaypointPort searchWaypointPort;
    private final CreateWaypointPort createWaypointPort;
    private final DeleteWaypointPort deleteWaypointPort;
    private final SearchAttractionPort searchAttractionPort;

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
        List<Plan> planList = searchPlanPort.searchMyPlans(command.getUid(), command.getPage(), command.getPageSize());
        List<SearchMyPlansUseCase.Result> content = PlanServiceMapper.toSearchPlanUserCaseResultList(planList);
        Long totalElements = countPlanPort.countPlanByUid(command.getUid());
        int totalPages = (int) Math.ceil((double) totalElements / command.getPageSize());

        return new PageDto<>(
                content,
                command.getPage(),
                command.getPageSize(),
                totalPages,
                totalElements,
                command.getPage() > 0,
                command.getPage() + 1 < totalPages
        );
    }

    @Override
    @Transactional(readOnly = true)
    public SearchPlanDetailUseCase.Result searchPlanDetail(SearchPlanDetailUseCase.Command command) {
        //플랜을 찾고
        Plan plan = searchPlanPort.searchPlan(command.getId());
        //그에 맞는 웨이포인트를 찾음
        List<Waypoint> waypoints = searchWaypointPort.searchWaypointsByPlanId(command.getId());
        plan.injectWaypoints(waypoints);

        //찾은 웨이포인트를 기반으로 관광지 정보를 채움
        List<AttractionId> attractionIds = waypoints.stream().map(Waypoint::getAttractionId).toList();
        List<Attraction> attractions = searchAttractionPort.searchAttractionByIds(attractionIds);
        return PlanServiceMapper.toSearchPlanDetailUseCaseResult(plan, attractions);
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
