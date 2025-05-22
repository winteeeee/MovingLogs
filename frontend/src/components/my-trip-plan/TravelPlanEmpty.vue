<template>
  <div class="travel-plan-empty">
    <div class="empty-content">
      <div class="empty-icon">
        <span :class="hasPlans ? 'icon-search' : 'icon-map'"></span>
      </div>

      <h3 class="empty-title">
        {{ hasPlans ? '검색 결과가 없습니다' : '아직 여행 계획이 없습니다' }}
      </h3>
      <p class="empty-message">
        {{
          hasPlans
            ? '다른 검색어나 필터 조건을 사용해보세요.'
            : '첫 번째 여행 계획을 만들어 여행을 시작해보세요!'
        }}
      </p>

      <div class="empty-actions">
        <button
          v-if="isFilterActive"
          class="button button-secondary"
          @click="$emit('clear-filter')"
        >
          <span class="icon-x"></span> 필터 초기화
        </button>

        <button v-if="!hasPlans" class="button button-primary" @click="$emit('create-new')">
          <span class="icon-plus"></span> 첫 여행 계획 만들기
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  hasPlans: {
    type: Boolean,
    default: false,
  },
  isFilterActive: {
    type: Boolean,
    default: false,
  },
})

defineEmits(['clear-filter', 'create-new'])
</script>

<style scoped>
/* 빈 상태 */
.travel-plan-empty {
  background-color: #f8f9fa;
  border-radius: 8px;
  border: 1px dashed #dee2e6;
  padding: 48px 24px;
}

.empty-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  max-width: 500px;
  margin: 0 auto;
}

.empty-icon {
  font-size: 3rem;
  color: #adb5bd;
  margin-bottom: 16px;
}

.empty-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #495057;
  margin-bottom: 8px;
}

.empty-message {
  color: #6c757d;
  margin-bottom: 24px;
}

.empty-actions {
  display: flex;
  justify-content: center;
  gap: 12px;
}

/* 버튼 스타일 */
.button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 8px 16px;
  border-radius: 4px;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  border: none;
  outline: none;
}

.button-primary {
  background-color: #4a6bdf;
  color: white;
}

.button-primary:hover {
  background-color: #3a5bc9;
}

.button-secondary {
  background-color: #f0f0f0;
  color: #333;
}

.button-secondary:hover {
  background-color: #e0e0e0;
}

/* 아이콘 스타일 */
.icon-search::before {
  content: '🔍';
}

.icon-map::before {
  content: '🗺️';
}

.icon-x::before {
  content: '×';
  margin-right: 6px;
}

.icon-plus::before {
  content: '+';
  margin-right: 6px;
}
</style>
