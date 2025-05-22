<template>
  <div class="search-result-item" :class="{ selected: isSelected }" @click="toggle">
    <div class="spot-info">
      <div class="spot-type-badge" :class="typeClass">
        {{ typeName }}
      </div>
      <h6 class="spot-name">{{ spot.name }}</h6>
      <p class="spot-address">{{ spot.address }}</p>
    </div>
    <button
      class="btn btn-sm"
      :class="isSelected ? 'btn-danger' : 'btn-primary'"
      @click.stop="toggle"
    >
      <i class="bi" :class="isSelected ? 'bi-dash-lg' : 'bi-plus-lg'"></i>
      {{ isSelected ? '제거' : '추가' }}
    </button>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  spot: {
    type: Object,
    required: true,
  },
  isSelected: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['toggle'])

// 관광지 타입 클래스
const typeClass = computed(() => {
  const typeClasses = {
    attraction: 'bg-primary',
    culture: 'bg-info',
    festival: 'bg-warning',
    course: 'bg-success',
    leports: 'bg-danger',
    lodging: 'bg-secondary',
    shopping: 'bg-dark',
    restaurant: 'bg-success',
  }
  return typeClasses[props.spot.type] || 'bg-secondary'
})

// 관광지 타입 이름
const typeName = computed(() => {
  const typeNames = {
    attraction: '관광지',
    culture: '문화시설',
    festival: '축제행사',
    course: '여행코스',
    leports: '레포츠',
    lodging: '숙박',
    shopping: '쇼핑',
    restaurant: '음식점',
  }
  return typeNames[props.spot.type] || '기타'
})

// 관광지 선택/해제
function toggle() {
  emit('toggle', props.spot)
}
</script>

<style scoped>
.search-result-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #e9ecef;
  cursor: pointer;
  transition: background-color 0.2s;
}

.search-result-item:last-child {
  border-bottom: none;
}

.search-result-item:hover {
  background-color: #f8f9fa;
}

.search-result-item.selected {
  background-color: #fff3e0;
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
  color: white;
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
</style>
