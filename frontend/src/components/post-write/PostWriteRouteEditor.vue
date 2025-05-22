<template>
  <div class="form-section">
    <h3>경로 정보</h3>
    <label class="include-route-toggle">
      <input type="checkbox" v-model="includeRouteLocal" @change="toggleIncludeRoute" /> 경로 포함
    </label>

    <div v-if="includeRouteLocal" class="route-input">
      <div class="form-group">
        <label for="departure">출발지</label>
        <input
          type="text"
          id="departure"
          v-model="routeData.departure"
          placeholder="출발지를 입력하세요"
          @input="emitRouteChange"
        />
      </div>
      <div class="waypoint-group">
        <label>경유지</label>
        <div v-for="(waypoint, index) in routeData.waypoints" :key="index" class="waypoint-item">
          <input
            type="text"
            v-model="routeData.waypoints[index]"
            placeholder="경유지를 입력하세요"
            @input="emitRouteChange"
          />
          <button @click="removeWaypoint(index)" class="remove-btn">
            <i class="bi bi-x"></i>
          </button>
        </div>
        <button @click="addWaypoint" class="add-waypoint-btn">
          <i class="bi bi-plus"></i> 경유지 추가
        </button>
      </div>
      <div class="form-group">
        <label for="destination">도착지</label>
        <input
          type="text"
          id="destination"
          v-model="routeData.destination"
          placeholder="도착지를 입력하세요"
          @input="emitRouteChange"
        />
      </div>
      <div class="map-container">
        <div id="kakaoMap" ref="kakaoMapRef"></div>
        <div class="map-controls">
          <button type="button" class="map-control-btn" @click="searchLocation">
            <i class="bi bi-search"></i> 위치 검색
          </button>
          <button type="button" class="map-control-btn" @click="resetRoute">
            <i class="bi bi-arrow-counterclockwise"></i> 경로 초기화
          </button>
        </div>
        <div class="map-instructions">
          <p>
            지도에서 우클릭하여 경로 포인트를 추가하세요. 드래그하여 포인트를 이동할 수 있습니다.
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted, nextTick, defineProps, defineEmits } from 'vue'

const props = defineProps({
  routeData: {
    type: Object,
    required: true,
  },
  includeRoute: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['update:include-route', 'route-change', 'update:coordinates'])

const includeRouteLocal = ref(props.includeRoute)
const kakaoMap = ref(null)
const markers = ref([])
const polyline = ref(null)
const kakaoMapRef = ref(null)

watch(
  () => props.includeRoute,
  (newVal) => {
    includeRouteLocal.value = newVal
  },
)

watch(
  () => includeRouteLocal.value,
  (newVal) => {
    if (newVal && kakaoMapRef.value && !kakaoMap.value) {
      nextTick(() => {
        initializeMap()
      })
    }
  },
)

watch(
  () => props.routeData.coordinates,
  () => {
    if (kakaoMap.value) {
      updatePolyline()
    }
  },
  { deep: true },
)

onMounted(() => {
  if (includeRouteLocal.value && kakaoMapRef.value) {
    loadKakaoMapScript()
  }
})

function toggleIncludeRoute() {
  emit('update:include-route', includeRouteLocal.value)
  if (includeRouteLocal.value && !kakaoMap.value) {
    nextTick(() => {
      loadKakaoMapScript()
    })
  }
}

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

  // 지도 우클릭 이벤트 등록
  window.kakao.maps.event.addListener(kakaoMap.value, 'rightclick', (mouseEvent) => {
    addRoutePoint(mouseEvent.latLng)
  })

  // 기존 경로가 있으면 표시
  if (props.routeData.coordinates && props.routeData.coordinates.length > 0) {
    displayRouteOnMap()
  }
}

function addRoutePoint(latLng) {
  const position = {
    lat: latLng.getLat(),
    lng: latLng.getLng(),
  }

  // 좌표 배열에 추가
  const newCoordinates = [...props.routeData.coordinates, position]
  emit('update:coordinates', newCoordinates)

  // 마커 생성
  const marker = new window.kakao.maps.Marker({
    position: latLng,
    map: kakaoMap.value,
    draggable: true,
  })

  markers.value.push(marker)

  // 마커 드래그 이벤트
  window.kakao.maps.event.addListener(marker, 'dragend', (mouseEvent) => {
    const idx = markers.value.indexOf(marker)
    if (idx > -1) {
      const newPosition = {
        lat: mouseEvent.latLng.getLat(),
        lng: mouseEvent.latLng.getLng(),
      }

      const updatedCoordinates = [...props.routeData.coordinates]
      updatedCoordinates[idx] = newPosition
      emit('update:coordinates', updatedCoordinates)

      updatePolyline()
    }
  })

  // 마커 우클릭 이벤트 (삭제)
  window.kakao.maps.event.addListener(marker, 'rightclick', () => {
    const idx = markers.value.indexOf(marker)
    if (idx > -1) {
      markers.value.splice(idx, 1)

      const updatedCoordinates = [...props.routeData.coordinates]
      updatedCoordinates.splice(idx, 1)
      emit('update:coordinates', updatedCoordinates)

      marker.setMap(null)
      updatePolyline()
    }
  })

  updatePolyline()

  // 첫 번째 포인트는 출발지, 마지막 포인트는 도착지로 설정
  if (newCoordinates.length === 1) {
    getAddressFromCoords(position, (address) => {
      props.routeData.departure = address
      emitRouteChange()
    })
  } else if (newCoordinates.length > 1) {
    const lastIdx = newCoordinates.length - 1
    getAddressFromCoords(newCoordinates[lastIdx], (address) => {
      props.routeData.destination = address
      emitRouteChange()
    })

    // 중간 포인트는 경유지로 설정
    if (newCoordinates.length > 2) {
      const waypointIdx = newCoordinates.length - 2
      getAddressFromCoords(newCoordinates[waypointIdx], (address) => {
        if (waypointIdx === 1) {
          props.routeData.waypoints = [address]
        } else {
          props.routeData.waypoints.push(address)
        }
        emitRouteChange()
      })
    }
  }
}

function updatePolyline() {
  // 기존 폴리라인 제거
  if (polyline.value) {
    polyline.value.setMap(null)
  }

  if (props.routeData.coordinates.length < 2) return

  // 경로 좌표 생성
  const path = props.routeData.coordinates.map(
    (coord) => new window.kakao.maps.LatLng(coord.lat, coord.lng),
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

function displayRouteOnMap() {
  // 기존 마커와 폴리라인 제거
  clearMapOverlays()

  if (!props.routeData.coordinates || props.routeData.coordinates.length === 0) return

  // 마커 생성
  props.routeData.coordinates.forEach((coord) => {
    const position = new window.kakao.maps.LatLng(coord.lat, coord.lng)
    const marker = new window.kakao.maps.Marker({
      position: position,
      map: kakaoMap.value,
      draggable: true,
    })

    markers.value.push(marker)

    // 마커 드래그 이벤트
    window.kakao.maps.event.addListener(marker, 'dragend', (mouseEvent) => {
      const idx = markers.value.indexOf(marker)
      if (idx > -1) {
        const newPosition = {
          lat: mouseEvent.latLng.getLat(),
          lng: mouseEvent.latLng.getLng(),
        }

        const updatedCoordinates = [...props.routeData.coordinates]
        updatedCoordinates[idx] = newPosition
        emit('update:coordinates', updatedCoordinates)

        updatePolyline()
      }
    })

    // 마커 우클릭 이벤트 (삭제)
    window.kakao.maps.event.addListener(marker, 'rightclick', () => {
      const idx = markers.value.indexOf(marker)
      if (idx > -1) {
        markers.value.splice(idx, 1)

        const updatedCoordinates = [...props.routeData.coordinates]
        updatedCoordinates.splice(idx, 1)
        emit('update:coordinates', updatedCoordinates)

        marker.setMap(null)
        updatePolyline()
      }
    })
  })

  updatePolyline()

  // 지도 범위 재설정
  const bounds = new window.kakao.maps.LatLngBounds()
  props.routeData.coordinates.forEach((coord) => {
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

function resetRoute() {
  clearMapOverlays()
  emit('update:coordinates', [])
  props.routeData.departure = ''
  props.routeData.destination = ''
  props.routeData.waypoints = ['']
  emitRouteChange()
}

function getAddressFromCoords(coords, callback) {
  if (!window.kakao || !window.kakao.maps) return

  const geocoder = new window.kakao.maps.services.Geocoder()

  geocoder.coord2Address(coords.lng, coords.lat, (result, status) => {
    if (status === window.kakao.maps.services.Status.OK) {
      if (result[0].address) {
        callback(result[0].address.region_1depth_name + ' ' + result[0].address.region_2depth_name)
      } else {
        callback('알 수 없는 위치')
      }
    } else {
      callback('알 수 없는 위치')
    }
  })
}

function searchLocation() {
  const searchTerm = prompt('검색할 위치를 입력하세요:')
  if (!searchTerm) return

  if (!window.kakao || !window.kakao.maps) return

  const places = new window.kakao.maps.services.Places()

  places.keywordSearch(searchTerm, (result, status) => {
    if (status === window.kakao.maps.services.Status.OK) {
      const firstResult = result[0]
      const position = new window.kakao.maps.LatLng(firstResult.y, firstResult.x)

      // 지도 중심 이동
      kakaoMap.value.setCenter(position)
      kakaoMap.value.setLevel(3)
    } else {
      alert('검색 결과가 없습니다.')
    }
  })
}

function addWaypoint() {
  props.routeData.waypoints.push('')
  emitRouteChange()
}

function removeWaypoint(index) {
  props.routeData.waypoints.splice(index, 1)
  if (props.routeData.waypoints.length === 0) {
    props.routeData.waypoints.push('')
  }
  emitRouteChange()
}

function emitRouteChange() {
  emit('route-change', props.routeData)
}
</script>

<style scoped>
.form-section {
  margin-bottom: 30px;
  border-top: 1px solid #e9ecef;
  padding-top: 20px;
}

.form-section h3 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 15px;
  color: #333;
}

.include-route-toggle {
  display: flex;
  align-items: center;
  cursor: pointer;
  margin-bottom: 15px;
}

.include-route-toggle input {
  margin-right: 8px;
}

.route-input {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #495057;
}

.form-group input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ced4da;
  border-radius: 4px;
  font-size: 16px;
}

.waypoint-group {
  margin-bottom: 15px;
}

.waypoint-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #495057;
}

.waypoint-item {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
  align-items: center;
}

.waypoint-item input {
  flex: 1;
  padding: 10px 12px;
  border: 1px solid #ced4da;
  border-radius: 4px;
  font-size: 16px;
}

.remove-btn {
  background-color: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.add-waypoint-btn {
  padding: 8px 15px;
  background-color: #6c757d;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 5px;
}

.add-waypoint-btn:hover {
  background-color: #5a6268;
}

.map-container {
  height: 400px;
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

.map-instructions {
  position: absolute;
  bottom: 10px;
  left: 10px;
  right: 10px;
  background-color: rgba(255, 255, 255, 0.8);
  padding: 10px;
  border-radius: 4px;
  font-size: 14px;
  color: #495057;
}
</style>
