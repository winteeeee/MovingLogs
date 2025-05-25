<template>
  <div class="post-header">
    <h1 class="post-title">{{ tripost.title }}</h1>
    <div class="post-meta">
      <div class="author-info">
<!--        <div class="author-avatar">-->
<!--          <img-->
<!--            :src="post.author.avatar || 'https://via.placeholder.com/40'"-->
<!--            :alt="post.author.name"-->
<!--          />-->
<!--        </div>-->
        <div class="author-name">{{ tripost.name }}</div>
      </div>
      <div class="post-date">작성 날짜 : {{ formatDate(tripost.createdAt) }}</div>
      <div class="post-stats">
        <span class="stat-item"> <i class="bi bi-eye"></i> 조회수 : {{ tripost.viewCount }} </span>
        <span class="stat-item">
          <i class="bi bi-chat"></i> 댓글수 : {{ tripost.commentCount }}
        </span>
        <span class="stat-item"> <i class="bi bi-heart"></i> 좋아요 : {{ tripost.likeCount }} </span>
      </div>
    </div>
    <div class="post-tags">
      <span
        v-for="(tag, index) in tripost.tags"
        :key="index"
        class="tag-item"
        @click="$emit('tag-click', tag)"
      >
        #{{ tag }}
      </span>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'

const props = defineProps({
  tripost: {
    type: Object,
    required: true,
  },
})

defineEmits(['tag-click'])

function getCategoryName(type) {
  const categories = {
    free: '자유게시판',
    notice: '공지사항',
    tips: '여행 팁',
    food: '현지 맛집',
    accommodation: '숙소 추천',
  }
  return categories[type] || '기타'
}

function formatDate(date) {
  if (!date) return ''

  const d = new Date(date)
  const now = new Date()
  const diff = Math.floor((now - d) / 1000) // 초 단위 차이

  if (diff < 60) {
    return '방금 전'
  } else if (diff < 3600) {
    return `${Math.floor(diff / 60)}분 전`
  } else if (diff < 86400) {
    return `${Math.floor(diff / 3600)}시간 전`
  } else if (diff < 604800) {
    return `${Math.floor(diff / 86400)}일 전`
  } else {
    return `${d.getFullYear()}.${String(d.getMonth() + 1).padStart(2, '0')}.${String(d.getDate()).padStart(2, '0')}`
  }
}
</script>

<style scoped>
.post-header {
  margin-bottom: 30px;
}

.post-category {
  display: inline-block;
  padding: 5px 10px;
  background-color: #f1f3f5;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  color: #495057;
  margin-bottom: 10px;
}

.post-title {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 15px;
  color: #333;
  line-height: 1.3;
}

.post-meta {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e9ecef;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.author-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
}

.author-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.author-name {
  font-weight: 500;
  color: #495057;
}

.post-date {
  color: #6c757d;
  font-size: 14px;
}

.post-stats {
  display: flex;
  gap: 15px;
  margin-left: auto;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #6c757d;
  font-size: 14px;
}

.post-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 15px;
}

.tag-item {
  padding: 5px 10px;
  background-color: #e9ecef;
  border-radius: 4px;
  font-size: 14px;
  color: #495057;
  cursor: pointer;
  transition: background-color 0.2s;
}

.tag-item:hover {
  background-color: #dee2e6;
}

@media (max-width: 768px) {
  .post-title {
    font-size: 24px;
  }

  .post-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .post-stats {
    margin-left: 0;
  }
}
</style>
