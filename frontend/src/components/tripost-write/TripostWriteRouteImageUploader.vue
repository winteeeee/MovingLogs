<template>
  <div class="form-section">
    <h3>경로별 이미지</h3>

    <div class="route-images">
      <div
        v-for="(waypoint, index) in waypoints"
        :key="waypoint.id"
        class="route-point-images"
      >
        <h4>{{ index + 1 }}. {{ waypoint.title }}</h4>
        <TripostWriteImageUploader
          :images="waypoint.images"
          @image-added="(image) => handleImageAdded(index, image)"
          @image-removed="(data) => handleImageRemoved(index, data.index, data.image)"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, watch, defineProps, defineEmits } from 'vue'
import TripostWriteImageUploader from './TripostWriteImageUploader.vue'

const props = defineProps({
  waypoints: {
    type: [],
    required: true,
  },
})

const emit = defineEmits(['image-added', 'image-removed'])


function handleImageAdded(waypointIndex, image) {
  emit('image-added', { waypointIndex, image })
}

function handleImageRemoved(waypointIndex, index, image) {
  // 이미지 제거 이벤트 처리
  if (props.waypoints[waypointIndex] && index >= 0 && index < props.waypoints[waypointIndex].images.length) {
    emit('image-removed', { waypointIndex, index, image })
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
