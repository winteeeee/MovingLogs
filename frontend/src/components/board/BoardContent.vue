<template>
  <div class="board-content">
    <div v-if="loading" class="loading-spinner">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">로딩중...</span>
      </div>
    </div>

    <template v-else>
      <div v-if="filteredPosts.length === 0" class="no-posts">
        <p>검색 결과가 없습니다.</p>
      </div>

      <div v-else class="post-list">
        <BoardPostItem
          v-for="post in filteredPosts"
          :key="post.id"
          :post="post"
          @view-post="$emit('view-post', post)"
          @preview-route="$emit('preview-route', post.route)"
        />
      </div>
    </template>

    <div class="pagination">
      <button
        :disabled="currentPage === 1"
        @click="$emit('change-page', currentPage - 1)"
        class="page-btn"
      >
        이전
      </button>
      <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
      <button
        :disabled="currentPage === totalPages"
        @click="$emit('change-page', currentPage + 1)"
        class="page-btn"
      >
        다음
      </button>
    </div>

    <div class="board-actions">
      <button
        v-if="activeTab !== 'notice' || isAdmin"
        class="write-btn"
        @click="$emit('write-post')"
      >
        <i class="bi bi-pencil"></i> 글쓰기
      </button>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'
import BoardPostItem from './BoardPostItem.vue'

defineProps({
  loading: {
    type: Boolean,
    required: true,
  },
  filteredPosts: {
    type: Array,
    required: true,
  },
  currentPage: {
    type: Number,
    required: true,
  },
  totalPages: {
    type: Number,
    required: true,
  },
  activeTab: {
    type: String,
    required: true,
  },
  isAdmin: {
    type: Boolean,
    required: true,
  },
})

defineEmits(['view-post', 'change-page', 'write-post', 'preview-route'])
</script>

<style scoped>
.loading-spinner {
  display: flex;
  justify-content: center;
  padding: 50px 0;
}

.no-posts {
  text-align: center;
  padding: 50px 0;
  color: #6c757d;
}

.post-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  margin: 20px 0;
}

.page-btn {
  padding: 8px 15px;
  background-color: #f8f9fa;
  border: 1px solid #ced4da;
  border-radius: 4px;
  cursor: pointer;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  font-size: 14px;
  color: #6c757d;
}

.board-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.write-btn {
  padding: 10px 20px;
  background-color: #fd7e14;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 5px;
}

.write-btn:hover {
  background-color: #e8590c;
}
</style>
