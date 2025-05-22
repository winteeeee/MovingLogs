<template>
  <div class="plan-spot-item">
    <div class="spot-order">
      <div class="order-buttons">
        <button
          class="order-button"
          :disabled="!canMoveUp"
          @click="$emit('move-up')"
          title="위로 이동"
        >
          <span class="icon-up"></span>
        </button>
        <button
          class="order-button"
          :disabled="!canMoveDown"
          @click="$emit('move-down')"
          title="아래로 이동"
        >
          <span class="icon-down"></span>
        </button>
      </div>
      <div class="order-number">{{ index + 1 }}</div>
    </div>

    <div class="spot-info">
      <div class="spot-name">{{ spot.name }}</div>
      <div class="spot-details">
        <span class="spot-category">{{ spot.category }}</span>
        <span class="spot-address">{{ spot.address }}</span>
      </div>
      <div class="spot-description">{{ spot.description }}</div>
    </div>

    <div class="spot-actions">
      <button class="button button-icon button-danger" @click="$emit('delete', spot)" title="삭제">
        <span class="icon-trash"></span>
      </button>
    </div>
  </div>
</template>

<script setup>
defineProps({
  spot: {
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
.plan-spot-item {
  display: flex;
  align-items: center;
  padding: 16px;
  background-color: white;
  transition: background-color 0.2s;
}

.plan-spot-item:hover {
  background-color: #f8f9fa;
}

/* 순서 영역 */
.spot-order {
  width: 60px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.order-buttons {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.order-button {
  width: 24px;
  height: 24px;
  border-radius: 4px;
  background-color: #f0f0f0;
  border: 1px solid #ced4da;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  padding: 0;
}

.order-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.order-button:hover:not(:disabled) {
  background-color: #e0e0e0;
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
.spot-info {
  flex: 1;
  padding-right: 16px;
}

.spot-name {
  font-weight: 600;
  font-size: 1rem;
  margin-bottom: 4px;
}

.spot-details {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
  font-size: 0.85rem;
}

.spot-category {
  background-color: #e9ecef;
  padding: 2px 8px;
  border-radius: 12px;
  color: #495057;
}

.spot-address {
  color: #6c757d;
}

.spot-description {
  font-size: 0.85rem;
  color: #6c757d;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 시간 영역 */
.spot-time {
  width: 180px;
  padding: 0 16px;
}

.spot-date {
  font-weight: 500;
  margin-bottom: 4px;
}

.spot-time-details {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.85rem;
  color: #6c757d;
}

.spot-duration {
  background-color: #e9ecef;
  padding: 2px 8px;
  border-radius: 12px;
}

/* 액션 영역 */
.spot-actions {
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

/* 아이콘 스타일 */
.icon-up::before {
  content: '↑';
}

.icon-down::before {
  content: '↓';
}

.icon-edit::before {
  content: '✏️';
}

.icon-trash::before {
  content: '🗑️';
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .plan-spot-item {
    flex-wrap: wrap;
  }

  .spot-order {
    width: 40px;
  }

  .spot-info {
    width: calc(100% - 40px);
    padding-right: 0;
    margin-bottom: 12px;
  }

  .spot-time {
    width: 50%;
    padding: 0;
  }

  .spot-actions {
    width: 50%;
    justify-content: flex-end;
  }
}
</style>
