<template>
  <div class="search-filter-container">
    <div class="search-box">
      <select
        type="text"
        v-model="localSearchType"
        @change="updateSearchType"
        placeholder="검색 타입"
        class="form-control form-control-sm"
      >
        <option value="TITLE">제목만</option>
        <option value="CONTENT">내용만</option>
        <option value="AUTHOR">작성자</option>
      </select>
      <input
        type="text"
        v-model="localSearchQuery"
        @input="updateSearchQuery"
        placeholder="검색어를 입력하세요"
        class="form-control form-control-sm search-input"
        v-on:keyup.enter="$emit('search')"
      />
      <button class="search-btn" @click="$emit('search')"><i class="bi bi-search"></i> 검색</button>
    </div>

    <div class="map-toggle">
      <button class="map-toggle-btn" @click="$emit('toggle-map')">
        <i :class="['bi', mapVisible ? 'bi-map-fill' : 'bi-map']"></i>
        {{ mapVisible ? '지도 숨기기' : '지도 보기' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, ref, watch } from 'vue'

const props = defineProps({
  searchType: {
    type: String,
    required: true,
  },
  searchQuery: {
    type: String,
    required: true,
  },
  mapVisible: {
    type: Boolean,
    required: true,
  },
})

const emit = defineEmits(['update:filters', 'update:searchQuery', 'update:searchType', 'toggle-map', 'search'])

const localFilters = ref({ ...props.filters })
const localSearchQuery = ref(props.searchQuery)
const localSearchType = ref(props.searchType)

watch(
  () => props.searchType,
  (newVal) => {
    localSearchType.value = newVal
  },
  { deep: true },
)

watch(
  () => props.searchQuery,
  (newVal) => {
    localSearchQuery.value = newVal
  },
)

const updateSearchQuery = () => {
  emit('update:searchQuery', localSearchQuery.value)
}

const updateSearchType = () => {
  emit('update:searchType', localSearchType.value)
}
</script>

<style scoped>
.search-filter-container {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 20px;
  border-radius: 8px;
}

.route-filter {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  align-items: center;
}

.filter-group {
  display: flex;
  flex-direction: column;
}

.filter-group label {
  font-size: 12px;
  margin-bottom: 5px;
  color: #495057;
}

.filter-arrow {
  display: flex;
  align-items: center;
  font-size: 18px;
  color: #6c757d;
}

.search-input {
  width: 10rem;
}

.search-box {
  display: flex;
  gap: 5px;
  margin-left: auto;
  align-items: center; /* 수직 중앙 정렬 추가 */
}

/* 검색 입력창 높이 조정 */
.search-box .form-control-sm {
  height: calc(1.5em + 0.5rem + 2px); /* 부트스트랩 form-control-sm 기본 높이 */
  padding: 0.25rem 0.5rem; /* 부트스트랩 form-control-sm 기본 패딩 */
}

/* 검색 버튼 높이 조정 */
.search-btn {
  white-space: nowrap;
  height: calc(1.5em + 0.5rem + 2px); /* 부트스트랩 form-control-sm과 동일한 높이 */
  padding: 0.25rem 0.5rem; /* 부트스트랩 btn-sm과 동일한 패딩 */
  background-color: #fd7e14;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
  display: flex;
  align-items: center;
  font-size: 0.875rem; /* 부트스트랩 btn-sm과 동일한 폰트 크기 */
}

.search-btn:hover {
  background-color: #e8590c;
}

.map-toggle {
  display: flex;
  align-items: center;
}

.map-toggle-btn {
  padding: 0.25rem 0.5rem; /* 부트스트랩 btn-sm과 동일한 패딩 */
  height: calc(1.5em + 0.5rem + 2px); /* 부트스트랩 form-control-sm과 동일한 높이 */
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 0.875rem; /* 부트스트랩 btn-sm과 동일한 폰트 크기 */
}

.map-toggle-btn:hover {
  background-color: #388e3c;
}

@media (max-width: 768px) {
  .search-filter-container {
    flex-direction: column;
  }

  .route-filter {
    width: 100%;
  }

  .search-box {
    width: 100%;
    margin-left: 0;
  }

  .map-toggle {
    width: 100%;
    justify-content: center;
  }
}
</style>
