<template>
  <div class="plan-add-spot">
    <button v-if="!isAdding" class="add-spot-button" @click="startAdding">
      <span class="icon-plus"></span>
      장소 추가
    </button>

    <div v-else class="add-spot-form">
      <div class="form-header">
        <h3 class="form-title">관광지 검색 및 추가</h3>
        <button class="close-button" @click="cancelAdding">×</button>
      </div>

      <div class="search-container">
        <!-- 검색 필터 -->
        <div class="search-filters">
          <div class="filter-group">
            <label for="sido" class="filter-label">시/도</label>
            <select
              id="sido"
              class="filter-select"
              v-model="searchFilters.sido"
              @change="onProvinceChange"
            >
              <option value="">전체</option>
              <option v-for="sido in sidos" :key="sido.sidoCode" :value="sido.sidoCode">
                {{ sido.sidoName }}
              </option>
            </select>
          </div>

          <div class="filter-group">
            <label for="gugun" class="filter-label">구/군</label>
            <select
              id="gugun"
              class="filter-select"
              v-model="searchFilters.guguns"
              :disabled="!searchFilters.sido"
            >
              <option value="">전체</option>
              <option v-for="gugun in guguns" :key="gugun.gugunCode" :value="gugun.gugunCode">
                {{ gugun.gugunName }}
              </option>
            </select>
          </div>

          <div class="filter-group">
            <label for="category" class="filter-label">관광지 유형</label>
            <select id="category" class="filter-select" v-model="searchFilters.contentType">
              <option value="">전체</option>
              <option
                v-for="type in contentTypes"
                :key="type.contentTypeId"
                :value="type.contentTypeId"
              >
                {{ type.contentTypeName }}
              </option>
            </select>
          </div>


          <div class="filter-group">
            <label for="category" class="filter-label">관광지 이름</label>
            <input type="search" class="filter-select" placeholder="검색어를 입력하세요" v-model="searchQuery" v-on:keyup.enter="searchSpots">
          </div>

          <button class="search-button" @click="searchSpots" :disabled="isSearching">
            <span class="icon-search"></span>
            {{ isSearching ? '검색 중...' : '검색' }}
          </button>
        </div>
      </div>

      <!-- 검색 결과 -->
      <div class="search-results" v-if="hasSearched">
        <div v-if="isSearching" class="search-loading">
          <div class="loading-spinner"></div>
          <p>검색 중입니다...</p>
        </div>

        <div v-else-if="searchResults.length === 0" class="search-empty">
          <span class="icon-empty"></span>
          <p>검색 결과가 없습니다.</p>
          <p class="empty-suggestion">다른 검색어나 필터를 사용해보세요.</p>
        </div>

        <div v-else class="results-list">
          <div
            v-for="spot in searchResults"
            :key="spot.id"
            class="result-item"
            :class="{ selected: selectedSpot && selectedSpot.id === spot.id }"
            @click="selectSpot(spot)"
          >
            <div class="waypoint-thumb">
              <img
                :src="
                  spot.firstImage1 ||
                  'https://img.freepik.com/premium-vector/no-photo-available-vector-icon-default-image-symbol-picture-coming-soon-web-site-mobile-app_87543-18055.jpg'
                "
                loading="lazy"
              />
            </div>
            <div class="result-category">{{ spot.contentTypeName }}</div>
            <div class="result-content">
              <h4 class="result-name">{{ spot.title }}</h4>
              <p class="result-address">{{ spot.addr1 }}</p>
              <p class="result-description">{{ spot.overview }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 액션 버튼 -->
      <div class="form-actions">
        <button class="button button-secondary" @click="cancelAdding">취소</button>
        <button class="button button-primary" @click="addSpot" :disabled="!selectedSpot">
          장소 추가
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import axios from 'axios'
import { useAuthStore } from '@/stores/authStore.js'
import api from '@/api/axios.js'

const emit = defineEmits(['add-spot'])

// 상태 관리
const serverUrl = import.meta.env.VITE_API_SERVER_URL
const authStore = useAuthStore()
const contentTypes = ref(null)
const sidos = ref(null)
const guguns = ref(null)
const isAdding = ref(false)
const isSearching = ref(false)
const hasSearched = ref(false)
const searchKeyword = ref('')
const searchResults = ref([])
const selectedSpot = ref(null)
const searchQuery = ref('')

// 검색 필터
const searchFilters = reactive({
  contentType: '',
  sido: '',
  guguns: '',
})

onMounted(async () => {
  console.log('PlanDetailPage - onMounted')
  await loadFilterData()
})

watch(
  () => searchFilters.sido,
  async (newValue, oldValue) => {
    if (isAdding.value) {
      await loadGugunData(searchFilters.sido)
    }
  },
)

async function loadFilterData() {
  try {
    const contentTypesResponse = await api.get(`${serverUrl}/api/v1/attractions/content-types`)
    const sidosResponse = await api.get(`${serverUrl}/api/v1/attractions/sidos`)

    contentTypes.value = contentTypesResponse.data
    sidos.value = sidosResponse.data
    console.log(contentTypes.value)
    console.log(sidos.value)
  } catch (error) {
    console.error(`데이터 로드 실패: ${error}`)
    alert('필터 정보를 불러오는 중 오류가 발생했습니다.')
  }
}

async function loadGugunData(sidoName) {
  try {
    const gugunResponse = await axios.get(
      `${serverUrl}/api/v1/attractions/guguns?sidoCode=${searchFilters.sido}`,
      {
        headers: {
          Authorization: `Bearer ${authStore.accessToken}`,
        },
      },
    )
    guguns.value = gugunResponse.data
  } catch (error) {
    console.error(`데이터 로드 실패: ${error}`)
    alert('구군 정보를 불러오는 중 오류가 발생했습니다.')
  }
}

// 시도 변경 시 시군구 초기화
function onProvinceChange() {
  searchFilters.guguns = ''
}

// 추가 모드 시작
function startAdding() {
  isAdding.value = true
}

// 추가 취소
function cancelAdding() {
  isAdding.value = false
  resetForm()
}

// 관광지 검색
async function searchSpots() {
  if (!searchKeyword.value.trim() && !searchFilters.sido && !searchFilters.contentType) {
    alert('검색어 또는 필터를 입력해주세요.')
    return
  }

  isSearching.value = true
  hasSearched.value = true

  try {
    const params = {}
    if (searchFilters.contentType !== null && searchFilters.contentType !== undefined)
      params.contentTypeId = searchFilters.contentType
    if (searchFilters.sido !== null && searchFilters.sido !== undefined)
      params.areaCode = searchFilters.sido
    if (searchFilters.guguns !== null && searchFilters.guguns !== undefined)
      params.siGunGuCode = searchFilters.guguns
    if (searchQuery.value !== null && searchQuery.value !== undefined) {
      params.query = searchQuery.value
    }
    params.page = 1
    params.pageSize = 10000
    console.log(params)
    const attractionResponse = await axios.get(`${serverUrl}/api/v1/attractions`, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`,
      },
      params,
    })

    searchResults.value = attractionResponse.data.content
    console.log(attractionResponse.data.content)
  } catch (error) {
    console.error('검색 실패:', error)
    alert('검색 중 오류가 발생했습니다.')
    searchResults.value = []
  } finally {
    isSearching.value = false
  }
}

// 장소 선택
function selectSpot(spot) {
  selectedSpot.value = spot
}

// 장소 추가
function addSpot() {
  if (!selectedSpot.value) return

  const newSpot = {
    id: { id: selectedSpot.value.id },
    title: selectedSpot.value.title,
    addr1: selectedSpot.value.addr1,
    firstImage1: selectedSpot.value.firstImage1,
    overview: selectedSpot.value.overview,
    latitude: selectedSpot.value.latitude,
    longitude: selectedSpot.value.longitude,
    contentTypeName: selectedSpot.value.contentTypeName,
  }

  emit('add-waypoint', newSpot)
}

// 폼 초기화
function resetForm() {
  searchKeyword.value = ''
  searchFilters.sido = ''
  searchFilters.guguns = ''
  searchFilters.contentType = ''
  searchResults.value = []
  selectedSpot.value = null
  hasSearched.value = false
}
</script>

<style scoped>
/* 장소 추가 버튼 */
.plan-add-spot {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  margin-bottom: 24px;
}

.add-spot-button {
  width: 100%;
  padding: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  background-color: #f8f9fa;
  border: 1px dashed #ced4da;
  border-radius: 8px;
  color: #4a6bdf;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.add-spot-button:hover {
  background-color: #e9ecef;
}

/* 장소 추가 폼 */
.add-spot-form {
  padding: 0;
}

.form-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-bottom: 1px solid #e9ecef;
}

.form-title {
  font-size: 1.1rem;
  font-weight: 600;
  margin: 0;
}

.close-button {
  background: transparent;
  border: none;
  font-size: 1.5rem;
  font-weight: 700;
  line-height: 1;
  color: #000;
  opagugun: 0.5;
  cursor: pointer;
}

.close-button:hover {
  opagugun: 0.75;
}

/* 검색 영역 */
.search-container {
  padding: 16px 24px;
  border-bottom: 1px solid #e9ecef;
}

.search-filters {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
}

.filter-group {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.filter-label {
  font-size: 0.85rem;
  font-weight: 500;
  color: #495057;
}

.filter-select {
  padding: 8px 12px;
  border: 1px solid #ced4da;
  border-radius: 4px;
  font-size: 0.9rem;
  transition:
    border-color 0.2s,
    box-shadow 0.2s;
}

.filter-select:focus {
  outline: none;
  border-color: #4a6bdf;
  box-shadow: 0 0 0 2px rgba(74, 107, 223, 0.25);
}

.search-button {
  padding: 10px 16px;
  background-color: #4a6bdf;
  color: white;
  border: none;
  border-radius: 4px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
  display: flex;
  align-items: center;
  gap: 6px;
}

.search-button:hover:not(:disabled) {
  background-color: #3a5bc9;
}

.search-button:disabled {
  opagugun: 0.6;
  cursor: not-allowed;
}

/* 검색 결과 */
.search-results {
  max-height: 300px;
  overflow-y: auto;
  border-bottom: 1px solid #e9ecef;
}

.search-loading,
.search-empty {
  padding: 32px 24px;
  text-align: center;
  color: #6c757d;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #4a6bdf;
  border-radius: 50%;
  margin: 0 auto 16px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.empty-suggestion {
  font-size: 0.85rem;
  margin-top: 8px;
}

.results-list {
  padding: 8px 0;
}

.result-item {
  display: flex;
  padding: 12px 24px;
  cursor: pointer;
  transition: background-color 0.2s;
  position: relative;
}

.result-item:hover {
  background-color: #f8f9fa;
}

.result-item.selected {
  background-color: #e8f0fe;
}

.result-item.selected::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 4px;
  background-color: #4a6bdf;
}

.result-category {
  background-color: #e9ecef;
  color: #495057;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.8rem;
  height: fit-content;
  margin-right: 12px;
  white-space: nowrap;
}

.result-content {
  flex: 1;
}

.result-name {
  font-weight: 600;
  margin: 0 0 4px;
}

.result-address {
  font-size: 0.85rem;
  color: #6c757d;
  margin: 0 0 4px;
}

.result-description {
  font-size: 0.8rem;
  color: #6c757d;
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 24px;
}

/* 버튼 스타일 */
.button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 8px 16px;
  border-radius: 4px;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  border: none;
  outline: none;
}

.button:disabled {
  opagugun: 0.6;
  cursor: not-allowed;
}

.button-primary {
  background-color: #4a6bdf;
  color: white;
}

.button-primary:hover:not(:disabled) {
  background-color: #3a5bc9;
}

.button-secondary {
  background-color: #f0f0f0;
  color: #333;
}

.button-secondary:hover:not(:disabled) {
  background-color: #e0e0e0;
}

/* 아이콘 스타일 */
.icon-plus::before {
  content: '+';
  font-size: 1.2rem;
  margin-right: 4px;
}

.icon-search::before {
  content: '🔍';
}

.icon-empty::before {
  content: '📭';
  font-size: 2rem;
  display: block;
  margin-bottom: 8px;
}

/* 썸네일 */
.waypoint-thumb {
  width: 184px;
  height: 80px;
  flex-shrink: 0;
  margin-right: 16px;
  border-radius: 4px;
  overflow: hidden;
}

.waypoint-thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .search-filters,
  .form-row {
    flex-direction: column;
    gap: 12px;
  }

  .search-input-container {
    flex-direction: column;
  }
}
</style>
