<template>
  <div class="travel-plan-card">
    <div class="card-thumbnail">
      <img :src="plan.thumbnail" :alt="plan.title" class="thumbnail-img" />
      <div class="card-date-badge" :class="getDateBadgeClass">
        <span v-if="plan.dDay > 0">D-{{ plan.dDay }}</span>
        <span v-else>지난 여행</span>
      </div>
    </div>

    <div class="card-body">
      <h5 class="card-title">{{ plan.title }}</h5>
      <div class="card-description">
        <span class="icon-location"></span>
        <span v-if="plan.desc">{{ plan.desc }}</span>
        <span v-else>설명이 없습니다.</span>
      </div>

      <div class="card-date">
        <span class="icon-calendar"></span>
        <span>{{ formatDateRange }}</span>
      </div>

      <div class="card-updated">
        <span class="icon-clock"></span>
        <span>{{ formatUpdatedAt }} 업데이트</span>
      </div>
    </div>

    <div class="card-actions" @click="$emit('view', plan)">
      <button class="button button-secondary" @click="$emit('edit', plan)">
        <span class="icon-pencil"></span> 수정
      </button>
      <button class="button button-secondary" @click="handleDelete">
        <span class="icon-trash"></span> 삭제
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onBeforeUnmount, onMounted } from 'vue'

const props = defineProps({
  plan: {
    type: Object,
    required: true,
  },
})

const emit = defineEmits(['view', 'edit', 'delete'])

// 날짜 배지 클래스
const getDateBadgeClass = computed(() => {
  const today = new Date()
  today.setHours(0, 0, 0, 0)

  const startDate = new Date(props.plan.startDate)
  const endDate = new Date(props.plan.endDate)

  if (endDate < today) {
    return 'past'
  } else if (startDate <= today && today <= endDate) {
    return 'current'
  } else {
    return 'upcoming'
  }
})

// 날짜 범위 포맷팅
const formatDateRange = computed(() => {
  const start = new Date(props.plan.startDate)
  const end = new Date(props.plan.endDate)

  const startMonth = start.getMonth() + 1
  const startDay = start.getDate()
  const endMonth = end.getMonth() + 1
  const endDay = end.getDate()

  // 같은 달이면 월 표시 한 번만
  if (startMonth === endMonth && start.getFullYear() === end.getFullYear()) {
    return `${start.getFullYear()}년 ${startMonth}월 ${startDay}일 - ${endDay}일`
  }

  return `${start.getFullYear()}년 ${startMonth}월 ${startDay}일 - ${end.getFullYear()}년 ${endMonth}월 ${endDay}일`
})

// 업데이트 시간 포맷팅
const formatUpdatedAt = computed(() => {
  const updated = new Date(props.plan.updatedAt)
  const now = new Date()

  const diffTime = now.getTime() - updated.getTime()
  const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24))

  if (diffDays === 0) {
    const diffHours = Math.floor(diffTime / (1000 * 60 * 60))
    if (diffHours === 0) {
      const diffMinutes = Math.floor(diffTime / (1000 * 60))
      return `${diffMinutes}분 전`
    }
    return `${diffHours}시간 전`
  } else if (diffDays < 7) {
    return `${diffDays}일 전`
  } else {
    const month = updated.getMonth() + 1
    const day = updated.getDate()
    return `${updated.getFullYear()}.${month}.${day}`
  }
})

// 삭제 버튼 클릭
function handleDelete() {
  emit('delete', props.plan)
}
</script>

<style scoped>
/* 여행 계획 카드 */
.travel-plan-card {
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.travel-plan-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  cursor: pointer;
}

.card-thumbnail {
  position: relative;
  height: 160px;
  overflow: hidden;
}

.thumbnail-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.travel-plan-card:hover .thumbnail-img {
  transform: scale(1.05);
}

.card-date-badge {
  position: absolute;
  top: 12px;
  right: 12px;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.75rem;
  font-weight: 600;
  color: white;
}

.card-date-badge.past {
  background-color: #6c757d;
}

.card-date-badge.current {
  background-color: #2ecc71;
}

.card-date-badge.upcoming {
  background-color: #4a6bdf;
}

.card-body {
  padding: 16px;
  flex: 1;
}

.card-title {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 8px;
  color: #333;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  height: 2.8rem;
}

.card-description {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 8px;
  font-size: 0.9rem;
  color: #495057;
}

.card-date {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 12px;
  font-size: 0.9rem;
  color: #495057;
}

.card-updated {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 0.8rem;
  color: #adb5bd;
}

.card-actions {
  display: flex;
  padding: 12px 16px;
  border-top: 1px solid #e9ecef;
  gap: 8px;
  justify-content: space-around;
}

.card-actions .button-primary {
  flex: 1;
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

.button-secondary {
  background-color: #f0f0f0;
  color: #333;
}

.button-secondary:hover {
  background-color: #e0e0e0;
}

/* 아이콘 스타일 */
.icon-location::before {
  content: '📍';
  margin-right: 6px;
}

.icon-calendar::before {
  content: '📅';
  margin-right: 6px;
}

.icon-clock::before {
  content: '🕒';
  margin-right: 6px;
}

.icon-pencil::before {
  content: '✏️';
  margin-right: 6px;
}

.icon-trash::before {
  content: '🗑️';
  margin-right: 6px;
}
</style>
