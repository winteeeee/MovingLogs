<template>
  <section class="map-section mb-4">
    <div class="card">
      <div class="card-body">
        <div class="d-flex justify-content-between align-items-center mb-3">
          <h5 class="section-title mb-0">여행 경로 지도</h5>
          <div class="map-controls">
            <button class="btn btn-outline-secondary btn-sm me-2" @click="resetMapView">
              <i class="bi bi-arrow-counterclockwise"></i> 지도 초기화
            </button>
            <button
              class="btn btn-outline-primary btn-sm"
              @click="optimizeRouteOrder"
              :disabled="selectedSpots.length < 2"
            >
              <i class="bi bi-shuffle"></i> 경로 최적화
            </button>
          </div>
        </div>

        <div class="map-container" ref="mapContainer"></div>

        <div class="route-info mt-3" v-if="selectedSpots.length > 0">
          <div class="d-flex align-items-center">
            <div class="route-stats me-3">
              <span class="route-stat-item">
                <i class="bi bi-geo-alt-fill"></i> {{ selectedSpots.length }}개 장소
              </span>
              <span class="route-stat-item" v-if="totalDistance > 0">
                <i class="bi bi-signpost-split-fill"></i> 총 거리:
                {{ (totalDistance / 1000).toFixed(1) }}km
              </span>
              <span class="route-stat-item" v-if="estimatedTime > 0">
                <i class="bi bi-clock-fill"></i> 예상 소요시간: {{ formatTime(estimatedTime) }}
              </span>
            </div>

            <div class="route-actions ms-auto">
              <button
                class="btn btn-sm btn-outline-danger"
                @click="clearSpots"
                :disabled="selectedSpots.length === 0"
              >
                <i class="bi bi-trash"></i> 전체 삭제
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue'
import { formatTime } from '../../utils/timeUtils.js'

const props = defineProps({
  selectedSpots: {
    type: Array,
    required: true,
  },
  searchResults: {
    type: Array,
    required: true,
  },
  totalDistance: {
    type: Number,
    default: 0,
  },
  estimatedTime: {
    type: Number,
    default: 0,
  },
})

const emit = defineEmits([
  'toggle-spot',
  'focus-spot',
  'reset-map',
  'optimize-route',
  'clear-spots',
])

// 지도 관련 참조
const mapContainer = ref(null)
const kakaoMap = ref(null)
const markers = ref([])
const polyline = ref(null)

// 지도 초기화
function initMap() {
  if (!mapContainer.value) return

  if (window.kakao && window.kakao.maps) {
    createMap()
  } else {
    // 카카오맵 API 로드
    const script = document.createElement('script')
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=YOUR_KAKAO_MAP_API_KEY&autoload=false`
    script.onload = () => {
      window.kakao.maps.load(() => {
        createMap()
      })
    }
    document.head.appendChild(script)
  }
}

// 지도 생성
function createMap() {
  const mapOptions = {
    center: new window.kakao.maps.LatLng(37.566826, 126.9786567), // 서울시청
    level: 8,
  }

  kakaoMap.value = new window.kakao.maps.Map(mapContainer.value, mapOptions)
}

// 지도 초기화 (중심점 및 줌 레벨 리셋)
function resetMapView() {
  if (kakaoMap.value) {
    kakaoMap.value.setCenter(new window.kakao.maps.LatLng(37.566826, 126.9786567))
    kakaoMap.value.setLevel(8)
  }

  emit('reset-map')
}

// 검색 결과를 지도에 표시
function displaySearchResults() {
  if (!kakaoMap.value) return

  // 기존 마커 제거
  clearMarkers()

  // 검색 결과가 없으면 리턴
  if (props.searchResults.length === 0) return

  // 검색 결과에 대한 마커 생성
  const bounds = new window.kakao.maps.LatLngBounds()

  props.searchResults.forEach((spot) => {
    const position = new window.kakao.maps.LatLng(spot.lat, spot.lng)

    // 마커 이미지 설정
    let markerImageSrc

    switch (spot.type) {
      case 'attraction':
        markerImageSrc = '//t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png'
        break
      case 'culture':
        markerImageSrc = '//t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_red.png'
        break
      default:
        markerImageSrc = '//t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_blue.png'
    }

    const markerImage = new window.kakao.maps.MarkerImage(
      markerImageSrc,
      new window.kakao.maps.Size(24, 35),
    )

    // 마커 생성
    const marker = new window.kakao.maps.Marker({
      position: position,
      map: kakaoMap.value,
      title: spot.name,
      image: markerImage,
    })

    // 마커 클릭 이벤트
    window.kakao.maps.event.addListener(marker, 'click', () => {
      emit('toggle-spot', spot)
    })

    // 인포윈도우 생성
    const infoContent = `
      <div class="marker-info">
        <strong>${spot.name}</strong>
        <p>${getSpotTypeName(spot.type)}</p>
      </div>
    `

    const infoWindow = new window.kakao.maps.InfoWindow({
      content: infoContent,
    })

    // 마커 마우스오버 이벤트
    window.kakao.maps.event.addListener(marker, 'mouseover', () => {
      infoWindow.open(kakaoMap.value, marker)
    })

    // 마커 마우스아웃 이벤트
    window.kakao.maps.event.addListener(marker, 'mouseout', () => {
      infoWindow.close()
    })

    // 마커 저장
    markers.value.push({
      marker: marker,
      infoWindow: infoWindow,
      spotId: spot.id,
    })

    // 지도 범위에 포함
    bounds.extend(position)
  })

  // 지도 범위 재설정
  if (!bounds.isEmpty()) {
    kakaoMap.value.setBounds(bounds)
  }
}

// 마커 제거
function clearMarkers() {
  markers.value.forEach((item) => {
    item.marker.setMap(null)
    item.infoWindow.close()
  })

  markers.value = []
}

// 경로를 지도에 표시
function updateRouteOnMap() {
  if (!kakaoMap.value || props.selectedSpots.length === 0) {
    // 경로 제거
    if (polyline.value) {
      polyline.value.setMap(null)
      polyline.value = null
    }

    return
  }

  // 경로 좌표 생성
  const path = props.selectedSpots.map((spot) => new window.kakao.maps.LatLng(spot.lat, spot.lng))

  // 기존 경로 제거
  if (polyline.value) {
    polyline.value.setMap(null)
  }

  // 새 경로 생성
  polyline.value = new window.kakao.maps.Polyline({
    path: path,
    strokeWeight: 4,
    strokeColor: '#FF6B6B',
    strokeOpacity: 0.7,
    strokeStyle: 'solid',
  })

  polyline.value.setMap(kakaoMap.value)

  // 마커 하이라이트
  highlightSelectedMarkers()

  // 지도 범위 재설정
  if (props.selectedSpots.length > 0) {
    const bounds = new window.kakao.maps.LatLngBounds()

    props.selectedSpots.forEach((spot) => {
      bounds.extend(new window.kakao.maps.LatLng(spot.lat, spot.lng))
    })

    kakaoMap.value.setBounds(bounds)
  }
}

// 선택된 마커 하이라이트
function highlightSelectedMarkers() {
  markers.value.forEach((item) => {
    const isSelected = props.selectedSpots.some((spot) => spot.id === item.spotId)

    if (isSelected) {
      // 선택된 마커는 z-index를 높여서 앞으로 표시
      item.marker.setZIndex(10)
    } else {
      item.marker.setZIndex(1)
    }
  })
}

// 특정 관광지로 지도 포커스
function focusOnSpot(spot) {
  if (!kakaoMap.value) return

  const position = new window.kakao.maps.LatLng(spot.lat, spot.lng)
  kakaoMap.value.setCenter(position)
  kakaoMap.value.setLevel(3)

  // 해당 마커 찾기
  const markerItem = markers.value.find((item) => item.spotId === spot.id)
  if (markerItem) {
    markerItem.infoWindow.open(kakaoMap.value, markerItem.marker)

    // 3초 후 인포윈도우 닫기
    setTimeout(() => {
      markerItem.infoWindow.close()
    }, 3000)
  }
}

// 경로 최적화
function optimizeRouteOrder() {
  emit('optimize-route')
}

// 선택된 관광지 모두 제거
function clearSpots() {
  emit('clear-spots')
}

// 관광지 타입 이름 반환
function getSpotTypeName(type) {
  const typeNames = {
    attraction: '관광지',
    culture: '문화시설',
    festival: '축제행사',
    course: '여행코스',
    leports: '레포츠',
    lodging: '숙박',
    shopping: '쇼핑',
    restaurant: '음식점',
  }

  return typeNames[type] || '기타'
}

// 라이프사이클 훅
onMounted(() => {
  initMap()
})

onBeforeUnmount(() => {
  // 리소스 정리
  clearMarkers()

  if (polyline.value) {
    polyline.value.setMap(null)
  }
})

// 선택된 관광지 변경 감시
watch(
  () => props.selectedSpots,
  () => {
    updateRouteOnMap()
  },
  { deep: true },
)

// 검색 결과 변경 감시
watch(
  () => props.searchResults,
  () => {
    displaySearchResults()
  },
  { deep: true },
)

// 외부에서 호출할 메서드 노출
defineExpose({
  displaySearchResults,
  updateRouteOnMap,
  focusOnSpot,
  resetMap: resetMapView,
})
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

.map-container {
  height: 500px;
  border-radius: 4px;
  overflow: hidden;
}

.route-stats {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.route-stat-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #495057;
}

@media (max-width: 767.98px) {
  .map-container {
    height: 350px;
  }

  .route-stats {
    flex-direction: column;
    gap: 8px;
  }
}
</style>
