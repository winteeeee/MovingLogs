<template>
  <div class="plan-waypoint-item drag-handle">
    <div class="waypoint-order">
      <div class="order-number">{{ index + 1 }}</div>
    </div>

    <div class="waypoint-info">
      <div class="waypoint-name">{{ waypoint.title }}</div>
      <div class="waypoint-details">
        <span class="waypoint-category">{{ waypoint.contentTypeName }}</span>
        <span class="waypoint-address">{{ waypoint.addr1 }}</span>
      </div>
    </div>

    <div class="waypoint-actions">
      <button class="button button-icon button-danger" @click="$emit('delete', waypoint)" title="삭제">
        <span class="icon-trash"></span>
      </button>
    </div>
  </div>
</template>

<script setup>
defineProps({
  waypoint: {
    type: Object,
    required: true,
  },
  index: {
    type: Number,
    required: true,
  },
  canMoveUp: {
    type: Boolean,
    default: false,
  },
  canMoveDown: {
    type: Boolean,
    default: false,
  },
})

defineEmits(['delete', 'move-up', 'move-down'])
</script>

<style scoped>
/* 스팟 아이템 스타일 */
.plan-waypoint-item {
  display: flex;
  align-items: center;
  padding: 16px;
  background-color: white;
  transition: background-color 0.2s;
}

.plan-waypoint-item:hover {
  background-color: #f8f9fa;
}

/* 순서 영역 */
.waypoint-order {
  width: 60px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.order-number {
  font-weight: 600;
  color: #495057;
  background-color: #e9ecef;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.85rem;
}

/* 정보 영역 */
.waypoint-info {
  flex: 1;
  padding-right: 16px;
}

.waypoint-name {
  font-weight: 600;
  font-size: 1rem;
  margin-bottom: 4px;
}

.waypoint-details {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
  font-size: 0.85rem;
}

.waypoint-category {
  background-color: #e9ecef;
  padding: 2px 8px;
  border-radius: 12px;
  color: #495057;
}

.waypoint-address {
  color: #6c757d;
}

/* 액션 영역 */
.waypoint-actions {
  width: 120px;
  display: flex;
  justify-content: center;
  gap: 8px;
}

/* 버튼 스타일 */
.button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  border: none;
  outline: none;
}

.button-icon {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-color: #f0f0f0;
  color: #495057;
}

.button-icon:hover {
  background-color: #e0e0e0;
}

.button-danger {
  color: #dc3545;
}

.button-danger:hover {
  background-color: #f8d7da;
}

.icon-trash::before {
  content: '🗑️';
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .plan-waypoint-item {
    flex-wrap: wrap;
  }

  .waypoint-order {
    width: 40px;
  }

  .waypoint-info {
    width: calc(100% - 40px);
    padding-right: 0;
    margin-bottom: 12px;
  }

  .waypoint-actions {
    width: 50%;
    justify-content: flex-end;
  }
}
</style>
