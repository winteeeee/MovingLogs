<template>
  <div class="map-container">
    <div id="kakaoMap" ref="kakaoMapRef"></div>
    <div class="map-controls">
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, defineProps, defineExpose } from 'vue'

const props = defineProps({
  posts: {
    type: Array,
    required: true,
  },
  kakaoMap: {
    type: Object,
    default: null,
  },
})

const kakaoMapRef = ref(null)
const map = ref(null)
const markers = ref([])
const polylines = ref([])

const initMap = () => {
  if (!kakaoMapRef.value) return

  if (window.kakao && window.kakao.maps) {
    createMap()
  } else {
    loadKakaoMapScript()
  }
}

const loadKakaoMapScript = () => {
  const script = document.createElement('script')
  script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=YOUR_KAKAO_MAP_API_KEY&autoload=false`
  script.onload = () => {
    window.kakao.maps.load(() => {
      createMap()
    })
  }
  document.head.appendChild(script)
}

const createMap = () => {
  const mapOptions = {
    center: new window.kakao.maps.LatLng(36.2, 127.9), // Korea center
    level: 13,
  }

  map.value = new window.kakao.maps.Map(kakaoMapRef.value, mapOptions)
  displayPostsOnMap()
}

const clearMapOverlays = () => {
  // Remove markers
  markers.value.forEach((marker) => {
    marker.setMap(null)
  })
  markers.value = []

  // Remove polylines
  polylines.value.forEach((polyline) => {
    polyline.setMap(null)
  })
  polylines.value = []
}

const displayPostsOnMap = () => {
  clearMapOverlays()

  props.posts.forEach((post) => {
    if (post.waypoints && post.waypoints.coordinates && post.waypoints.coordinates.length > 0) {
      // Create path coordinates
      const path = post.waypoints.coordinates.map(
        (coord) => new window.kakao.maps.LatLng(coord.lat, coord.lng),
      )

      // Create polyline
      const polyline = new window.kakao.maps.Polyline({
        path: path,
        strokeWeight: 5,
        strokeColor: '#FF6B6B',
        strokeOpacity: 0.7,
        strokeStyle: 'solid',
      })

      polyline.setMap(map.value)
      polylines.value.push(polyline)

      // Create start and end markers
      const startMarker = new window.kakao.maps.Marker({
        position: path[0],
        map: map.value,
      })

      const endMarker = new window.kakao.maps.Marker({
        position: path[path.length - 1],
        map: map.value,
      })

      markers.value.push(startMarker, endMarker)
    }
  })
}

const showWaypointsOnMap = (waypoints) => {
  clearMapOverlays()

  if (waypoints && waypoints.length > 0) {
    const path = waypoints.map(
      (waypoint) => new window.kakao.maps.LatLng(waypoint.latitude, waypoint.longitude),
    )

    // Create polyline
    const polyline = new window.kakao.maps.Polyline({
      path: path,
      strokeWeight: 5,
      strokeColor: '#4CAF50',
      strokeOpacity: 0.9,
      strokeStyle: 'solid',
    })

    polyline.setMap(map.value)
    polylines.value.push(polyline)

    // Create start and end markers
    const startMarker = new window.kakao.maps.Marker({
      position: path[0],
      map: map.value,
    })

    const endMarker = new window.kakao.maps.Marker({
      position: path[path.length - 1],
      map: map.value,
    })

    markers.value.push(startMarker, endMarker)

    // Create waypoint markers
    for (let i = 1; i < path.length - 1; i++) {
      const waypointMarker = new window.kakao.maps.Marker({
        position: path[i],
        map: map.value,
      })
      markers.value.push(waypointMarker)
    }

    // Set map bounds to show all markers
    const bounds = new window.kakao.maps.LatLngBounds()
    path.forEach((point) => {
      bounds.extend(point)
    })
    map.value.setBounds(bounds)
  }
}

onMounted(() => {
  initMap()
})

// Expose methods to parent component
defineExpose({
  initMap,
  showWaypointsOnMap,
})
</script>

<style scoped>
.map-container {
  height: 400px;
  margin-bottom: 20px;
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
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
</style>
