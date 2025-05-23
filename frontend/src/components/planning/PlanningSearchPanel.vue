<template>
  <div class="search-panel">
    <div class="panel-header">
      <h5 class="panel-title">관광지 검색</h5>
    </div>

    <div class="panel-body">
      <!-- 검색 필터 -->
      <div class="search-filters">
        <div class="form-group mb-3">
          <label for="region" class="form-label">시도</label>
          <select
            id="region"
            v-model="localSearchParams.region"
            class="form-select form-select-sm"
            @change="onRegionChange"
          >
            <option value="">전체</option>
            <option v-for="region in regions" :key="region.code" :value="region.code">
              {{ region.name }}
            </option>
          </select>
        </div>

        <div class="form-group mb-3">
          <label for="district" class="form-label">구군</label>
          <select
            id="district"
            v-model="localSearchParams.district"
            class="form-select form-select-sm"
            :disabled="!localSearchParams.region"
            @change="updateSearchParams"
          >
            <option value="">전체</option>
            <option
              v-for="district in filteredDistricts"
              :key="district.code"
              :value="district.code"
            >
              {{ district.name }}
            </option>
          </select>
        </div>

        <div class="form-group mb-3">
          <label for="type" class="form-label">관광지 타입</label>
          <select
            id="type"
            v-model="localSearchParams.type"
            class="form-select form-select-sm"
            @change="updateSearchParams"
          >
            <option value="">전체</option>
            <option v-for="type in tourismTypes" :key="type.code" :value="type.code">
              {{ type.name }}
            </option>
          </select>
        </div>

        <div class="form-group mb-3">
          <label for="keyword" class="form-label">검색어</label>
          <div class="input-group input-group-sm">
            <input
              type="text"
              id="keyword"
              v-model="localSearchParams.keyword"
              class="form-control"
              placeholder="관광지명 입력"
              @input="updateSearchParams"
              @keyup.enter="search"
            />
            <button class="btn btn-primary" type="button" @click="search">
              <i class="bi bi-search"></i>
            </button>
          </div>
        </div>
      </div>

      <!-- 검색 및 AI 추천 버튼 -->
      <div class="action-buttons mb-4">
        <button class="btn btn-sm btn-outline-secondary w-50" @click="search">
          <i class="bi bi-search me-1"></i> 검색
        </button>
        <button
          class="btn btn-sm btn-success w-50"
          :disabled="!canRecommend"
          @click="getAIRecommendations"
        >
          <i class="bi bi-robot me-1"></i> AI 추천
        </button>
      </div>

      <!-- 탭 네비게이션 -->
      <ul class="nav nav-tabs nav-fill mb-3" role="tablist">
        <li class="nav-item" role="presentation">
          <button
            class="nav-link"
            :class="{ active: activeTab === 'search' }"
            @click="activeTab = 'search'"
          >
            <i class="bi bi-list-ul me-1"></i> 검색 결과
            <span v-if="searchResults.length > 0" class="badge bg-primary ms-1">{{
              searchResults.length
            }}</span>
          </button>
        </li>
        <li class="nav-item" role="presentation">
          <button
            class="nav-link"
            :class="{ active: activeTab === 'ai' }"
            @click="activeTab = 'ai'"
          >
            <i class="bi bi-robot me-1"></i> AI 추천
            <span v-if="aiRecommendations.length > 0" class="badge bg-success ms-1">{{
              aiRecommendations.length
            }}</span>
          </button>
        </li>
      </ul>

      <!-- 탭 콘텐츠 -->
      <div class="tab-content">
        <!-- 검색 결과 탭 -->
        <div class="tab-pane fade" :class="{ 'show active': activeTab === 'search' }">
          <div v-if="searchResults.length > 0" class="search-results">
            <PlanningSearchResultItem
              v-for="spot in searchResults"
              :key="spot.id"
              :spot="spot"
              :is-selected="isSpotSelected(spot)"
              @toggle="toggleSpot"
            />
          </div>
          <div v-else-if="hasSearched" class="no-results">
            <div class="text-center text-secondary py-4">
              <i class="bi bi-exclamation-circle fs-4 d-block mb-2"></i>
              <p class="mb-0">검색 결과가 없습니다.</p>
              <small>다른 검색어로 시도해보세요.</small>
            </div>
          </div>
          <div v-else class="no-search">
            <div class="text-center text-secondary py-4">
              <i class="bi bi-search fs-4 d-block mb-2"></i>
              <p class="mb-0">검색 조건을 입력하고 검색 버튼을 눌러주세요.</p>
            </div>
          </div>
        </div>

        <!-- AI 추천 탭 -->
        <div class="tab-pane fade" :class="{ 'show active': activeTab === 'ai' }">
          <div v-if="aiRecommendations.length > 0" class="ai-recommendations">
            <div
              v-for="(recommendation, index) in aiRecommendations"
              :key="index"
              class="ai-recommendation-item"
            >
              <div class="recommendation-header">
                <h6 class="recommendation-title">{{ recommendation.title }}</h6>
                <span class="recommendation-address">{{ recommendation.address }}</span>
              </div>
              <p class="recommendation-reason">
                <span class="reason-label">추천 이유:</span> {{ recommendation.reason }}
              </p>
              <button class="btn btn-sm btn-outline-primary" @click="addToSelected(recommendation)">
                <i class="bi bi-plus-circle me-1"></i> 선택에 추가
              </button>
            </div>
          </div>
          <div v-else-if="isLoadingRecommendations" class="ai-loading">
            <div class="d-flex justify-content-center align-items-center p-4">
              <div class="spinner-border text-success me-3" role="status">
                <span class="visually-hidden">Loading...</span>
              </div>
              <p class="mb-0">AI가 최적의 관광지를 추천하고 있습니다...</p>
            </div>
          </div>
          <div v-else class="no-recommendations">
            <div class="text-center text-secondary py-4">
              <i class="bi bi-robot fs-4 d-block mb-2"></i>
              <p class="mb-0">시도, 구군, 관광지 타입을 선택하고</p>
              <p class="mb-0">AI 추천 버튼을 눌러주세요.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import PlanningSearchResultItem from './PlanningSearchResultItem.vue'

const props = defineProps({
  regions: {
    type: Array,
    required: true,
  },
  districts: {
    type: Array,
    required: true,
  },
  tourismTypes: {
    type: Array,
    required: true,
  },
  searchParams: {
    type: Object,
    required: true,
  },
  searchResults: {
    type: Array,
    required: true,
  },
  selectedSpots: {
    type: Array,
    required: true,
  },
  hasSearched: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['update:search-params', 'search', 'toggle-spot', 'fetch-districts'])

// 로컬 검색 파라미터
const localSearchParams = reactive({
  region: props.searchParams.region,
  district: props.searchParams.district,
  type: props.searchParams.type,
  keyword: props.searchParams.keyword,
})

// AI 추천 관련 상태
const aiRecommendations = ref([])
const isLoadingRecommendations = ref(false)
const activeTab = ref('search')

// 필터링된 구군 목록
const filteredDistricts = computed(() => {
  if (!localSearchParams.region) return []

  // 선택된 시도에 해당하는 구군만 필터링
  return props.districts.filter((district) => {
    // 구군 코드가 시도 코드로 시작하는지 확인 (예: 시도 코드가 '01'이면 '01xxx'로 시작하는 구군)
    // 또는 구군 객체에 regionCode 속성이 있다면 그것을 사용
    return (
      district.regionCode === localSearchParams.region ||
      (district.code && district.code.startsWith(localSearchParams.region))
    )
  })
})

// AI 추천 버튼 활성화 여부
const canRecommend = computed(() => {
  return localSearchParams.region && localSearchParams.district && localSearchParams.type
})

// props 변경 감지
watch(
  () => props.searchParams,
  (newParams) => {
    localSearchParams.region = newParams.region
    localSearchParams.district = newParams.district
    localSearchParams.type = newParams.type
    localSearchParams.keyword = newParams.keyword
  },
  { deep: true },
)

// 시도 변경 시 처리
function onRegionChange() {
  // 시도가 변경되면 구군 초기화
  localSearchParams.district = ''

  // 부모 컴포넌트에 시도 변경 알림 (필요한 경우 구군 데이터 요청)
  emit('fetch-districts', localSearchParams.region)

  // 검색 파라미터 업데이트
  updateSearchParams()
}

// 검색 파라미터 업데이트
function updateSearchParams() {
  emit('update:search-params', { ...localSearchParams })
}

// 검색 실행
function search() {
  emit('search')
  activeTab.value = 'search' // 검색 결과 탭으로 전환
}

// 관광지가 선택되었는지 확인
function isSpotSelected(spot) {
  return props.selectedSpots.some((selected) => selected.id === spot.id)
}

// 관광지 선택/해제
function toggleSpot(spot) {
  emit('toggle-spot', spot)
}

// AI 추천 관광지 가져오기
function getAIRecommendations() {
  // 이미 로딩 중이면 중복 요청 방지
  if (isLoadingRecommendations.value) return

  activeTab.value = 'ai' // AI 추천 탭으로 전환
  isLoadingRecommendations.value = true
  aiRecommendations.value = [] // 기존 추천 초기화

  // 서버 연동 시뮬레이션 (실제로는 API 호출로 대체)
  setTimeout(() => {
    // 더미 데이터 생성 (실제로는 서버에서 받아온 데이터 사용)
    const dummyRecommendations = [
      {
        id: 'ai-rec-1',
        title: '경복궁',
        address: '서울특별시 종로구 사직로 161',
        reason:
          '선택하신 지역에서 가장 인기 있는 역사 관광지로, 조선시대 왕실 문화를 체험할 수 있습니다. 주변에 다양한 문화 시설과 맛집이 있어 하루 코스로 추천합니다.',
        type: '역사/문화',
        image: '/images/gyeongbokgung.jpg',
      },
      {
        id: 'ai-rec-2',
        title: '북촌 한옥마을',
        address: '서울특별시 종로구 계동길 37',
        reason:
          '전통 한옥의 아름다움을 감상할 수 있는 곳으로, 경복궁과 가까워 연계 관광이 가능합니다. 전통 공예 체험과 한복 체험도 추천합니다.',
        type: '역사/문화',
        image: '/images/bukchon.jpg',
      },
      {
        id: 'ai-rec-3',
        title: '남산서울타워',
        address: '서울특별시 용산구 남산공원길 105',
        reason:
          '서울의 전경을 한눈에 볼 수 있는 랜드마크로, 야경이 특히 아름답습니다. 연인과 함께 방문하기 좋은 로맨틱한 장소입니다.',
        type: '자연/경관',
        image: '/images/namsan.jpg',
      },
    ]

    aiRecommendations.value = dummyRecommendations
    isLoadingRecommendations.value = false
  }, 2000) // 2초 후 결과 표시
}

// AI 추천 관광지를 선택에 추가
function addToSelected(recommendation) {
  // 이미 선택된 관광지인지 확인
  if (!isSpotSelected(recommendation)) {
    emit('toggle-spot', recommendation)
  }
}
</script>

<style scoped>
.search-panel {
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: white;
}

.panel-header {
  padding: 16px;
  border-bottom: 1px solid #e9ecef;
}

.panel-title {
  font-weight: 700;
  color: #333;
  position: relative;
  padding-left: 16px;
  margin: 0;
}

.panel-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 6px;
  height: 16px;
  width: 4px;
  background-color: #198754;
  border-radius: 2px;
}

.panel-body {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
}

.search-filters {
  margin-bottom: 16px;
}

.form-label {
  font-size: 0.85rem;
  font-weight: 600;
  color: #495057;
  margin-bottom: 4px;
}

.action-buttons {
  display: flex;
  gap: 8px;
}

/* 탭 스타일 */
.nav-tabs {
  border-bottom: 1px solid #dee2e6;
}

.nav-tabs .nav-link {
  font-size: 0.9rem;
  padding: 8px 12px;
  border: none;
  color: #495057;
}

.nav-tabs .nav-link.active {
  color: #198754;
  font-weight: 600;
  border-bottom: 2px solid #198754;
}

.tab-content {
  padding-top: 16px;
}

/* 검색 결과 스타일 */
.search-results,
.ai-recommendations {
  max-height: calc(100vh - 400px);
  overflow-y: auto;
  border: 1px solid #e9ecef;
  border-radius: 4px;
}

.no-results,
.no-search,
.no-recommendations {
  border: 1px dashed #dee2e6;
  border-radius: 4px;
}

/* AI 추천 관련 스타일 */
.ai-recommendation-item {
  padding: 16px;
  border-bottom: 1px solid #e9ecef;
  transition: background-color 0.2s;
}

.ai-recommendation-item:last-child {
  border-bottom: none;
}

.ai-recommendation-item:hover {
  background-color: #f8f9fa;
}

.recommendation-header {
  margin-bottom: 8px;
}

.recommendation-title {
  font-weight: 600;
  margin-bottom: 4px;
  color: #333;
}

.recommendation-address {
  display: block;
  font-size: 0.85rem;
  color: #6c757d;
  margin-bottom: 8px;
}

.recommendation-reason {
  font-size: 0.9rem;
  color: #495057;
  margin-bottom: 12px;
  line-height: 1.5;
}

.reason-label {
  font-weight: 600;
  color: #198754;
}

.ai-loading {
  background-color: #f8f9fa;
  border-radius: 4px;
  padding: 16px;
}

/* 반응형 디자인 */
@media (max-width: 1200px) {
  .search-results,
  .ai-recommendations {
    max-height: 300px;
  }
}

@media (max-width: 768px) {
  .panel-body {
    padding: 12px;
  }

  .action-buttons button {
    font-size: 0.8rem;
  }
}
</style>
