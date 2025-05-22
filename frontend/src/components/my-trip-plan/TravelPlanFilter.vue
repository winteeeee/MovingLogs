<template>
  <div class="travel-plan-filter">
    <div class="filter-container">
      <div class="search-box">
        <div class="search-input-wrapper">
          <span class="search-icon"></span>
          <input
            type="text"
            class="search-input"
            placeholder="여행 계획 검색..."
            v-model="localFilter.keyword"
            @input="updateFilter"
          />
        </div>
      </div>

      <div class="filter-options">
        <div class="filter-group">
          <label class="filter-label">날짜 범위:</label>
          <div class="select-wrapper">
            <select class="select-input" v-model="localFilter.dateRange" @change="updateFilter">
              <option value="all">전체 기간</option>
              <option value="upcoming">예정된 여행</option>
              <option value="past">지난 여행</option>
            </select>
          </div>
        </div>

        <div class="filter-group">
          <label class="filter-label">정렬:</label>
          <div class="select-wrapper">
            <select class="select-input" v-model="localFilter.sortBy" @change="updateFilter">
              <option value="newest">최신순</option>
              <option value="oldest">오래된순</option>
              <option value="title">제목순</option>
            </select>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  filter: {
    type: Object,
    required: true,
  },
})

const emit = defineEmits(['update:filter'])

// 로컬 필터 상태
const localFilter = ref({ ...props.filter })

// props 변경 감지
watch(
  () => props.filter,
  (newFilter) => {
    localFilter.value = { ...newFilter }
  },
  { deep: true },
)

// 필터 업데이트
function updateFilter() {
  emit('update:filter', { ...localFilter.value })
}
</script>

<style scoped>
/* 필터 스타일 */
.travel-plan-filter {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 24px;
}

.filter-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
}

.search-box {
  flex: 1;
  max-width: 400px;
}

.search-input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.search-icon {
  position: absolute;
  left: 10px;
  color: #6c757d;
}

.search-icon::before {
  content: '🔍';
}

.search-input {
  width: 100%;
  padding: 8px 8px 8px 30px;
  border: 1px solid #ced4da;
  border-radius: 4px;
  font-size: 0.9rem;
}

.search-input:focus {
  outline: none;
  border-color: #4a6bdf;
  box-shadow: 0 0 0 2px rgba(74, 107, 223, 0.25);
}

.filter-options {
  display: flex;
  gap: 16px;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-label {
  font-size: 0.85rem;
  color: #495057;
  margin-bottom: 0;
  white-space: nowrap;
}

.select-wrapper {
  position: relative;
}

.select-input {
  appearance: none;
  padding: 6px 24px 6px 10px;
  border: 1px solid #ced4da;
  border-radius: 4px;
  background-color: white;
  font-size: 0.85rem;
  cursor: pointer;
}

.select-wrapper::after {
  content: '';
  position: absolute;
  top: 50%;
  right: 10px;
  transform: translateY(-50%);
  width: 0;
  height: 0;
  border-left: 5px solid transparent;
  border-right: 5px solid transparent;
  border-top: 5px solid #6c757d;
  pointer-events: none;
}

.select-input:focus {
  outline: none;
  border-color: #4a6bdf;
  box-shadow: 0 0 0 2px rgba(74, 107, 223, 0.25);
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .filter-container {
    flex-direction: column;
    align-items: stretch;
  }

  .search-box {
    max-width: 100%;
  }

  .filter-options {
    justify-content: space-between;
  }
}

@media (max-width: 576px) {
  .filter-options {
    flex-direction: column;
    gap: 12px;
  }

  .filter-group {
    width: 100%;
  }

  .filter-group .select-wrapper {
    flex: 1;
  }
}
</style>
