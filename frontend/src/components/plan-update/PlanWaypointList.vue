<template>
  <div class="plan-waypoint-list">
    <h2 class="section-title">
      여행 장소
      <span class="waypoint-count">{{ waypointList.length }}개 장소</span>
    </h2>

    <div v-if="waypointList.length === 0" class="empty-waypointList">
      <div class="empty-content">
        <span class="icon-map-pin"></span>
        <h3>등록된 장소가 없습니다</h3>
        <p>아래 '장소 추가' 버튼을 통해 여행 장소를 추가해보세요.</p>
      </div>
    </div>

    <div v-else class="waypoint-list-container">
      <div class="waypoint-list-header">
        <div class="waypoint-header-order">순서</div>
        <div class="waypoint-header-info">장소 정보</div>
        <div class="waypoint-header-actions">삭제</div>
      </div>

      <Draggable
        v-model="localList"
        class="waypoint-list"
        item-key="id"
        handle=".drag-handle"
        @end="onDragEnd"
      >
        <template #item="{ element, index }">
            <PlanWaypointItem
              :waypoint="element"
              :index="index"
              @edit="$emit('edit-waypoint', $event)"
              @delete="$emit('delete-waypoint', $event)"
            />
        </template>
      </Draggable>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import Draggable from 'vuedraggable'
import PlanWaypointItem from '@/components/plan-update/PlanWaypointItem.vue'

const props = defineProps({
  waypointList: {
    type: Array,
    default: () => [],
  },
})

const emit = defineEmits(['update:waypointList', 'edit-waypoint', 'delete-waypoint'])

// local copy for draggable
const localList = ref([...props.waypointList])

// props 변경 감지 시 localList 동기화
watch(() => props.waypointList, newList => {
  localList.value = [...newList]
}, { deep: true })

// 드래그가 끝났을 때 순서 업데이트
function onDragEnd() {
  // order 재정렬
  const updated = localList.value.map((waypoint, idx) => ({
    ...waypoint,
    order: idx
  }))
  emit('update:waypointList', updated)
}
</script>

<style scoped>
/* 섹션 스타일 */
.plan-waypoint-list {
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

.waypoint-count {
  font-size: 0.9rem;
  font-weight: normal;
  color: #6c757d;
  margin-left: 8px;
}

/* 빈 상태 */
.empty-waypointList {
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
.waypoint-list-container {
  margin-top: 16px;
}

.waypoint-list-header {
  display: flex;
  padding: 12px 16px;
  background-color: #f8f9fa;
  border-radius: 4px 4px 0 0;
  border: 1px solid #e9ecef;
  font-weight: 500;
  font-size: 0.9rem;
  color: #495057;
}

.waypoint-header-order {
  width: 60px;
  text-align: center;
}

.waypoint-header-info {
  flex: 1;
}

.waypoint-header-time {
  width: 180px;
}

.waypoint-header-actions {
  width: 120px;
  text-align: center;
}

.waypoint-list {
  border-left: 1px solid #e9ecef;
  border-right: 1px solid #e9ecef;
  border-bottom: 1px solid #e9ecef;
  border-radius: 0 0 4px 4px;
}

.waypoint-item-wrapper {
  border-bottom: 1px solid #e9ecef;
}

.waypoint-item-wrapper:last-child {
  border-bottom: none;
}

/* 아이콘 스타일 */
.icon-map-pin::before {
  content: '📍';
  font-size: 2rem;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .waypoint-list-header {
    display: none;
  }
}

.drag-handle {
  cursor: grab;
}
</style>
