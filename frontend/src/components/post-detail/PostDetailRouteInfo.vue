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
          <i
            class="point-marker-end bi bi-flag-fill"
            v-else-if="index === waypoints.length - 1"
          ></i>
          <i class="point-marker-middle bi bi-geo" v-else></i>
        </div>
        <div class="point-info">
          <span class="point-label me-2">{{ index + 1 }}</span>
          <span class="point-name">{{ waypoint.attractionName }}</span>
        </div>
      </div>
    </div>

    <RouteMap :waypoints="waypoints" :map-visible="mapVisible" />
    <div v-if="mapVisible" class="route-map-container">
      <div id="routeMap" ref="routeMapRef" class="route-map"></div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'
import RouteMap from '@/components/common/RouteMap.vue'

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

@media (max-width: 768px) {
  .route-info {
    padding: 15px;
  }

  .point-name {
    font-size: 14px;
  }
}
</style>
