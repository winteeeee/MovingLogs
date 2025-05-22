<template>
  <div class="plan-update-header">
    <div class="header-content">
      <div class="header-title">
        <h1 class="title">{{ plan?.title || '여행 계획 수정' }}</h1>
        <span class="last-updated" v-if="plan?.updatedAt">
          마지막 업데이트: {{ formatDate(plan.updatedAt) }}
        </span>
      </div>

      <div class="header-actions">
        <button class="button button-danger" @click="$emit('delete')" :disabled="isSaving">
          <span class="icon-trash"></span> 삭제
        </button>

        <button class="button button-secondary" @click="$emit('cancel')" :disabled="isSaving">
          취소
        </button>

        <button
          class="button button-primary"
          @click="$emit('save')"
          :disabled="isSaving || !hasChanges"
        >
          <span class="icon-save"></span>
          {{ isSaving ? '저장 중...' : '저장' }}
        </button>
      </div>
    </div>

    <div class="save-status" v-if="hasChanges">
      <span class="icon-info"></span>
      저장되지 않은 변경 사항이 있습니다.
    </div>
  </div>
</template>

<script setup>
defineProps({
  plan: {
    type: Object,
    default: null,
  },
  isSaving: {
    type: Boolean,
    default: false,
  },
  hasChanges: {
    type: Boolean,
    default: false,
  },
})

defineEmits(['save', 'cancel', 'delete'])

// 날짜 포맷팅
function formatDate(dateString) {
  const date = new Date(dateString)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')

  return `${year}.${month}.${day} ${hours}:${minutes}`
}
</script>

<style scoped>
/* 헤더 스타일 */
.plan-update-header {
  margin-bottom: 24px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.title {
  font-size: 1.75rem;
  font-weight: 700;
  color: #333;
  margin: 0;
}

.last-updated {
  font-size: 0.85rem;
  color: #6c757d;
  margin-top: 4px;
  display: block;
}

.header-actions {
  display: flex;
  gap: 12px;
}

/* 저장 상태 알림 */
.save-status {
  background-color: #fff8e6;
  border-radius: 4px;
  padding: 8px 12px;
  font-size: 0.9rem;
  color: #856404;
  display: flex;
  align-items: center;
  gap: 8px;
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

.button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.button-primary {
  background-color: #4a6bdf;
  color: white;
}

.button-primary:hover:not(:disabled) {
  background-color: #3a5bc9;
}

.button-secondary {
  background-color: #f0f0f0;
  color: #333;
}

.button-secondary:hover:not(:disabled) {
  background-color: #e0e0e0;
}

.button-danger {
  background-color: #f8d7da;
  color: #dc3545;
}

.button-danger:hover:not(:disabled) {
  background-color: #f5c6cb;
}

/* 아이콘 스타일 */
.icon-save::before {
  content: '💾';
  margin-right: 6px;
}

.icon-info::before {
  content: 'ℹ️';
}

.icon-trash::before {
  content: '🗑️';
  margin-right: 6px;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .header-actions {
    width: 100%;
  }

  .button {
    flex: 1;
  }
}
</style>
