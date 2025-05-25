<template>
  <div class="post-item" @click="$emit('view-tripost')">
    <div class="post-header">
      <h3 class="post-title">{{ tripost.title }} <span class="comment-count">({{ tripost.commentCount }})</span> </h3>
      <div class="post-meta">
        <span class="post-author"> 글쓴이 : {{ tripost.name }}</span>
        <span class="post-date"> 작성 날짜 : {{ formatDate(tripost.createdAt) }}</span>
        <span class="post-views"><i class="bi bi-eye"></i> {{ tripost.viewCount }}</span>
        <span class="post-likes"><i class="bi bi-heart"></i> {{ tripost.likeCount }}</span>
      </div>
    </div>

    <div class="post-preview">
      <p>{{ truncateText(tripost.description, 150) }}</p>
    </div>

    <div class="post-route">
      <div class="ellipsis">
        <i class="bi bi-geo-alt-fill me-1"></i>
        <span>{{ tripost.waypoints.map(e=>e.attractionName).join("&nbsp;&nbsp;→&nbsp;&nbsp;") }} </span>
      </div>
      <button class="preview-route-btn" @click.stop="$emit('preview-route')">경로 미리보기</button>
    </div>

    <div v-if="tripost.images && tripost.images.length > 0" class="post-images">
      <img
        v-for="(image, idx) in tripost.images.slice(0, 3)"
        :key="idx"
        :src="serverUrl + image.thumbnailUrl"
      />
      <div v-if="tripost.images.length > 3" class="more-images">+{{ tripost.images.length - 3 }}</div>
    </div>

    <div class="post-tags">
      <span v-for="(tag, idx) in tripost.tags" :key="idx" class="tag"> #{{ tag }} </span>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'
const serverUrl = import.meta.env.VITE_API_SERVER_URL

defineProps({
  tripost: {
    type: Object,
    required: true,
  },
})

defineEmits(['view-post', 'preview-route'])

const truncateText = (text, maxLength) => {
  if (!text || text.length <= maxLength) return text
  return text.substring(0, maxLength) + '...'
}

const formatDate = (date) => {
  return new Date(date).toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  })
}
</script>

<style scoped>
.post-item {
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
}

.post-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 10px;
  flex-wrap: wrap;
}

.post-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
  flex: 1;
}

.comment-count {
  font-size: 14px;
  font-weight: bold;
  color: #999;
}

.ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.post-meta {
  display: flex;
  gap: 15px;
  font-size: 14px;
  color: #6c757d;
}

.post-preview {
  margin-bottom: 15px;
  color: #495057;
  line-height: 1.5;
}

.post-route {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 15px;
  padding: 8px 12px;
  background-color: #f8f9fa;
  border-radius: 4px;
  font-size: 14px;
}

.preview-route-btn {
  min-width: fit-content;
  margin-left: auto;
  padding: 5px 10px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
}

.preview-route-btn:hover {
  background-color: #388e3c;
}

.post-images {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
  position: relative;
}

.post-images img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
}

.more-images {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 80px;
  height: 80px;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  border-radius: 4px;
  font-size: 14px;
}

.post-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.tag {
  padding: 4px 8px;
  background-color: #e9ecef;
  border-radius: 4px;
  font-size: 12px;
  color: #495057;
}

@media (max-width: 768px) {
  .post-header {
    flex-direction: column;
  }

  .post-meta {
    margin-top: 5px;
    flex-wrap: wrap;
  }
}
</style>
