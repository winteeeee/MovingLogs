<template>
  <div class="planning-page">
    <div class="planning-container">
      <!-- 사이드바 영역 (검색 패널) -->
      <div class="sidebar">
        <PlanningSearchPanel
          :regions="regions"
          :districts="districts"
          :tourism-types="tourismTypes"
          :search-params="searchParams"
          :search-results="searchResults"
          :selected-spots="selectedSpots"
          :has-searched="hasSearched"
          @update:search-params="updateSearchParams"
          @search="searchTourismSpots"
          @toggle-spot="toggleSpotSelection"
          @fetch-districts="updateDistricts"
        />
      </div>

      <!-- 메인 콘텐츠 영역 (지도 + 여행 계획) -->
      <div class="main-content">
        <!-- 지도 영역 -->
        <div class="map-container">
          <PlanningMapView
            :selected-spots="selectedSpots"
            :search-results="searchResults"
            :total-distance="totalDistance"
            :estimated-time="estimatedTime"
            @toggle-spot="toggleSpotSelection"
            @focus-spot="focusOnSpot"
            @reset-map="resetMap"
            @optimize-route="optimizeRoute"
            @clear-spots="clearSelectedSpots"
            ref="mapViewRef"
          />
        </div>

        <!-- 여행 계획 영역 -->
        <div class="plan-container">
          <PlanningPlanInfo
            :planInfo="planInfo"
            :selected-spots="selectedSpots"
            :is-valid="isPlanInfoValid"
            @update:route-plan="updateRoutePlan"
            @save="savePlan"
            @cancel="cancelPlanning"
            @move-spot-up="moveSpotUp"
            @move-spot-down="moveSpotDown"
            @remove-spot="removeSpot"
            @focus-spot="focusOnSpot"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import PlanningSearchPanel from '@/components/planning/PlanningSearchPanel.vue'
import PlanningMapView from '@/components/planning/PlanningMapView.vue'
import PlanningPlanInfo from '@/components/planning/PlanningPlanInfo.vue'
import { getRegions, getDistricts, getTourismTypes, searchSpots } from '@/services/tourismService.js'
import { calculateDistance, optimizeRouteOrder } from '@/utils/routeUtils.js'

// 지도 컴포넌트 참조
const mapViewRef = ref(null)

// 검색 파라미터
const searchParams = reactive({
  region: '',
  district: '',
  type: '',
  keyword: '',
})

// 검색 결과
const searchResults = ref([])
const hasSearched = ref(false)

// 선택된 관광지
const selectedSpots = ref([])
const activeSpotId = ref(null)

// 경로 통계
const totalDistance = ref(0)
const estimatedTime = ref(0)

// 여행 계획 정보
const planInfo = reactive({
  title: '',
  startDate: '',
  endDate: '',
  description: '',
})

// 시도 목록
const regions = ref([
  { code: 'seoul', name: '서울시' },
  { code: 'gyeonggi', name: '경기도' },
  { code: 'incheon', name: '인천시' },
  { code: 'gangwon', name: '강원도' },
  { code: 'chungbuk', name: '충청북도' },
  { code: 'chungnam', name: '충청남도' },
  { code: 'daejeon', name: '대전시' },
  { code: 'sejong', name: '세종시' },
  { code: 'gyeongbuk', name: '경상북도' },
  { code: 'gyeongnam', name: '경상남도' },
  { code: 'daegu', name: '대구시' },
  { code: 'ulsan', name: '울산시' },
  { code: 'busan', name: '부산시' },
  { code: 'jeonbuk', name: '전라북도' },
  { code: 'jeonnam', name: '전라남도' },
  { code: 'gwangju', name: '광주시' },
  { code: 'jeju', name: '제주도' },
])

// 구군 목록
const districts = ref([])

// 관광지 타입 목록
const tourismTypes = ref([
  { code: 'attraction', name: '관광지' },
  { code: 'culture', name: '문화시설' },
  { code: 'festival', name: '축제공연행사' },
  { code: 'course', name: '여행코스' },
  { code: 'leports', name: '레포츠' },
  { code: 'lodging', name: '숙박' },
  { code: 'shopping', name: '쇼핑' },
  { code: 'restaurant', name: '음식점' },
])

// 계산된 속성
const isPlanInfoValid = computed(() => {
  return planInfo.title.trim() !== '' && selectedSpots.value.length >= 2
})

// 검색 파라미터 업데이트
function updateSearchParams(newParams) {
  Object.assign(searchParams, newParams)

  // 시도 변경 시 구군 목록 업데이트
  if ('region' in newParams) {
    updateDistricts(newParams.region)
  }
}

// 구군 목록 업데이트
function updateDistricts(regionCode) {
  searchParams.district = ''

  // 실제로는 API 호출을 통해 해당 시도의 구군 목록을 가져와야 함
  // 여기서는 예시 데이터만 제공
  if (regionCode === 'seoul') {
    districts.value = [
      { code: 'gangnam', name: '강남구', regionCode: 'seoul' },
      { code: 'gangdong', name: '강동구', regionCode: 'seoul' },
      { code: 'gangbuk', name: '강북구', regionCode: 'seoul' },
      { code: 'gangseo', name: '강서구', regionCode: 'seoul' },
      { code: 'gwanak', name: '관악구', regionCode: 'seoul' },
      { code: 'gwangjin', name: '광진구', regionCode: 'seoul' },
      { code: 'guro', name: '구로구', regionCode: 'seoul' },
      { code: 'geumcheon', name: '금천구', regionCode: 'seoul' },
      { code: 'nowon', name: '노원구', regionCode: 'seoul' },
      { code: 'dobong', name: '도봉구', regionCode: 'seoul' },
      { code: 'dongdaemun', name: '동대문구', regionCode: 'seoul' },
      { code: 'dongjak', name: '동작구', regionCode: 'seoul' },
      { code: 'mapo', name: '마포구', regionCode: 'seoul' },
      { code: 'seodaemun', name: '서대문구', regionCode: 'seoul' },
      { code: 'seocho', name: '서초구', regionCode: 'seoul' },
      { code: 'seongdong', name: '성동구', regionCode: 'seoul' },
      { code: 'seongbuk', name: '성북구', regionCode: 'seoul' },
      { code: 'songpa', name: '송파구', regionCode: 'seoul' },
      { code: 'yangcheon', name: '양천구', regionCode: 'seoul' },
      { code: 'yeongdeungpo', name: '영등포구', regionCode: 'seoul' },
      { code: 'yongsan', name: '용산구', regionCode: 'seoul' },
      { code: 'eunpyeong', name: '은평구', regionCode: 'seoul' },
      { code: 'jongno', name: '종로구', regionCode: 'seoul' },
      { code: 'jung', name: '중구', regionCode: 'seoul' },
      { code: 'jungnang', name: '중랑구', regionCode: 'seoul' },
    ]
  } else if (regionCode === 'busan') {
    districts.value = [
      { code: 'gangseo', name: '강서구', regionCode: 'busan' },
      { code: 'geumjeong', name: '금정구', regionCode: 'busan' },
      { code: 'gijang', name: '기장군', regionCode: 'busan' },
      { code: 'nam', name: '남구', regionCode: 'busan' },
      { code: 'dong', name: '동구', regionCode: 'busan' },
      { code: 'dongnae', name: '동래구', regionCode: 'busan' },
      { code: 'busanjin', name: '부산진구', regionCode: 'busan' },
      { code: 'buk', name: '북구', regionCode: 'busan' },
      { code: 'sasang', name: '사상구', regionCode: 'busan' },
      { code: 'saha', name: '사하구', regionCode: 'busan' },
      { code: 'seo', name: '서구', regionCode: 'busan' },
      { code: 'suyeong', name: '수영구', regionCode: 'busan' },
      { code: 'yeonje', name: '연제구', regionCode: 'busan' },
      { code: 'yeongdo', name: '영도구', regionCode: 'busan' },
      { code: 'jung', name: '중구', regionCode: 'busan' },
      { code: 'haeundae', name: '해운대구', regionCode: 'busan' },
    ]
  } else {
    districts.value = []
  }
}

// 관광지 검색
function searchTourismSpots() {
  hasSearched.value = true

  // 실제로는 API 호출을 통해 관광지 정보를 가져와야 함
  // 여기서는 예시 데이터만 제공
  const dummyResults = [
    {
      id: 1,
      name: '경복궁',
      type: 'attraction',
      address: '서울특별시 종로구 사직로 161',
      lat: 37.579617,
      lng: 126.977041,
      description: '조선 왕조의 법궁(法宮)으로, 1395년에 창건되었다.',
    },
    {
      id: 2,
      name: '국립중앙박물관',
      type: 'culture',
      address: '서울특별시 용산구 서빙고로 137',
      lat: 37.523281,
      lng: 126.980642,
      description: '한국의 역사와 예술을 소개하는 국립박물관이다.',
    },
    {
      id: 3,
      name: '남산서울타워',
      type: 'attraction',
      address: '서울특별시 용산구 남산공원길 105',
      lat: 37.551245,
      lng: 126.988222,
      description: '서울의 상징적인 타워로, 전망대에서 서울 시내를 한눈에 볼 수 있다.',
    },
    {
      id: 4,
      name: '롯데월드',
      type: 'leports',
      address: '서울특별시 송파구 올림픽로 240',
      lat: 37.511361,
      lng: 127.098306,
      description: '실내외 테마파크로, 다양한 놀이기구와 공연을 즐길 수 있다.',
    },
    {
      id: 5,
      name: '명동성당',
      type: 'culture',
      address: '서울특별시 중구 명동길 74',
      lat: 37.563545,
      lng: 126.987608,
      description: '서울대교구 주교좌 성당으로, 고딕 양식의 건축물이다.',
    },
    {
      id: 6,
      name: '서울숲',
      type: 'attraction',
      address: '서울특별시 성동구 뚝섬로 273',
      lat: 37.544167,
      lng: 127.037778,
      description: '도심 속 생태공원으로, 다양한 테마의 정원과 산책로가 있다.',
    },
    {
      id: 7,
      name: '한강공원',
      type: 'leports',
      address: '서울특별시 영등포구 여의동로 330',
      lat: 37.526389,
      lng: 126.934167,
      description: '한강변을 따라 조성된 공원으로, 다양한 레저 활동을 즐길 수 있다.',
    },
    {
      id: 8,
      name: '서울시립미술관',
      type: 'culture',
      address: '서울특별시 중구 덕수궁길 61',
      lat: 37.565833,
      lng: 126.975278,
      description: '현대 미술 작품을 전시하는 미술관으로, 다양한 기획전이 열린다.',
    },
  ]

  // 검색 조건에 따라 필터링
  let filteredResults = [...dummyResults]

  if (searchParams.type) {
    filteredResults = filteredResults.filter((spot) => spot.type === searchParams.type)
  }

  if (searchParams.keyword) {
    const keyword = searchParams.keyword.toLowerCase()
    filteredResults = filteredResults.filter(
      (spot) =>
        spot.name.toLowerCase().includes(keyword) || spot.address.toLowerCase().includes(keyword),
    )
  }

  searchResults.value = filteredResults

  // 지도에 검색 결과 표시
  if (mapViewRef.value) {
    mapViewRef.value.displaySearchResults()
  }
}

// 관광지 선택/해제
function toggleSpotSelection(spot) {
  const index = selectedSpots.value.findIndex((selected) => selected.id === spot.id)

  if (index !== -1) {
    // 이미 선택된 경우 제거
    selectedSpots.value.splice(index, 1)
  } else {
    // 선택되지 않은 경우 추가
    selectedSpots.value.push(spot)
  }

  // 지도에 경로 업데이트
  if (mapViewRef.value) {
    mapViewRef.value.updateRouteOnMap()
  }
}

// 선택된 관광지 제거
function removeSpot(index) {
  selectedSpots.value.splice(index, 1)

  // 지도에 경로 업데이트
  if (mapViewRef.value) {
    mapViewRef.value.updateRouteOnMap()
  }
}

// 선택된 관광지 위로 이동
function moveSpotUp(index) {
  if (index > 0) {
    const temp = selectedSpots.value[index]
    selectedSpots.value[index] = selectedSpots.value[index - 1]
    selectedSpots.value[index - 1] = temp

    // 지도에 경로 업데이트
    if (mapViewRef.value) {
      mapViewRef.value.updateRouteOnMap()
    }
  }
}

// 선택된 관광지 아래로 이동
function moveSpotDown(index) {
  if (index < selectedSpots.value.length - 1) {
    const temp = selectedSpots.value[index]
    selectedSpots.value[index] = selectedSpots.value[index + 1]
    selectedSpots.value[index + 1] = temp

    // 지도에 경로 업데이트
    if (mapViewRef.value) {
      mapViewRef.value.updateRouteOnMap()
    }
  }
}

// 선택된 관광지 모두 제거
function clearSelectedSpots() {
  selectedSpots.value = []

  // 지도에 경로 업데이트
  if (mapViewRef.value) {
    mapViewRef.value.updateRouteOnMap()
  }
}

// 지도 초기화
function resetMap() {
  if (mapViewRef.value) {
    mapViewRef.value.resetMap()
  }
}

// 특정 관광지로 지도 포커스
function focusOnSpot(spot) {
  activeSpotId.value = spot.id

  if (mapViewRef.value) {
    mapViewRef.value.focusOnSpot(spot)
  }
}

// 여행 계획 정보 업데이트
function updateRoutePlan(newPlan) {
  Object.assign(planInfo, newPlan)
}

// 여행 계획 저장
function savePlan() {
  if (!isPlanInfoValid.value) return

  // 여행 계획 데이터 구성
  const planData = {
    title: planInfo.title,
    startDate: planInfo.startDate,
    endDate: planInfo.endDate,
    description: planInfo.description,
    spots: selectedSpots.value.map((spot) => ({
      id: spot.id,
      name: spot.name,
      type: spot.type,
      lat: spot.lat,
      lng: spot.lng,
    })),
    totalDistance: totalDistance.value,
    estimatedTime: estimatedTime.value,
  }

  console.log('여행 계획 저장:', planData)

  // 실제로는 API를 통해 서버에 저장
  alert('여행 계획이 저장되었습니다.')
}

// 여행 계획 취소
function cancelPlanning() {
  if (confirm('작성 중인 여행 계획을 취소하시겠습니까?')) {
    // 페이지 이동 또는 초기화
    console.log('여행 계획 취소')
  }
}

// 경로 통계 업데이트
function updateWaypointStats() {
  if (selectedSpots.value.length < 2) {
    totalDistance.value = 0
    estimatedTime.value = 0
    return
  }

  let distance = 0

  for (let i = 0; i < selectedSpots.value.length - 1; i++) {
    const spot1 = selectedSpots.value[i]
    const spot2 = selectedSpots.value[i + 1]

    // 간단한 유클리드 거리 계산 (실제로는 API를 통해 실제 도로 거리를 가져와야 함)
    const dx = (spot1.lat - spot2.lat) * 111000 // 위도 1도 = 약 111km
    const dy = (spot1.lng - spot2.lng) * 88400 // 경도 1도 = 약 88.4km (서울 기준)

    distance += Math.sqrt(dx * dx + dy * dy)
  }

  totalDistance.value = distance

  // 예상 소요시간 계산 (평균 속도 60km/h 가정)
  estimatedTime.value = (distance / 1000 / 60) * 60 * 60 // 초 단위
}

// 선택된 관광지 변경 감시
watch(
  selectedSpots,
  () => {
    updateWaypointStats()
  },
  { deep: true },
)

// 라이프사이클 훅
onMounted(() => {
  // 초기 데이터 로드
  // 실제로는 API 호출을 통해 데이터를 가져옴
})
</script>

<style scoped>
.planning-page {
  min-height: 100vh;
  background-color: #f8f9fa;
  padding: 20px;
}

.planning-container {
  display: flex;
  gap: 20px;
  max-width: 1600px;
  margin: 0 auto;
  height: calc(100vh - 40px);
}

/* 사이드바 스타일 */
.sidebar {
  width: 380px;
  flex-shrink: 0;
  height: 100%;
  overflow-y: auto;
  border-radius: 12px;
  background-color: white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

/* 메인 콘텐츠 스타일 */
.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
  height: 100%;
  overflow: hidden;
}

.map-container {
  flex: 1;
  min-height: 60%;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  background-color: white;
}

.plan-container {
  height: 40%;
  min-height: 300px;
  border-radius: 12px;
  overflow: auto;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  background-color: white;
}

/* 반응형 디자인 */
@media (max-width: 1200px) {
  .planning-container {
    flex-direction: column;
    height: auto;
  }

  .sidebar {
    width: 100%;
    height: auto;
    max-height: 500px;
  }

  .main-content {
    height: auto;
  }

  .map-container {
    height: 500px;
  }
}

@media (max-width: 768px) {
  .planning-page {
    padding: 10px;
  }

  .map-container {
    height: 400px;
  }

  .plan-container {
    min-height: 250px;
  }
}
</style>
