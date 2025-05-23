<template>
  <section class="plan-section mb-4">
    <div class="card">
      <div class="card-body">
        <h5 class="section-title mb-4">여행 계획 정보</h5>

        <div class="row g-3 mb-4">
          <div class="col-md-6">
            <div class="form-group">
              <label for="planTitle" class="form-label">여행 계획 이름</label>
              <input
                type="text"
                id="planTitle"
                v-model="localPlan.title"
                class="form-control"
                placeholder="예: 서울 3박 4일 여행"
                @input="updatePlan"
              />
            </div>
          </div>

          <div class="col-md-3">
            <div class="form-group">
              <label for="planStartDate" class="form-label">시작일</label>
              <input
                type="date"
                id="planStartDate"
                v-model="localPlan.startDate"
                class="form-control"
                @input="updatePlan"
              />
            </div>
          </div>

          <div class="col-md-3">
            <div class="form-group">
              <label for="planEndDate" class="form-label">종료일</label>
              <input
                type="date"
                id="planEndDate"
                v-model="localPlan.endDate"
                class="form-control"
                @input="updatePlan"
              />
            </div>
          </div>
        </div>

        <div class="form-group mb-4">
          <label for="planDescription" class="form-label">여행 계획 설명</label>
          <textarea
            id="planDescription"
            v-model="localPlan.description"
            class="form-control"
            rows="3"
            placeholder="여행 계획에 대한 설명을 입력하세요."
            @input="updatePlan"
          ></textarea>
        </div>

        <h6 class="mb-3">선택한 여행 경로</h6>

        <div class="selected-spots-container" v-if="selectedSpots.length > 0">
          <PlanningWaypointList
            :spots="selectedSpots"
            @move-up="moveSpotUp"
            @move-down="moveSpotDown"
            @remove="removeSpot"
            @focus="focusSpot"
          />
        </div>

        <div class="no-selected-spots" v-else>
          <p class="text-center text-secondary">
            <i class="bi bi-map fs-4 d-block mb-2"></i>
            선택한 여행 경로가 없습니다. 위 지도에서 관광지를 검색하고 추가해보세요.
          </p>
        </div>

        <div class="d-flex justify-content-end mt-4">
          <button class="btn btn-secondary me-2" @click="cancel">취소</button>
          <button class="btn btn-primary" @click="save" :disabled="!isValid">저장</button>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { reactive, watch } from 'vue'
import PlanningWaypointList from './PlanningWaypointList.vue'

const props = defineProps({
  planInfo: {
    type: Object,
    required: true,
  },
  selectedSpots: {
    type: Array,
    required: true,
  },
  isValid: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits([
  'update:route-plan',
  'save',
  'cancel',
  'move-spot-up',
  'move-spot-down',
  'remove-spot',
  'focus-spot',
])

// 로컬 여행 계획 정보
const localPlan = reactive({
  title: props.planInfo.title,
  startDate: props.planInfo.startDate,
  endDate: props.planInfo.endDate,
  description: props.planInfo.description,
})

// props 변경 감지
watch(
  () => props.planInfo,
  (newPlan) => {
    localPlan.title = newPlan.title
    localPlan.startDate = newPlan.startDate
    localPlan.endDate = newPlan.endDate
    localPlan.description = newPlan.description
  },
  { deep: true },
)

// 여행 계획 정보 업데이트
function updatePlan() {
  emit('update:route-plan', { ...localPlan })
}

// 여행 계획 저장
function save() {
  emit('save')
}

// 여행 계획 취소
function cancel() {
  emit('cancel')
}

// 관광지 순서 위로 이동
function moveSpotUp(index) {
  emit('move-spot-up', index)
}

// 관광지 순서 아래로 이동
function moveSpotDown(index) {
  emit('move-spot-down', index)
}

// 관광지 제거
function removeSpot(index) {
  emit('remove-spot', index)
}

// 관광지 포커스
function focusSpot(spot) {
  emit('focus-spot', spot)
}
</script>

<style scoped>
.section-title {
  font-weight: 700;
  color: #333;
  position: relative;
  padding-left: 16px;
}

.section-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 6px;
  height: 20px;
  width: 4px;
  background-color: var(--primary-color);
  border-radius: 2px;
}

.card {
  border: none;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  border-radius: 8px;
}

.selected-spots-container {
  border: 1px solid #e9ecef;
  border-radius: 4px;
  max-height: 400px;
  overflow-y: auto;
}

.no-selected-spots {
  padding: 40px 0;
}
</style>
