<template>
  <div class="travel-plan-header">
    <div class="header-content">
      <div class="header-title">
        <h1 class="title">내 여행 계획</h1>
        <span class="plan-count">{{ totalPlans }}개의 여행 계획</span>
      </div>

      <div class="header-actions">
        <button class="button button-primary" @click="$emit('create-new')">
          <span class="icon-plus"></span> 새 여행 계획
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  totalPlans: {
    type: Number,
    default: 0,
  },
})

defineEmits(['create-new'])
</script>

<style scoped>
/* 헤더 스타일 */
.travel-plan-header {
  margin-bottom: 24px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  font-size: 1.75rem;
  font-weight: 700;
  color: #333;
  margin: 0;
}

.plan-count {
  font-size: 0.9rem;
  color: #6c757d;
  margin-left: 8px;
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

/* 아이콘 스타일 */
.icon-plus::before {
  content: '+';
  margin-right: 6px;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .header-title {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
  }

  .plan-count {
    margin-left: 0;
    margin-top: 4px;
  }
}
</style>
