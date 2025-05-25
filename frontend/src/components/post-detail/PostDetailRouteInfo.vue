<template>
  <div class="route-info" v-if="waypoints">
    <div class="route-header">
      <h3>여행 경로</h3>
      <button class="map-toggle-btn" @click="$emit('toggle-map')">
        <i :class="['bi', mapVisible ? 'bi-map-fill' : 'bi-map']"></i>
        {{ mapVisible ? '지도 닫기' : '지도 보기' }}
      </button>
    </div>

    <div class="route-path">

      <div
        v-for="(waypoint, index) in waypoints"
        :key="`waypoint-${index}`"
        class="route-point mb-2"
      >
        <div class="point-marker">
          <i class="point-marker-start bi bi-geo-alt-fill" v-if="index === 0"></i>
          <i class="point-marker-end bi bi-flag-fill" v-else-if="index === waypoints.length-1"></i>
          <i class="point-marker-middle bi bi-geo" v-else></i>
        </div>
        <div class="point-info">
          <span class="point-label me-2">{{ index + 1 }}</span>
          <span class="point-name">{{ waypoint.attractionName }}</span>
        </div>
      </div>

    </div>

    <div v-if="mapVisible" class="route-map-container">
      <div id="routeMap" ref="routeMapRef" class="route-map"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, nextTick, defineProps, defineEmits } from 'vue'

const props = defineProps({
  waypoints: {
    type: Object,
    required: true,
  },
  mapVisible: {
    type: Boolean,
    default: false,
  },
})

defineEmits(['toggle-map'])

const kakaoMap = ref(null)
const markers = ref([])
const polyline = ref(null)
const routeMapRef = ref(null)

watch(
  () => props.mapVisible,
  (newVal) => {
    if (newVal) {
      nextTick(() => {
        initializeMap()
      })
    }
  },
)

function initializeMap() {
  if (!routeMapRef.value) return

  if (window.kakao && window.kakao.maps) {
    createMap()
  } else {
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

function createMap() {
  const mapOptions = {
    center: new window.kakao.maps.LatLng(36.2, 127.9), // 한국 중심점
    level: 13,
  }

  kakaoMap.value = new window.kakao.maps.Map(routeMapRef.value, mapOptions)
  displayRouteOnMap()
}

function displayRouteOnMap() {
  // 기존 마커와 폴리라인 제거
  clearMapOverlays()

  if (!props.waypoints || props.waypoints.length === 0) return

  // 마커 생성
  props.waypoints.forEach((wp, index) => {
    const position = new window.kakao.maps.LatLng(wp.latitude, wp.longitude)

    // 마커 아이콘 설정
    let markerImage
    if (index === 0) {
      // 출발지 마커
      markerImage = new window.kakao.maps.MarkerImage(
        'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/red_b.png',
        new window.kakao.maps.Size(50, 45),
        { offset: new window.kakao.maps.Point(15, 43) },
      )
    } else if (index === props.waypoints.length - 1) {
      // 도착지 마커
      markerImage = new window.kakao.maps.MarkerImage(
        'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/blue_b.png',
        new window.kakao.maps.Size(50, 45),
        { offset: new window.kakao.maps.Point(15, 43) },
      )
    } else {
      // 경유지 마커
      markerImage = new window.kakao.maps.MarkerImage(
        'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png',
        new window.kakao.maps.Size(24, 35),
        { offset: new window.kakao.maps.Point(12, 35) },
      )
    }

    const marker = new window.kakao.maps.Marker({
      position: position,
      map: kakaoMap.value,
      image: markerImage,
    })

    markers.value.push(marker)

    // 마커 클릭 이벤트
    window.kakao.maps.event.addListener(marker, 'click', () => {
      const pointName = props.waypoints[index].attractionName;
      const infowindow = new window.kakao.maps.InfoWindow({
        content: `<div style="padding:5px; font-size:12px;">${pointName}</div>`,
      })

      infowindow.open(kakaoMap.value, marker)

      // 3초 후 인포윈도우 닫기
      setTimeout(() => {
        infowindow.close()
      }, 2000)
    })
  })

  // 경로 폴리라인 생성
  const path = props.waypoints.map(
    (wp) => new window.kakao.maps.LatLng(wp.latitude, wp.longitude)
  )

  polyline.value = new window.kakao.maps.Polyline({
    path: path,
    strokeWeight: 5,
    strokeColor: '#FF6B6B',
    strokeOpacity: 0.7,
    strokeStyle: 'solid',
  })

  polyline.value.setMap(kakaoMap.value)

  // 지도 범위 재설정
  const bounds = new window.kakao.maps.LatLngBounds()
  props.waypoints.forEach((wp) => {
    bounds.extend(new window.kakao.maps.LatLng(wp.latitude, wp.longitude))
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
.route-info {
  margin: 30px 0;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.route-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.route-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.map-toggle-btn {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 8px 12px;
  background-color: #fd7e14;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
}

.map-toggle-btn:hover {
  background-color: #e8590c;
}

.route-path {
  display: flex;
  flex-direction: column;
  gap: 8px;
  position: relative;
  padding-left: 10px;
}

.route-path::before {
  content: '';
  position: absolute;
  left: 6px;
  top: -5px;
  bottom: 2px;
  width: 38px;
  background-color: #efefef;
  z-index: 1;
  border-radius: 20px;
}

.route-point {
  display: flex;
  align-items: center;
  gap: 15px;
}

.point-marker {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.point-marker-start {
  color: #fd7e14;
}

.point-marker-middle {
  color: #20c997;
}

.point-marker-end {
  color: #339af0;
}

.point-info {
  flex: 1;
}

.point-label {
  font-size: 14px;
  font-weight: bold;
  color: #6c757d;
}

.point-name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.route-map-container {
  margin-top: 20px;
  height: 400px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.route-map {
  width: 100%;
  height: 100%;
}

@media (max-width: 768px) {
  .route-info {
    padding: 15px;
  }

  .point-name {
    font-size: 14px;
  }
}
</style>
