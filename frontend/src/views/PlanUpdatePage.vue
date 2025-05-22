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

      <!-- 여행 장소 목록 -->
      <PlanSpotList
        :spots="plan.spots"
        @update:spots="updateSpots"
        @edit-spot="editSpot"
        @delete-spot="deleteSpot"
      />

      <!-- 장소 추가 영역 -->
      <PlanAddSpot @add-spot="addSpot" />
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
      v-if="showDeleteSpotModal"
      title="장소 삭제"
      :message="`'${spotToDelete?.name}' 장소를 정말 삭제하시겠습니까?`"
      confirm-text="삭제"
      confirm-type="danger"
      @confirm="confirmDeleteSpot"
      @close="showDeleteSpotModal = false"
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
import PlanSpotList from '@/components/plan-update/PlanSpotList.vue'
import PlanAddSpot from '@/components/plan-update/PlanAddSpot.vue'
import ConfirmModal from '@/components/plan-update/ConfirmModal.vue'

// 상태 관리
const originalPlan = ref(null)
const plan = ref({ plan: [] })
const isSaving = ref(false)
const showCancelModal = ref(false)
const showDeleteSpotModal = ref(false)
const showDeletePlanModal = ref(false)
const spotToDelete = ref(null)

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
function updateSpots(updatedSpots) {
  plan.value.spots = [...updatedSpots]
}

// 장소 추가
function addSpot(newSpot) {
  plan.value.spots.push({
    ...newSpot,
    id: `spot-${Date.now()}`,
    order: plan.value.spots.length,
  })
}

// 장소 수정
function editSpot(updatedSpot) {
  const index = plan.value.spots.findIndex((spot) => spot.id === updatedSpot.id)
  if (index !== -1) {
    plan.value.spots[index] = { ...updatedSpot }
  }
}

// 장소 삭제 확인
function deleteSpot(spot) {
  spotToDelete.value = spot
  showDeleteSpotModal.value = true
}

// 장소 삭제 실행
function confirmDeleteSpot() {
  if (!spotToDelete.value) return

  plan.value.spots = plan.value.spots.filter((spot) => spot.id !== spotToDelete.value.id)

  // 순서 재정렬
  plan.value.spots.forEach((spot, index) => {
    spot.order = index
  })

  showDeleteSpotModal.value = false
  spotToDelete.value = null
}

// 여행 계획 저장
async function savePlan() {
  if (!hasChanges.value) return

  isSaving.value = true

  try {
    // 실제 구현: API 호출로 저장
    await new Promise((resolve) => setTimeout(resolve, 1000))

    // 저장 성공 시 원본 데이터 업데이트
    originalPlan.value = JSON.parse(JSON.stringify(plan.value))

    // 알림 표시
    alert('여행 계획이 저장되었습니다.')
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
  // 실제 구현: router.push('/travel-plans')
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
    // 실제 구현: API 호출로 데이터 가져오기
    // 여기서는 더미 데이터 사용
    const dummyPlan = {
      id: '1',
      title: '서울 3박 4일 여행',
      destination: '서울',
      startDate: '2025-06-10',
      endDate: '2025-06-13',
      description: '서울의 주요 관광지와 맛집을 탐방하는 여행',
      isPublic: true,
      thumbnail: 'https://via.placeholder.com/800x400?text=Seoul',
      spots: [
        {
          id: 'spot-1',
          name: '경복궁',
          address: '서울 종로구 사직로 161',
          category: '관광',
          description: '조선시대 대표적인 궁궐',
          order: 0,
          location: { lat: 37.579617, lng: 126.977041 },
        },
        {
          id: 'spot-2',
          name: '광화문 광장',
          address: '서울 종로구 세종로',
          category: '관광',
          description: '서울의 중심 광장',
          order: 1,
          location: { lat: 37.572976, lng: 126.976757 },
        },
        {
          id: 'spot-3',
          name: '명동',
          address: '서울 중구 명동길',
          category: '쇼핑',
          description: '쇼핑과 맛집의 중심지',
          order: 2,
          location: { lat: 37.563576, lng: 126.983431 },
        },
      ],
      createdAt: '2025-05-15T09:30:00Z',
      updatedAt: '2025-05-18T14:20:00Z',
    }

    // 깊은 복사로 원본 데이터 저장
    originalPlan.value = JSON.parse(JSON.stringify(dummyPlan))
    plan.value = JSON.parse(JSON.stringify(dummyPlan))
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
