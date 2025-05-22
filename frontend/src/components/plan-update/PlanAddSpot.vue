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
            <label for="province" class="filter-label">시/도</label>
            <select
              id="province"
              class="filter-select"
              v-model="searchFilters.province"
              @change="onProvinceChange"
            >
              <option value="">전체</option>
              <option v-for="province in provinces" :key="province" :value="province">
                {{ province }}
              </option>
            </select>
          </div>

          <div class="filter-group">
            <label for="city" class="filter-label">구/군</label>
            <select
              id="city"
              class="filter-select"
              v-model="searchFilters.city"
              :disabled="!searchFilters.province"
            >
              <option value="">전체</option>
              <option v-for="city in filteredCities" :key="city" :value="city">
                {{ city }}
              </option>
            </select>
          </div>

          <div class="filter-group">
            <label for="category" class="filter-label">관광지 유형</label>
            <select id="category" class="filter-select" v-model="searchFilters.category">
              <option value="">전체</option>
              <option value="관광">관광지</option>
              <option value="맛집">맛집</option>
              <option value="쇼핑">쇼핑</option>
              <option value="숙소">숙소</option>
              <option value="교통">교통</option>
              <option value="기타">기타</option>
            </select>
          </div>
        </div>

        <!-- 검색어 입력 및 버튼 -->
        <div class="search-input-container">
          <input
            type="text"
            class="search-input"
            v-model="searchKeyword"
            placeholder="관광지 이름을 입력하세요"
            @keyup.enter="searchSpots"
          />
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
            <div class="result-category">{{ spot.category }}</div>
            <div class="result-content">
              <h4 class="result-name">{{ spot.name }}</h4>
              <p class="result-address">{{ spot.address }}</p>
              <p class="result-description" v-if="spot.description">{{ spot.description }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 선택된 장소 상세 정보 -->
      <div class="selected-spot-details" v-if="selectedSpot">
        <h4 class="details-title">선택한 장소 정보</h4>

        <div class="form-row">
          <div class="form-group">
            <label for="spot-visit-date" class="form-label">방문일</label>
            <input
              type="date"
              id="spot-visit-date"
              class="form-input"
              v-model="spotData.visitDate"
            />
          </div>

          <div class="form-group">
            <label for="spot-visit-time" class="form-label">방문 시간</label>
            <input
              type="time"
              id="spot-visit-time"
              class="form-input"
              v-model="spotData.visitTime"
            />
          </div>

          <div class="form-group">
            <label for="spot-duration" class="form-label">소요 시간 (분)</label>
            <input
              type="number"
              id="spot-duration"
              class="form-input"
              v-model.number="spotData.duration"
              min="0"
              placeholder="0"
            />
          </div>
        </div>

        <div class="form-group">
          <label for="spot-notes" class="form-label">메모</label>
          <textarea
            id="spot-notes"
            class="form-textarea"
            v-model="spotData.notes"
            placeholder="이 장소에 대한 메모를 입력하세요"
            rows="2"
          ></textarea>
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
import { ref, reactive, computed, watch } from 'vue'

const emit = defineEmits(['add-spot'])

// 상태 관리
const isAdding = ref(false)
const isSearching = ref(false)
const hasSearched = ref(false)
const searchKeyword = ref('')
const searchResults = ref([])
const selectedSpot = ref(null)

// 검색 필터
const searchFilters = reactive({
  province: '',
  city: '',
  category: '',
})

// 스팟 데이터
const spotData = reactive({
  visitDate: '',
  visitTime: '',
  duration: null,
  notes: '',
})

// 시도 목록 (실제로는 API에서 가져올 수 있음)
const provinces = [
  '서울',
  '부산',
  '대구',
  '인천',
  '광주',
  '대전',
  '울산',
  '세종',
  '경기',
  '강원',
  '충북',
  '충남',
  '전북',
  '전남',
  '경북',
  '경남',
  '제주',
]

// 시군구 데이터 (실제로는 API에서 가져올 수 있음)
const citiesByProvince = {
  서울: [
    '종로구',
    '중구',
    '용산구',
    '성동구',
    '광진구',
    '동대문구',
    '중랑구',
    '성북구',
    '강북구',
    '도봉구',
    '노원구',
    '은평구',
    '서대문구',
    '마포구',
    '양천구',
    '강서구',
    '구로구',
    '금천구',
    '영등포구',
    '동작구',
    '관악구',
    '서초구',
    '강남구',
    '송파구',
    '강동구',
  ],
  부산: [
    '중구',
    '서구',
    '동구',
    '영도구',
    '부산진구',
    '동래구',
    '남구',
    '북구',
    '해운대구',
    '사하구',
    '금정구',
    '강서구',
    '연제구',
    '수영구',
    '사상구',
    '기장군',
  ],
  대구: ['중구', '동구', '서구', '남구', '북구', '수성구', '달서구', '달성군'],
  인천: [
    '중구',
    '동구',
    '미추홀구',
    '연수구',
    '남동구',
    '부평구',
    '계양구',
    '서구',
    '강화군',
    '옹진군',
  ],
  광주: ['동구', '서구', '남구', '북구', '광산구'],
  대전: ['동구', '중구', '서구', '유성구', '대덕구'],
  울산: ['중구', '남구', '동구', '북구', '울주군'],
  세종: ['세종시'],
  경기: [
    '수원시',
    '고양시',
    '용인시',
    '성남시',
    '부천시',
    '안산시',
    '화성시',
    '남양주시',
    '안양시',
    '평택시',
    '시흥시',
    '파주시',
    '의정부시',
    '김포시',
    '광주시',
    '광명시',
    '군포시',
    '하남시',
    '오산시',
    '양주시',
    '이천시',
    '구리시',
    '안성시',
    '포천시',
    '의왕시',
    '여주시',
    '양평군',
    '동두천시',
    '가평군',
    '과천시',
    '연천군',
  ],
  강원: [
    '춘천시',
    '원주시',
    '강릉시',
    '동해시',
    '태백시',
    '속초시',
    '삼척시',
    '홍천군',
    '횡성군',
    '영월군',
    '평창군',
    '정선군',
    '철원군',
    '화천군',
    '양구군',
    '인제군',
    '고성군',
    '양양군',
  ],
  충북: [
    '청주시',
    '충주시',
    '제천시',
    '보은군',
    '옥천군',
    '영동군',
    '증평군',
    '진천군',
    '괴산군',
    '음성군',
    '단양군',
  ],
  충남: [
    '천안시',
    '공주시',
    '보령시',
    '아산시',
    '서산시',
    '논산시',
    '계룡시',
    '당진시',
    '금산군',
    '부여군',
    '서천군',
    '청양군',
    '홍성군',
    '예산군',
    '태안군',
  ],
  전북: [
    '전주시',
    '군산시',
    '익산시',
    '정읍시',
    '남원시',
    '김제시',
    '완주군',
    '진안군',
    '무주군',
    '장수군',
    '임실군',
    '순창군',
    '고창군',
    '부안군',
  ],
  전남: [
    '목포시',
    '여수시',
    '순천시',
    '나주시',
    '광양시',
    '담양군',
    '곡성군',
    '구례군',
    '고흥군',
    '보성군',
    '화순군',
    '장흥군',
    '강진군',
    '해남군',
    '영암군',
    '무안군',
    '함평군',
    '영광군',
    '장성군',
    '완도군',
    '진도군',
    '신안군',
  ],
  경북: [
    '포항시',
    '경주시',
    '김천시',
    '안동시',
    '구미시',
    '영주시',
    '영천시',
    '상주시',
    '문경시',
    '경산시',
    '군위군',
    '의성군',
    '청송군',
    '영양군',
    '영덕군',
    '청도군',
    '고령군',
    '성주군',
    '칠곡군',
    '예천군',
    '봉화군',
    '울진군',
    '울릉군',
  ],
  경남: [
    '창원시',
    '진주시',
    '통영시',
    '사천시',
    '김해시',
    '밀양시',
    '거제시',
    '양산시',
    '의령군',
    '함안군',
    '창녕군',
    '고성군',
    '남해군',
    '하동군',
    '산청군',
    '함양군',
    '거창군',
    '합천군',
  ],
  제주: ['제주시', '서귀포시'],
}

// 필터링된 시군구 목록
const filteredCities = computed(() => {
  if (!searchFilters.province) return []
  return citiesByProvince[searchFilters.province] || []
})

// 시도 변경 시 시군구 초기화
function onProvinceChange() {
  searchFilters.city = ''
}

// 추가 모드 시작
function startAdding() {
  isAdding.value = true

  // 오늘 날짜를 기본값으로 설정
  const today = new Date()
  const year = today.getFullYear()
  const month = String(today.getMonth() + 1).padStart(2, '0')
  const day = String(today.getDate()).padStart(2, '0')

  spotData.visitDate = `${year}-${month}-${day}`
}

// 추가 취소
function cancelAdding() {
  isAdding.value = false
  resetForm()
}

// 관광지 검색
async function searchSpots() {
  if (!searchKeyword.value.trim() && !searchFilters.province && !searchFilters.category) {
    alert('검색어 또는 필터를 입력해주세요.')
    return
  }

  isSearching.value = true
  hasSearched.value = true

  try {
    // 실제 구현: API 호출로 검색
    await new Promise((resolve) => setTimeout(resolve, 1000))

    // 더미 검색 결과
    const dummyResults = generateDummyResults()
    searchResults.value = dummyResults

    // 검색 결과가 있으면 첫 번째 항목 선택
    if (dummyResults.length > 0) {
      selectSpot(dummyResults[0])
    } else {
      selectedSpot.value = null
    }
  } catch (error) {
    console.error('검색 실패:', error)
    alert('검색 중 오류가 발생했습니다.')
    searchResults.value = []
  } finally {
    isSearching.value = false
  }
}

// 더미 검색 결과 생성 (실제로는 API에서 가져옴)
function generateDummyResults() {
  // 검색어가 없으면 빈 배열 반환
  if (!searchKeyword.value.trim() && !searchFilters.province) return []

  // 검색어 또는 필터에 따라 결과 필터링
  let results = [
    {
      id: 'place-1',
      name: '경복궁',
      category: '관광',
      address: '서울 종로구 사직로 161',
      description: '조선시대 대표적인 궁궐',
      location: { lat: 37.579617, lng: 126.977041 },
    },
    {
      id: 'place-2',
      name: '광화문 광장',
      category: '관광',
      address: '서울 종로구 세종로',
      description: '서울의 중심 광장',
      location: { lat: 37.572976, lng: 126.976757 },
    },
    {
      id: 'place-3',
      name: '명동 쇼핑거리',
      category: '쇼핑',
      address: '서울 중구 명동길',
      description: '쇼핑과 맛집의 중심지',
      location: { lat: 37.563576, lng: 126.983431 },
    },
    {
      id: 'place-4',
      name: '남산서울타워',
      category: '관광',
      address: '서울 용산구 남산공원길 105',
      description: '서울의 랜드마크',
      location: { lat: 37.551168, lng: 126.988228 },
    },
    {
      id: 'place-5',
      name: '이태원 거리',
      category: '맛집',
      address: '서울 용산구 이태원로',
      description: '다양한 문화와 맛집이 있는 거리',
      location: { lat: 37.534511, lng: 126.994034 },
    },
    {
      id: 'place-6',
      name: '해운대 해수욕장',
      category: '관광',
      address: '부산 해운대구 해운대해변로 264',
      description: '부산의 대표적인 해변',
      location: { lat: 35.158795, lng: 129.160728 },
    },
    {
      id: 'place-7',
      name: '제주 성산일출봉',
      category: '관광',
      address: '제주 서귀포시 성산읍 성산리',
      description: '유네스코 세계자연유산',
      location: { lat: 33.458031, lng: 126.94252 },
    },
  ]

  // 시도 필터링
  if (searchFilters.province) {
    results = results.filter((spot) => spot.address.includes(searchFilters.province))

    // 시군구 필터링
    if (searchFilters.city) {
      results = results.filter((spot) => spot.address.includes(searchFilters.city))
    }
  }

  // 카테고리 필터링
  if (searchFilters.category) {
    results = results.filter((spot) => spot.category === searchFilters.category)
  }

  // 검색어 필터링
  if (searchKeyword.value.trim()) {
    const keyword = searchKeyword.value.trim().toLowerCase()
    results = results.filter(
      (spot) =>
        spot.name.toLowerCase().includes(keyword) ||
        spot.address.toLowerCase().includes(keyword) ||
        (spot.description && spot.description.toLowerCase().includes(keyword)),
    )
  }

  return results
}

// 장소 선택
function selectSpot(spot) {
  selectedSpot.value = spot

  // 기본 메모 설정
  spotData.notes = spot.description || ''
}

// 장소 추가
function addSpot() {
  if (!selectedSpot.value) return

  const newSpot = {
    name: selectedSpot.value.name,
    address: selectedSpot.value.address,
    category: selectedSpot.value.category,
    description: spotData.notes || selectedSpot.value.description || '',
    visitDate: spotData.visitDate,
    visitTime: spotData.visitTime,
    duration: spotData.duration,
    location: selectedSpot.value.location,
  }

  emit('add-spot', newSpot)

  isAdding.value = false
  resetForm()
}

// 폼 초기화
function resetForm() {
  searchKeyword.value = ''
  searchFilters.province = ''
  searchFilters.city = ''
  searchFilters.category = ''
  searchResults.value = []
  selectedSpot.value = null
  hasSearched.value = false

  spotData.visitDate = ''
  spotData.visitTime = ''
  spotData.duration = null
  spotData.notes = ''
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
  opacity: 0.5;
  cursor: pointer;
}

.close-button:hover {
  opacity: 0.75;
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

.search-input-container {
  display: flex;
  gap: 8px;
}

.search-input {
  flex: 1;
  padding: 10px 12px;
  border: 1px solid #ced4da;
  border-radius: 4px;
  font-size: 0.95rem;
  transition:
    border-color 0.2s,
    box-shadow 0.2s;
}

.search-input:focus {
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
  opacity: 0.6;
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

/* 선택된 장소 상세 정보 */
.selected-spot-details {
  padding: 16px 24px;
  border-bottom: 1px solid #e9ecef;
}

.details-title {
  font-size: 1rem;
  font-weight: 600;
  margin: 0 0 16px;
}

/* 폼 스타일 */
.form-row {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
}

.form-group {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-label {
  font-size: 0.85rem;
  font-weight: 500;
  color: #495057;
}

.form-input,
.form-textarea {
  padding: 8px 12px;
  border: 1px solid #ced4da;
  border-radius: 4px;
  font-size: 0.9rem;
  transition:
    border-color 0.2s,
    box-shadow 0.2s;
}

.form-input:focus,
.form-textarea:focus {
  outline: none;
  border-color: #4a6bdf;
  box-shadow: 0 0 0 2px rgba(74, 107, 223, 0.25);
}

.form-textarea {
  resize: vertical;
  min-height: 60px;
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
  opacity: 0.6;
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
