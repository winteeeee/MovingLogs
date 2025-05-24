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
          @write-tripost="writeTripost"
        />
      </div>

      <Pagination
        v-if="totalPages >= 1"
        :total="totalPages"
        :current="currentPage"
        @page-change="onPageChange"
      />
    </div>

    <!-- 삭제 확인 모달 -->
    <TravelPlanDeleteModal
      v-if="isDeleteModalVisible"
      :plan="planToDelete"
      @close="closeDeleteModal"
      @confirm="deletePlan"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useAuthStore } from '@/stores/authStore.js'
import axios from 'axios'
import TravelPlanHeader from '@/components/my-trip-plan/TravelPlanHeader.vue'
import TravelPlanFilter from '@/components/my-trip-plan/TravelPlanFilter.vue'
import TravelPlanCard from '@/components/my-trip-plan/TravelPlanCard.vue'
import TravelPlanEmpty from '@/components/my-trip-plan/TravelPlanEmpty.vue'
import TravelPlanDeleteModal from '@/components/my-trip-plan/TravelPlanDeleteModal.vue'
import Pagination from '@/components/common/Pagination.vue'
import { useRouter } from 'vue-router'
import api from '@/api/axios.js'

const serverUrl = import.meta.env.VITE_API_SERVER_URL
const authStore = useAuthStore()
const router = useRouter()

// 상태 관리
const travelPlans = ref([])
const isLoading = ref(true)
const planToDelete = ref(null)
const isDeleteModalVisible = ref(false)
const currentPage = ref(1)
const totalPages = ref(0)
const itemsPerPage = 6


function onPageChange(page) {
  if (1 <= page && page <= totalPages.value) {
    currentPage.value = page
  }
}

watch(currentPage, async (newPage) => {
  await loadTravelPlans()
})

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
  router.push('/planning')
}

// 여행 계획 상세 보기
function viewPlan(plan) {
  console.log('여행 계획 상세 보기:', plan.id)
  router.push(`/plan/detail/${plan.id}`);
}

// 여행 계획 수정
function editPlan(plan) {
  console.log('여행 계획 수정:', plan.id)
  router.push(`/plan/update/${plan.id}`)
}

// 여행 계획 삭제 확인
function confirmDeletePlan(plan) {
  planToDelete.value = plan
  isDeleteModalVisible.value = true

  // 모달 표시 시 body 스크롤 방지
  document.body.style.overflow = 'hidden'
}

function writeTripost(plan) {
  router.push({
    name: 'TripostWritePage',
    params: {'planId': plan.id},
  });
}

// 삭제 모달 닫기
function closeDeleteModal() {
  isDeleteModalVisible.value = false

  // 모달 닫을 때 body 스크롤 복원
  document.body.style.overflow = ''
}

// 여행 계획 삭제
async function deletePlan() {
  if (!planToDelete.value) return

  console.log('여행 계획 삭제:', planToDelete.value.id)
  await api.delete(`${serverUrl}/api/v1/plans/${planToDelete.value.id}`)
  // 모달 닫기
  closeDeleteModal()

  // 알림 표시
  alert('여행 계획이 삭제되었습니다.')
  await loadTravelPlans()
}

// 데이터 로드
async function loadTravelPlans() {
  isLoading.value = true
  const response = await axios.get(`${serverUrl}/api/v1/plans`, {
    params: {
      page: currentPage.value,
      pageSize: itemsPerPage,
    },
    headers: {
      Authorization: `Bearer ${authStore.accessToken}`,
    },
  })

  const data = response.data
  console.log(data)
  travelPlans.value = data.content
  totalPages.value = data.totalPages
  isLoading.value = false
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
onMounted(async () => {
  await loadTravelPlans()
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
