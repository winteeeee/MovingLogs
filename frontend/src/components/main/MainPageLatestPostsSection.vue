<template>
  <section class="latest-posts-section">
    <div class="container">
      <div class="section-header">
        <h2 class="section-title">최신 여행 이야기</h2>
        <router-link to="/tripost/board" class="view-all-link">
          전체보기 <i class="bi bi-arrow-right"></i>
        </router-link>
      </div>

      <div class="row">
        <div class="col-md-6 col-lg-3 mb-4" v-for="post in latestPosts" :key="post.id">
          <div class="post-card" @click="moveToBoard(post.id)">
            <div class="post-thumbnail">
              <img :src="serverUrl + post.imageUrl" :alt="post.title" class="post-img" />
            </div>
            <div class="post-content">
              <h3 class="post-title">{{ post.title }}</h3>
              <p class="post-excerpt">{{ truncateContent(post.description) }}</p>
              <div class="post-meta">
                <span class="post-date">{{ formatDate(post.createdAt) }}</span>
                <div class="post-stats">
                  <span class="stat-item"> <i class="bi bi-eye"></i> {{ post.viewCount }} </span>
                  <span class="stat-item">
                    <i class="bi bi-chat"></i> {{ post.commentCount }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api/axios.js'
import { useRouter } from 'vue-router'


// 최신 게시글 데이터
const serverUrl = import.meta.env.VITE_API_SERVER_URL
const latestPosts = ref(null)
const router = useRouter();

function moveToBoard(id) {
  router.push(`/tripost/${id}`)
}

onMounted(async () => {
  await loadLatestPosts()
})

async function loadLatestPosts() {
  const response = await api.get(`/api/v1/triposts/latest`, {
    params: {
      size: 4,
    },
  })

  latestPosts.value = response.data
  console.log(response.data)
}

function truncateContent(content) {
  if (!content) return ''
  if (content.length <= 60) return content
  return content.substring(0, 60) + '...'
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
/* 최신 게시글 섹션 */
.latest-posts-section {
  background-color: #f8f9fa;
}

.post-card {
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.post-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
  cursor: pointer;
}

.post-thumbnail {
  position: relative;
  height: 160px;
  overflow: hidden;
}

.post-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.post-card:hover .post-img {
  transform: scale(1.05);
}

.post-category {
  position: absolute;
  bottom: 12px;
  left: 12px;
  background-color: rgba(0, 0, 0, 0.6);
  color: white;
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.post-content {
  padding: 20px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.post-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 10px;
  color: #333;
  line-height: 1.3;
}

.post-excerpt {
  font-size: 14px;
  color: #6c757d;
  line-height: 1.5;
  margin-bottom: 15px;
  flex: 1;
}

.post-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #6c757d;
}

.post-stats {
  display: flex;
  gap: 10px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

section {
  padding: 60px 0;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.section-title {
  font-size: 1.8rem;
  font-weight: 700;
  color: #333;
  position: relative;
  padding-left: 16px;
}

.section-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 8px;
  height: 24px;
  width: 4px;
  background-color: var(--primary-color);
  border-radius: 2px;
}

.view-all-link {
  display: flex;
  align-items: center;
  color: var(--primary-color);
  font-weight: 500;
  text-decoration: none;
  transition: color 0.2s;
}

.view-all-link:hover {
  color: var(--primary-hover);
}

.view-all-link i {
  margin-left: 6px;
}

@media (max-width: 767.98px) {
  section {
    padding: 40px 0;
  }

  .section-title {
    font-size: 1.5rem;
  }
}
</style>
