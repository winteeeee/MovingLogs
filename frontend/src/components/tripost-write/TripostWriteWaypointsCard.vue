<template>
  <div class="waypoints-card">
    <h3>여행 경로 ({{ waypointList?.length }}개 장소)</h3>

    <div v-if="waypointList" class="waypoints-timeline">
      <div
        v-for="(waypoint, index) in waypointList"
        :key="waypoint.id"
        class="timeline-item"
      >
        <div class="timeline-marker">
          <div class="marker-number">{{ index + 1 }}</div>
          <div class="timeline-line" v-if="index < waypointList.length - 1"></div>
        </div>

        <div class="waypoint-card">
          <div class="waypoint-header">
            <div class="waypoint-name">{{ waypoint.title }}</div>
            <div class="waypoint-category">{{ waypoint.contentTypeName }}</div>
          </div>

          <div class="waypoint-details">
            <div class="waypoint-address">
              <span class="icon-location"></span>
              {{ waypoint.addr1 }}
            </div>
          </div>
        </div>
      </div>

    </div>
    <!-- 로딩 상태 -->
    <div v-else class="loading-container">
      <div class="loading-spinner"></div>
      <p>여행 계획을 불러오는 중...</p>
    </div>

    <hr />

    <div class="map-container">
      <div id="kakaoMap" ref="kakaoMapRef"></div>
      <div class="map-controls">
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, onMounted, ref, watch } from 'vue'

const props = defineProps({
  waypointList: {
    type: Object
  }
})


const kakaoMap = ref(null)
const markers = ref([])
const polyline = ref(null)
const kakaoMapRef = ref(null)


watch(
  () => props.waypointList,
  () => {
    if (kakaoMap.value) {
      displayWaypointOnMap()
    }
  },
  { deep: true },
)

onMounted(() => {
  if (kakaoMapRef.value) {
    loadKakaoMapScript()
  }
})


function loadKakaoMapScript() {
  if (window.kakao && window.kakao.maps) {
    initializeMap()
  } else {
    const script = document.createElement('script')
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=YOUR_KAKAO_MAP_API_KEY&libraries=services&autoload=false`
    script.onload = () => {
      window.kakao.maps.load(() => {
        initializeMap()
      })
    }
    document.head.appendChild(script)
  }
}

function initializeMap() {
  if (!kakaoMapRef.value) return

  const mapOptions = {
    center: new window.kakao.maps.LatLng(36.2, 127.9), // 한국 중심점
    level: 13,
  }

  kakaoMap.value = new window.kakao.maps.Map(kakaoMapRef.value, mapOptions)

  // 기존 경로가 있으면 표시
  if (props.waypointList && props.waypointList.length > 0) {
    displayWaypointOnMap()
  }
}

function updatePolyline() {
  // 기존 폴리라인 제거
  if (polyline.value) {
    polyline.value.setMap(null)
  }

  if (props.waypointList.length < 2) return

  // 경로 좌표 생성
  const path = props.waypointList.map(
    (waypoint) => new window.kakao.maps.LatLng(waypoint.latitude, waypoint.longitude),
  )

  // 폴리라인 생성
  polyline.value = new window.kakao.maps.Polyline({
    path: path,
    strokeWeight: 5,
    strokeColor: '#FF6B6B',
    strokeOpacity: 0.7,
    strokeStyle: 'solid',
  })

  polyline.value.setMap(kakaoMap.value)
}

function displayWaypointOnMap() {
  // 기존 마커와 폴리라인 제거
  clearMapOverlays()

  if (!props.waypointList || props.waypointList.length === 0) return

  // 마커 생성
  props.waypointList.forEach((waypoint) => {
    const position = new window.kakao.maps.LatLng(waypoint.latitude, waypoint.longitude)
    const marker = new window.kakao.maps.Marker({
      position: position,
      map: kakaoMap.value,
    })

    markers.value.push(marker)
  })

  updatePolyline()

  // 지도 범위 재설정
  const bounds = new window.kakao.maps.LatLngBounds()
  props.waypointList.forEach((waypoint) => {
    bounds.extend(new window.kakao.maps.LatLng(waypoint.latitude, waypoint.longitude))
  })
  kakaoMap.value.setBounds(bounds)
}

function clearMapOverlays() {
  // 마커 제거
  markers.value.forEach((marker) => {
    marker.setMap(null)
  })
  markers.value = []

  // 폴리라인 제거
  if (polyline.value) {
    polyline.value.setMap(null)
    polyline.value = null
  }
}


</script>

<style scoped>
.waypoints-card h3 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 15px;
  color: #333;
}

.map-container {
  height: 600px;
  margin-top: 20px;
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

#kakaoMap {
  width: 100%;
  height: 100%;
}

.map-controls {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 1;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.map-control-btn {
  padding: 8px 12px;
  background-color: white;
  border: 1px solid #ced4da;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.map-control-btn:hover {
  background-color: #f8f9fa;
}


/* 타임라인 스타일 */
.waypoints-timeline {
  position: relative;
}

.timeline-item {
  display: flex;
  margin-bottom: 24px;
  position: relative;
}

.timeline-item:last-child {
  margin-bottom: 0;
}

.timeline-marker {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-right: 20px;
  position: relative;
}

.marker-number {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #4a6bdf;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 0.9rem;
  z-index: 2;
}

.timeline-line {
  width: 2px;
  height: 60px;
  background-color: #e9ecef;
  margin-top: 8px;
}

.waypoint-card {
  flex: 1;
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 16px;
  border-left: 4px solid #4a6bdf;
}

.waypoint-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.waypoint-name {
  font-weight: 600;
  font-size: 1.1rem;
  color: #333;
}

.waypoint-category {
  background-color: #e9ecef;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.8rem;
  color: #495057;
}

.waypoint-details {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.waypoint-address {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #6c757d;
  font-size: 0.9rem;
}
</style>
