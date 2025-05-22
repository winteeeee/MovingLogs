<template>
  <div class="plan-spot-list">
    <h2 class="section-title">
      여행 장소
      <span class="spot-count">{{ spots.length }}개 장소</span>
    </h2>

    <div v-if="spots.length === 0" class="empty-spots">
      <div class="empty-content">
        <span class="icon-map-pin"></span>
        <h3>등록된 장소가 없습니다</h3>
        <p>아래 '장소 추가' 버튼을 통해 여행 장소를 추가해보세요.</p>
      </div>
    </div>

    <div v-else class="spot-list-container">
      <div class="spot-list-header">
        <div class="spot-header-order">순서</div>
        <div class="spot-header-info">장소 정보</div>
        <div class="spot-header-actions">관리</div>
      </div>

      <div class="spot-list">
        <div v-for="(spot, index) in spots" :key="spot.id" class="spot-item-wrapper">
          <PlanSpotItem
            :spot="spot"
            :index="index"
            @edit="$emit('edit-spot', $event)"
            @delete="$emit('delete-spot', $event)"
            @move-up="moveSpot(index, index - 1)"
            @move-down="moveSpot(index, index + 1)"
            :can-move-up="index > 0"
            :can-move-down="index < spots.length - 1"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import PlanSpotItem from '@/components/plan-update/PlanSpotItem.vue'

const props = defineProps({
  spots: {
    type: Array,
    default: () => [],
  },
})

const emit = defineEmits(['update:spots', 'edit-spot', 'delete-spot'])

// 스팟 위치 이동
function moveSpot(fromIndex, toIndex) {
  if (fromIndex === toIndex || toIndex < 0 || toIndex >= props.spots.length) return

  const updatedSpots = [...props.spots]
  const [movedItem] = updatedSpots.splice(fromIndex, 1)
  updatedSpots.splice(toIndex, 0, movedItem)

  // 순서 재정렬
  updatedSpots.forEach((spot, index) => {
    spot.order = index
  })

  emit('update:spots', updatedSpots)
}
</script>

<style scoped>
/* 섹션 스타일 */
.plan-spot-list {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 24px;
}

.section-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid #e9ecef;
  display: flex;
  align-items: center;
}

.spot-count {
  font-size: 0.9rem;
  font-weight: normal;
  color: #6c757d;
  margin-left: 8px;
}

/* 빈 상태 */
.empty-spots {
  background-color: #f8f9fa;
  border-radius: 8px;
  border: 1px dashed #dee2e6;
  padding: 40px 24px;
  margin-top: 16px;
}

.empty-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.empty-content h3 {
  font-size: 1.1rem;
  font-weight: 600;
  color: #495057;
  margin: 12px 0 8px;
}

.empty-content p {
  color: #6c757d;
  margin: 0;
}

/* 스팟 목록 */
.spot-list-container {
  margin-top: 16px;
}

.spot-list-header {
  display: flex;
  padding: 12px 16px;
  background-color: #f8f9fa;
  border-radius: 4px 4px 0 0;
  border: 1px solid #e9ecef;
  font-weight: 500;
  font-size: 0.9rem;
  color: #495057;
}

.spot-header-order {
  width: 60px;
  text-align: center;
}

.spot-header-info {
  flex: 1;
}

.spot-header-time {
  width: 180px;
}

.spot-header-actions {
  width: 120px;
  text-align: center;
}

.spot-list {
  border-left: 1px solid #e9ecef;
  border-right: 1px solid #e9ecef;
  border-bottom: 1px solid #e9ecef;
  border-radius: 0 0 4px 4px;
}

.spot-item-wrapper {
  border-bottom: 1px solid #e9ecef;
}

.spot-item-wrapper:last-child {
  border-bottom: none;
}

/* 아이콘 스타일 */
.icon-map-pin::before {
  content: '📍';
  font-size: 2rem;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .spot-list-header {
    display: none;
  }
}
</style>
