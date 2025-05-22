<template>
  <div class="selected-spot-item" :class="{ active: isActive }" @click="focus(spot)">
    <div class="spot-order">{{ index + 1 }}</div>
    <div class="spot-info">
      <div class="spot-type-badge" :class="typeClass">
        {{ typeName }}
      </div>
      <h6 class="spot-name">{{ spot.name }}</h6>
      <p class="spot-address">{{ spot.address }}</p>
    </div>
    <div class="spot-actions">
      <button
        class="btn btn-sm btn-outline-secondary me-1"
        @click.stop="moveUp"
        :disabled="isFirst"
      >
        <i class="bi bi-arrow-up"></i>
      </button>
      <button
        class="btn btn-sm btn-outline-secondary me-1"
        @click.stop="moveDown"
        :disabled="isLast"
      >
        <i class="bi bi-arrow-down"></i>
      </button>
      <button class="btn btn-sm btn-outline-danger" @click.stop="remove">
        <i class="bi bi-trash"></i>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { getSpotTypeClass, getSpotTypeName } from '../../utils/tourismUtils.js'

const props = defineProps({
  spot: {
    type: Object,
    required: true,
  },
  index: {
    type: Number,
    required: true,
  },
  isFirst: {
    type: Boolean,
    default: false,
  },
  isLast: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['move-up', 'move-down', 'remove', 'focus'])

// 활성화 상태
const isActive = ref(false)

// 관광지 타입 클래스
const typeClass = computed(() => {
  return getSpotTypeClass(props.spot.type)
})

// 관광지 타입 이름
const typeName = computed(() => {
  return getSpotTypeName(props.spot.type)
})

// 관광지 순서 위로 이동
function moveUp() {
  if (!props.isFirst) {
    emit('move-up', props.index)
  }
}

// 관광지 순서 아래로 이동
function moveDown() {
  if (!props.isLast) {
    emit('move-down', props.index)
  }
}

// 관광지 제거
function remove() {
  emit('remove', props.index)
}

// 관광지 포커스
function focus(spot) {
  isActive.value = true
  emit('focus', spot)

  // 3초 후 활성화 상태 해제
  setTimeout(() => {
    isActive.value = false
  }, 3000)
}
</script>

<style scoped>
.selected-spot-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #e9ecef;
  transition: background-color 0.2s;
  cursor: pointer;
}

.selected-spot-item:last-child {
  border-bottom: none;
}

.selected-spot-item:hover {
  background-color: #f8f9fa;
}

.selected-spot-item.active {
  background-color: #fff3e0;
}

.spot-order {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  background-color: var(--primary-color);
  color: white;
  border-radius: 50%;
  font-weight: 600;
  margin-right: 16px;
  flex-shrink: 0;
}

.spot-info {
  flex: 1;
}

.spot-type-badge {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  margin-bottom: 6px;
}

.spot-name {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 4px;
  color: #333;
}

.spot-address {
  font-size: 13px;
  color: #6c757d;
  margin: 0;
}

.spot-actions {
  display: flex;
  gap: 4px;
  margin-left: 16px;
}

@media (max-width: 767.98px) {
  .spot-actions {
    flex-direction: column;
    gap: 4px;
  }
}
</style>
