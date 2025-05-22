<template>
  <div class="form-section" v-if="includeRoute">
    <h3>경로별 이미지</h3>

    <div class="route-images">
      <div class="route-point-images">
        <h4>출발지 이미지</h4>
        <PostWriteImageUploader
          :images="routeImages.departure"
          @image-added="(image) => handleImageAdded('departure', image)"
          @image-removed="(data) => handleImageRemoved('departure', data.index, data.image)"
        />
      </div>

      <div
        v-for="(waypoint, index) in routeData.waypoints"
        :key="`waypoint-${index}`"
        class="route-point-images"
      >
        <h4>경유지 {{ index + 1 }} 이미지</h4>
        <PostWriteImageUploader
          :images="getWaypointImages(index)"
          @image-added="(image) => handleImageAdded(`waypoint-${index}`, image)"
          @image-removed="(data) => handleImageRemoved(`waypoint-${index}`, data.index, data.image)"
        />
      </div>

      <div class="route-point-images">
        <h4>도착지 이미지</h4>
        <PostWriteImageUploader
          :images="routeImages.destination"
          @image-added="(image) => handleImageAdded('destination', image)"
          @image-removed="(data) => handleImageRemoved('destination', data.index, data.image)"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, watch, defineProps, defineEmits } from 'vue'
import PostWriteImageUploader from './PostWriteImageUploader.vue'

const props = defineProps({
  includeRoute: {
    type: Boolean,
    required: true,
  },
  routeData: {
    type: Object,
    required: true,
  },
  routeImages: {
    type: Object,
    default: () => ({
      departure: [],
      destination: [],
      // waypoint-0, waypoint-1 등은 동적으로 추가됨
    }),
  },
})

const emit = defineEmits(['image-added', 'image-removed'])

// 경유지 변경 감지
watch(
  () => props.routeData.waypoints,
  (newWaypoints) => {
    // 경유지 이미지 배열 초기화
    newWaypoints.forEach((_, index) => {
      if (!props.routeImages[`waypoint-${index}`]) {
        props.routeImages[`waypoint-${index}`] = []
      }
    })

    // 제거된 경유지의 이미지 배열 정리
    Object.keys(props.routeImages).forEach((key) => {
      if (key.startsWith('waypoint-')) {
        const waypointIndex = parseInt(key.split('-')[1])
        if (waypointIndex >= newWaypoints.length) {
          delete props.routeImages[key]
        }
      }
    })
  },
  { deep: true },
)

function getWaypointImages(index) {
  const key = `waypoint-${index}`
  if (!props.routeImages[key]) {
    props.routeImages[key] = []
  }
  return props.routeImages[key]
}

function handleImageAdded(pointType, image) {
  // 이미지 추가 이벤트 처리
  if (!props.routeImages[pointType]) {
    props.routeImages[pointType] = []
  }

  props.routeImages[pointType].push(image)
  emit('image-added', { pointType, image })
}

function handleImageRemoved(pointType, index, image) {
  // 이미지 제거 이벤트 처리
  if (props.routeImages[pointType] && index >= 0 && index < props.routeImages[pointType].length) {
    props.routeImages[pointType].splice(index, 1)
    emit('image-removed', { pointType, index, image })
  }
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

.route-images {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.route-point-images {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 15px;
}

.route-point-images h4 {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 10px;
  color: #495057;
}
</style>
