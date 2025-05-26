<template>
  <div class="plan-update-page">
    <!-- 헤더 영역 -->
    <PlanUpdateHeader
      :plan="plan"
      :is-saving="isSaving"
      :has-changes="hasChanges"
      @save="savePlan"
      @cancel="confirmCancel"
      @delete="confirmDelete"
    />

    <div class="plan-update-content">
      <!-- 기본 정보 영역 -->
      <PlanBasicInfo :plan="plan" @update:plan="updatePlanBasicInfo" />

      <RouteMap :waypoints="plan.waypointList" :map-visible="showMap" />
      <!-- 여행 장소 목록 -->
      <PlanWaypointList
        :waypointList="plan.waypointList"
        :show-map="showMap"
        @update:waypointList="updateWaypoints"
        @edit-waypoint="editWaypoint"
        @delete-waypoint="deleteWaypoint"
        @toggle-map="toggleMap"
      />

      <!-- 장소 추가 영역 -->
      <PlanAddWaypoint @add-waypoint="addWaypoint" />
    </div>

    <!-- 취소 확인 모달 -->
    <ConfirmModal
      v-if="showCancelModal"
      title="변경 사항 취소"
      message="저장되지 않은 변경 사항이 있습니다. 정말 취소하시겠습니까?"
      confirm-text="취소하기"
      cancel-text="계속 편집"
      @confirm="cancelEdit"
      @close="showCancelModal = false"
    />

    <!-- 스팟 삭제 확인 모달 -->
    <ConfirmModal
      v-if="showDeleteWaypointModal"
      title="장소 삭제"
      :message="`${waypointToDelete?.title} 정말 삭제하시겠습니까?`"
      confirm-text="삭제"
      confirm-type="danger"
      @confirm="confirmDeleteWaypoint"
      @close="showDeleteWaypointModal = false"
    />

    <!-- 여행 계획 삭제 확인 모달 -->
    <ConfirmModal
      v-if="showDeletePlanModal"
      title="여행 계획 삭제"
      message="이 여행 계획을 영구적으로 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다."
      confirm-text="삭제"
      confirm-type="danger"
      @confirm="deletePlan"
      @close="showDeletePlanModal = false"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount, watch } from 'vue'
import PlanUpdateHeader from '@/components/plan-update/PlanUpdateHeader.vue'
import PlanBasicInfo from '@/components/plan-update/PlanBasicInfo.vue'
import PlanWaypointList from '@/components/plan-update/PlanWaypointList.vue'
import PlanAddWaypoint from '@/components/plan-update/PlanAddWaypoint.vue'
import RouteMap from '@/components/common/RouteMap.vue'
import ConfirmModal from '@/components/plan-update/ConfirmModal.vue'
import { useRoute } from 'vue-router'
import { useRouter } from 'vue-router'
import api from '@/api/axios.js'

// 상태 관리
const serverUrl = import.meta.env.VITE_API_SERVER_URL
const route = useRoute()
const router = useRouter()
const originalPlan = ref(null)
const plan = ref({ plan: [] })
const isSaving = ref(false)
const showCancelModal = ref(false)
const showDeleteWaypointModal = ref(false)
const showDeletePlanModal = ref(false)
const waypointToDelete = ref(null)
const showMap = ref(false)

function toggleMap() {
  showMap.value = !showMap.value
}

// 변경 사항 감지
const hasChanges = computed(() => {
  if (!originalPlan.value || !plan.value) return false
  return JSON.stringify(originalPlan.value) !== JSON.stringify(plan.value)
})

// 페이지 이탈 방지
function handleBeforeUnload(e) {
  if (hasChanges.value) {
    e.preventDefault()
    e.returnValue = ''
    return ''
  }
}

// 기본 정보 업데이트
function updatePlanBasicInfo(updatedInfo) {
  plan.value = {
    ...plan.value,
    ...updatedInfo,
  }
}

// 여행 장소 목록 업데이트
function updateWaypoints(updatedWaypoints) {
  plan.value.waypointList = [...updatedWaypoints]
}

// 장소 추가
function addWaypoint(newWaypoint) {
  if (plan.value.waypointList.some((wp) => wp.id.id === newWaypoint.id.id)) {
    alert('이미 추가된 관광지입니다.')
    return
  }

  plan.value.waypointList.push({
    ...newWaypoint,
  })
}

// 장소 수정
function editWaypoint(updatedWaypoint) {
  const index = plan.value.waypointList.findIndex((waypoint) => waypoint.id === updatedWaypoint.id)
  if (index !== -1) {
    plan.value.waypointList[index] = { ...updatedWaypoint }
  }
}

// 장소 삭제 확인
function deleteWaypoint(waypoint) {
  waypointToDelete.value = waypoint
  showDeleteWaypointModal.value = true
}

// 장소 삭제 실행
function confirmDeleteWaypoint() {
  if (!waypointToDelete.value) return

  plan.value.waypointList = plan.value.waypointList.filter(
    (waypoint) => waypoint.id !== waypointToDelete.value.id,
  )

  // 순서 재정렬
  plan.value.waypointList.forEach((waypoint, index) => {
    waypoint.order = index
  })

  showDeleteWaypointModal.value = false
  waypointToDelete.value = null
}

// 여행 계획 저장
async function savePlan() {
  if (!hasChanges.value) return
  isSaving.value = true

  const requestBody = {
    id: plan.value.id,
    title: plan.value.title,
    desc: plan.value.description,
    thumbnailUrl: plan.value.waypointList.find((wp) => wp.firstImage1 !== '').firstImage1,
    startDate: plan.value.startDate,
    endDate: plan.value.endDate,
    attractionIds: plan.value.waypointList.map((item) => item.id.id),
  }
  console.log(requestBody)

  try {
    await api.put(`${serverUrl}/api/v1/plans`, requestBody)
    alert('여행 계획이 저장되었습니다.')
    router.back()
  } catch (error) {
    console.error('저장 실패:', error)
    alert('저장 중 오류가 발생했습니다.')
  } finally {
    isSaving.value = false
  }
}

// 취소 확인
function confirmCancel() {
  if (hasChanges.value) {
    showCancelModal.value = true
  } else {
    cancelEdit()
  }
}

// 편집 취소
function cancelEdit() {
  console.log('편집 취소, 목록 페이지로 이동')
  router.back()
}

// 삭제 확인
function confirmDelete() {
  showDeletePlanModal.value = true
}

// 여행 계획 삭제
async function deletePlan() {
  try {
    // 실제 구현: API 호출로 삭제
    await new Promise((resolve) => setTimeout(resolve, 1000))

    alert('여행 계획이 삭제되었습니다.')
    // 실제 구현: router.push('/travel-plans')
  } catch (error) {
    console.error('삭제 실패:', error)
    alert('삭제 중 오류가 발생했습니다.')
  } finally {
    showDeletePlanModal.value = false
  }
}

// 여행 계획 데이터 로드
async function loadPlanData() {
  try {
    const response = await api.get(`${serverUrl}/api/v1/plans/${route.params.id}`)
    console.log('데이터 로드')
    console.log(response.data)
    plan.value = JSON.parse(JSON.stringify(response.data))
    originalPlan.value = JSON.parse(JSON.stringify(response.data))
  } catch (error) {
    console.error('데이터 로드 실패:', error)
    alert('여행 계획을 불러오는 중 오류가 발생했습니다.')
  }
}

// 컴포넌트 마운트 시 데이터 로드 및 이벤트 리스너 등록
onMounted(() => {
  console.log('PlanUpdatePage - onMounted')
  loadPlanData()
  window.addEventListener('beforeunload', handleBeforeUnload)
})

// 컴포넌트 언마운트 시 이벤트 리스너 제거
onBeforeUnmount(() => {
  window.removeEventListener('beforeunload', handleBeforeUnload)
})
</script>

<style scoped>
/* 기본 스타일 및 리셋 */
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

/* 페이지 레이아웃 */
.plan-update-page {
  padding: 24px;
  max-width: 1000px;
  margin: 0 auto;
  font-family:
    -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans',
    'Helvetica Neue', sans-serif;
  color: #333;
}

/* 콘텐츠 레이아웃 */
.plan-update-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
  margin-top: 24px;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .plan-update-page {
    padding: 16px;
  }
}
</style>
