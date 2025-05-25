<template>
  <div class="board-header">
    <h1 class="mb-4">여행 커뮤니티</h1>
<!--    <div class="board-tabs">-->
<!--      <button-->
<!--        v-for="tab in tabs"-->
<!--        :key="tab.value"-->
<!--        :class="['tab-btn', { active: activeTab === tab.value }]"-->
<!--        @click="$emit('update:activeTab', tab.value)"-->
<!--      >-->
<!--        {{ tab.label }}-->
<!--      </button>-->
<!--    </div>-->
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'

defineProps({
  activeTab: {
    type: String,
    required: true,
  },
})

defineEmits(['update:activeTab'])

const tabs = [
  { value: 'tripost', label: '여행게시판' },
  // { value: 'notice', label: '공지사항' },
  // { value: 'tips', label: '여행 팁' },
  // { value: 'food', label: '현지 맛집' },
  // { value: 'accommodation', label: '숙소 추천' },
]
</script>

<style scoped>
.board-header {
  margin-bottom: 20px;
}

.board-header h1 {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 15px;
  color: #333;
}

.board-tabs {
  display: flex;
  border-bottom: 2px solid #e9ecef;
  margin-bottom: 20px;
}

.tab-btn {
  padding: 10px 20px;
  background: none;
  border: none;
  font-size: 16px;
  font-weight: 500;
  color: #6c757d;
  cursor: pointer;
  transition: all 0.3s ease;
  white-space: nowrap;
}

.tab-btn.active {
  color: #fd7e14;
  border-bottom: 2px solid #fd7e14;
  margin-bottom: -2px;
}

@media (max-width: 768px) {
  .board-header h1 {
    font-size: 24px;
  }

  .tab-btn {
    padding: 8px 15px;
    font-size: 14px;
  }
}
</style>
