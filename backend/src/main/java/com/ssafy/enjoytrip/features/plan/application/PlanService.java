package com.ssafy.enjoytrip.features.plan.application;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.plan.application.port.in.*;
import com.ssafy.enjoytrip.features.plan.application.port.out.*;
import com.ssafy.enjoytrip.features.plan.domain.Plan;
import com.ssafy.enjoytrip.features.plan.domain.PlanDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService implements SearchPlanUseCase, CreatePlanUseCase, UpdatePlanUseCase, DeletePlanUseCase {
    @Value("${paging.size}")
    private Integer pageSize;
    private final CreatePlanPort createPlanPort;
    private final DeletePlanPort deletePlanPort;
    private final SearchPlanPort searchPlanPort;
    private final UpdatePlanPort updatePlanPort;
    private final CountPlanPort countPlanPort;
    private final CreatePlanDetailPort createPlanDetailPort;
    private final DeletePlanDetailPort deletePlanDetailPort;

    @Override
    @Transactional
    public CreatePlanUseCase.Result createPlan(CreatePlanUseCase.Command command) {
        //일단 커맨드에서 도메인 객체로 변환
        Plan plan = PlanServiceMapper.toPlan(command);
        //이후 포트를 통해 플랜을 생성
        CreatePlanUseCase.Result result = PlanServiceMapper.toCreatePlanUseCaseResult(createPlanPort.createPlan(plan));
        //result에서 pk값을 가져와서 할당하고 플랜 디테일들을 추가적으로 삽입
        List<PlanDetail> planDetails = plan.getPlanDetails();
        for (PlanDetail planDetail : planDetails) {
            planDetail.setPlanId(result.getPlanId());
        }
        createPlanDetailPort.createPlanDetails(plan.getPlanDetails());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public PageDto<SearchPlanUseCase.Result> searchPlans(SearchPlanUseCase.Command command) {
        List<Plan> planList = searchPlanPort.searchPlanByUid(command.getUid(), command.getPage());
        List<SearchPlanUseCase.Result> content = PlanServiceMapper.toSearchPlanUserCaseResultList(planList);
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
        //원래 있던 플랜 디테일은 모두 삭제
        deletePlanDetailPort.deletePlanDetailsByPlanId(command.getId());
        //새롭게 입력된 플랜 디테일로 다시 삽입
        createPlanDetailPort.createPlanDetails(PlanDetailServiceMapper.toPlanDetailList(command.getAttractionIds()));
        //이후 본래의 여행 계획 업데이트
        updatePlanPort.updatePlan(PlanServiceMapper.toPlan(command));
    }

    @Override
    @Transactional
    public void deletePlan(DeletePlanUseCase.Command command) {
        //일단 플랜 디테일을 먼저 삭제하고
        deletePlanDetailPort.deletePlanDetailsByPlanId(command.getId());
        //그 다음 플랜 자체를 삭제
        deletePlanPort.deletePlan(command.getId());
    }
}
