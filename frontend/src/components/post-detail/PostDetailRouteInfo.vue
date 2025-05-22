<template>
  <div class="route-info" v-if="route">
    <div class="route-header">
      <h3>여행 경로</h3>
      <button class="map-toggle-btn" @click="$emit('toggle-map')">
        <i :class="['bi', mapVisible ? 'bi-map-fill' : 'bi-map']"></i>
        {{ mapVisible ? '지도 닫기' : '지도 보기' }}
      </button>
    </div>

    <div class="route-path">
      <div class="route-point departure">
        <div class="point-marker">
          <i class="bi bi-geo-alt-fill"></i>
        </div>
        <div class="point-info">
          <div class="point-label">출발</div>
          <div class="point-name">{{ route.departure }}</div>
        </div>
      </div>

      <div
        v-for="(waypoint, index) in route.waypoints"
        :key="`waypoint-${index}`"
        class="route-point waypoint"
      >
        <div class="point-marker">
          <i class="bi bi-geo"></i>
        </div>
        <div class="point-info">
          <div class="point-label">경유 {{ index + 1 }}</div>
          <div class="point-name">{{ waypoint }}</div>
        </div>
      </div>

      <div class="route-point destination">
        <div class="point-marker">
          <i class="bi bi-flag-fill"></i>
        </div>
        <div class="point-info">
          <div class="point-label">도착</div>
          <div class="point-name">{{ route.destination }}</div>
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
  route: {
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

  if (props.route.coordinates && props.route.coordinates.length > 0) {
    displayRouteOnMap()
  }
}

function displayRouteOnMap() {
  // 기존 마커와 폴리라인 제거
  clearMapOverlays()

  if (!props.route.coordinates || props.route.coordinates.length === 0) return

  // 마커 생성
  props.route.coordinates.forEach((coord, index) => {
    const position = new window.kakao.maps.LatLng(coord.lat, coord.lng)

    // 마커 아이콘 설정
    let markerImage
    if (index === 0) {
      // 출발지 마커
      markerImage = new window.kakao.maps.MarkerImage(
        'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/red_b.png',
        new window.kakao.maps.Size(50, 45),
        { offset: new window.kakao.maps.Point(15, 43) },
      )
    } else if (index === props.route.coordinates.length - 1) {
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
      let pointName
      if (index === 0) {
        pointName = props.route.departure
      } else if (index === props.route.coordinates.length - 1) {
        pointName = props.route.destination
      } else {
        pointName = props.route.waypoints[index - 1]
      }

      const infowindow = new window.kakao.maps.InfoWindow({
        content: `<div style="padding:5px;font-size:12px;">${pointName}</div>`,
      })

      infowindow.open(kakaoMap.value, marker)

      // 3초 후 인포윈도우 닫기
      setTimeout(() => {
        infowindow.close()
      }, 3000)
    })
  })

  // 경로 폴리라인 생성
  const path = props.route.coordinates.map(
    (coord) => new window.kakao.maps.LatLng(coord.lat, coord.lng),
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
  props.route.coordinates.forEach((coord) => {
    bounds.extend(new window.kakao.maps.LatLng(coord.lat, coord.lng))
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
  gap: 15px;
  position: relative;
  padding-left: 30px;
}

.route-path::before {
  content: '';
  position: absolute;
  left: 15px;
  top: 30px;
  bottom: 30px;
  width: 2px;
  background-color: #ced4da;
  z-index: 1;
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

.departure .point-marker {
  color: #fd7e14;
}

.waypoint .point-marker {
  color: #20c997;
}

.destination .point-marker {
  color: #339af0;
}

.point-info {
  flex: 1;
}

.point-label {
  font-size: 12px;
  color: #6c757d;
  margin-bottom: 3px;
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
