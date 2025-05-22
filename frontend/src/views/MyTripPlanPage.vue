<template>
  <div class="my-travel-plans-page">
    <!-- 헤더 영역 -->
    <TravelPlanHeader :total-plans="filteredPlans.length" @create-new="navigateToCreate" />

    <!-- 필터 영역 -->
    <TravelPlanFilter :filter="filter" @update:filter="updateFilter" />

    <!-- 여행 계획 목록 영역 -->
    <div class="travel-plans-container">
      <!-- 로딩 상태 -->
      <div v-if="isLoading" class="loading-container">
        <div class="spinner"></div>
        <p class="loading-text">여행 계획을 불러오는 중입니다...</p>
      </div>

      <!-- 빈 상태 (검색 결과 없음) -->
      <TravelPlanEmpty
        v-else-if="filteredPlans.length === 0"
        :has-plans="travelPlans.length > 0"
        :is-filter-active="isFilterActive"
        @clear-filter="clearFilter"
        @create-new="navigateToCreate"
      />

      <!-- 여행 계획 카드 그리드 -->
      <div v-else class="travel-plan-grid">
        <TravelPlanCard
          v-for="plan in filteredPlans"
          :key="plan.id"
          :plan="plan"
          @view="viewPlan"
          @edit="editPlan"
          @delete="confirmDeletePlan"
          @duplicate="duplicatePlan"
        />
      </div>
    </div>

    <!-- 삭제 확인 모달 -->
    <DeleteModal
      v-if="isDeleteModalVisible"
      :plan="planToDelete"
      @close="closeDeleteModal"
      @confirm="deletePlan"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import TravelPlanHeader from '@/components/my-trip-plan/TravelPlanHeader.vue'
import TravelPlanFilter from '@/components/my-trip-plan/TravelPlanFilter.vue'
import TravelPlanCard from '@/components/my-trip-plan/TravelPlanCard.vue'
import TravelPlanEmpty from '@/components/my-trip-plan/TravelPlanEmpty.vue'
import TravelPlanDeleteModal from '@/components/my-trip-plan/TravelPlanDeleteModal.vue'

// 상태 관리
const travelPlans = ref([])
const isLoading = ref(true)
const planToDelete = ref(null)
const isDeleteModalVisible = ref(false)

// 필터 상태
const filter = ref({
  keyword: '',
  dateRange: 'all', // all, upcoming, past
  sortBy: 'newest', // newest, oldest, title
})

// 필터가 활성화되었는지 확인
const isFilterActive = computed(() => {
  return filter.value.keyword !== '' || filter.value.dateRange !== 'all'
})

// 필터링된 여행 계획
const filteredPlans = computed(() => {
  let result = [...travelPlans.value]

  // 키워드 필터링
  if (filter.value.keyword) {
    const keyword = filter.value.keyword.toLowerCase()
    result = result.filter(
      (plan) =>
        plan.title.toLowerCase().includes(keyword) ||
        plan.destination?.toLowerCase().includes(keyword),
    )
  }

  // 날짜 범위 필터링
  const today = new Date()
  today.setHours(0, 0, 0, 0)

  if (filter.value.dateRange === 'upcoming') {
    result = result.filter((plan) => {
      const startDate = new Date(plan.startDate)
      return startDate >= today
    })
  } else if (filter.value.dateRange === 'past') {
    result = result.filter((plan) => {
      const endDate = new Date(plan.endDate)
      return endDate < today
    })
  }

  // 정렬
  result.sort((a, b) => {
    if (filter.value.sortBy === 'newest') {
      return new Date(b.createdAt) - new Date(a.createdAt)
    } else if (filter.value.sortBy === 'oldest') {
      return new Date(a.createdAt) - new Date(b.createdAt)
    } else if (filter.value.sortBy === 'title') {
      return a.title.localeCompare(b.title)
    }
    return 0
  })

  return result
})

// 필터 업데이트
function updateFilter(newFilter) {
  filter.value = { ...newFilter }
}

// 필터 초기화
function clearFilter() {
  filter.value = {
    keyword: '',
    dateRange: 'all',
    sortBy: 'newest',
  }
}

// 여행 계획 생성 페이지로 이동
function navigateToCreate() {
  console.log('새 여행 계획 생성 페이지로 이동')
  // 실제 구현: router.push('/travel-plans/create')
}

// 여행 계획 상세 보기
function viewPlan(plan) {
  console.log('여행 계획 상세 보기:', plan.id)
  // 실제 구현: router.push(`/travel-plans/${plan.id}`)
}

// 여행 계획 수정
function editPlan(plan) {
  console.log('여행 계획 수정:', plan.id)
  // 실제 구현: router.push(`/travel-plans/${plan.id}/edit`)
}

// 여행 계획 삭제 확인
function confirmDeletePlan(plan) {
  planToDelete.value = plan
  isDeleteModalVisible.value = true

  // 모달 표시 시 body 스크롤 방지
  document.body.style.overflow = 'hidden'
}

// 삭제 모달 닫기
function closeDeleteModal() {
  isDeleteModalVisible.value = false

  // 모달 닫을 때 body 스크롤 복원
  document.body.style.overflow = ''
}

// 여행 계획 삭제
function deletePlan() {
  if (!planToDelete.value) return

  console.log('여행 계획 삭제:', planToDelete.value.id)

  // 실제 구현: API 호출로 삭제 처리
  // 여기서는 프론트엔드에서만 삭제하는 것으로 시뮬레이션
  const index = travelPlans.value.findIndex((plan) => plan.id === planToDelete.value.id)
  if (index !== -1) {
    travelPlans.value.splice(index, 1)
  }

  // 모달 닫기
  closeDeleteModal()

  // 알림 표시
  alert('여행 계획이 삭제되었습니다.')
}

// 여행 계획 복제
function duplicatePlan(plan) {
  console.log('여행 계획 복제:', plan.id)

  // 실제 구현: API 호출로 복제 처리
  // 여기서는 프론트엔드에서만 복제하는 것으로 시뮬레이션
  const newPlan = {
    ...plan,
    id: `duplicate-${plan.id}-${Date.now()}`,
    title: `${plan.title} (복사본)`,
    createdAt: new Date().toISOString(),
  }

  travelPlans.value.push(newPlan)

  // 알림 표시
  alert('여행 계획이 복제되었습니다.')
}

// 데이터 로드
function loadTravelPlans() {
  isLoading.value = true

  // 실제 구현: API 호출로 데이터 가져오기
  // 여기서는 더미 데이터 사용
  setTimeout(() => {
    travelPlans.value = [
      {
        id: '1',
        title: '서울 3박 4일 여행',
        destination: '서울',
        startDate: '2025-06-10',
        endDate: '2025-06-13',
        spotCount: 8,
        totalDistance: 42500, // 미터 단위
        thumbnail: 'https://via.placeholder.com/300x200?text=Seoul',
        createdAt: '2025-05-15T09:30:00Z',
        updatedAt: '2025-05-18T14:20:00Z',
      },
      {
        id: '2',
        title: '제주도 힐링 여행',
        destination: '제주도',
        startDate: '2025-07-20',
        endDate: '2025-07-25',
        spotCount: 12,
        totalDistance: 156000,
        thumbnail: 'https://via.placeholder.com/300x200?text=Jeju',
        createdAt: '2025-05-10T11:45:00Z',
        updatedAt: '2025-05-10T11:45:00Z',
      },
      {
        id: '3',
        title: '부산 맛집 투어',
        destination: '부산',
        startDate: '2025-08-05',
        endDate: '2025-08-08',
        spotCount: 10,
        totalDistance: 38700,
        thumbnail: 'https://via.placeholder.com/300x200?text=Busan',
        createdAt: '2025-04-28T16:20:00Z',
        updatedAt: '2025-05-02T10:15:00Z',
      },
      {
        id: '4',
        title: '강원도 스키 여행',
        destination: '평창',
        startDate: '2024-12-20',
        endDate: '2024-12-23',
        spotCount: 5,
        totalDistance: 28500,
        thumbnail: 'https://via.placeholder.com/300x200?text=Gangwon',
        createdAt: '2024-11-15T08:30:00Z',
        updatedAt: '2024-11-15T08:30:00Z',
      },
      {
        id: '5',
        title: '경주 역사 탐방',
        destination: '경주',
        startDate: '2025-04-15',
        endDate: '2025-04-17',
        spotCount: 7,
        totalDistance: 32100,
        thumbnail: 'https://via.placeholder.com/300x200?text=Gyeongju',
        createdAt: '2025-03-01T13:10:00Z',
        updatedAt: '2025-03-05T09:45:00Z',
      },
      {
        id: '6',
        title: '전주 한옥마을 여행',
        destination: '전주',
        startDate: '2025-05-01',
        endDate: '2025-05-03',
        spotCount: 6,
        totalDistance: 18900,
        thumbnail: 'https://via.placeholder.com/300x200?text=Jeonju',
        createdAt: '2025-02-20T10:30:00Z',
        updatedAt: '2025-02-22T16:40:00Z',
      },
    ]

    isLoading.value = false
  }, 1500) // 로딩 시뮬레이션
}

// 필터 변경 감지
watch(
  filter,
  () => {
    console.log('필터 변경됨:', filter.value)
  },
  { deep: true },
)

// 컴포넌트 마운트 시 데이터 로드
onMounted(() => {
  loadTravelPlans()
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
.my-travel-plans-page {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
  margin-bottom: 5%;
  font-family:
    -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans',
    'Helvetica Neue', sans-serif;
  color: #333;
}

/* 여행 계획 컨테이너 */
.travel-plans-container {
  margin-top: 24px;
}

.travel-plan-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}

/* 로딩 상태 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 0;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid rgba(74, 107, 223, 0.3);
  border-radius: 50%;
  border-top-color: #4a6bdf;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.loading-text {
  margin-top: 16px;
  color: #6c757d;
}

/* 반응형 디자인 */
@media (max-width: 992px) {
  .travel-plan-grid {
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  }
}

@media (max-width: 768px) {
  .my-travel-plans-page {
    padding: 16px;
  }
}

@media (max-width: 576px) {
  .travel-plan-grid {
    grid-template-columns: 1fr;
  }
}
</style>
