<template>
  <section class="popular-routes-section">
    <div class="container">
      <div class="section-header">
        <h2 class="section-title">인기 여행 이야기</h2>
        <router-link to="/tripost/board" class="view-all-link">
          전체보기 <i class="bi bi-arrow-right"></i>
        </router-link>
      </div>

      <div class="row">
        <div class="col-md-6 col-lg-4 mb-4" v-for="hotPost in hotPosts" :key="hotPost.id">
          <div class="hotPost-card" @click="moveToBoard(hotPost.id)">
            <div class="hotPost-thumbnail">
              <img :src="serverUrl + hotPost.imageUrl" :alt="hotPost.title" class="hotPost-img" />
              <div class="hotPost-overlay">
                <span class="hotPost-likes">
                    <i class="bi bi-heart-fill"></i> {{ hotPost.likeCount }}
                  </span>
              </div>
            </div>
            <div class="hotPost-content">
              <h3 class="hotPost-title">{{ hotPost.title }}</h3>
              <div class="hotPost-info">
                <div class="hotPost-path">
                  <span class="hotPost-point">{{ hotPost.startWaypoint }}</span>
                  <i class="bi bi-arrow-right"></i>
                  <span v-if="hotPost.waypointLength - 2 > 0" class="hotPost-waypoints">
                      {{ hotPost.waypointLength - 2 }}개 경유지
                    </span>
                  <i v-if="hotPost.waypointLength - 2 > 0" class="bi bi-arrow-right"></i>
                  <span class="hotPost-point">{{ hotPost.endWaypoint }}</span>
                </div>
                <div class="hotPost-meta">
                  <span class="hotPost-author">
                      {{ hotPost.author }}
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

const serverUrl = import.meta.env.VITE_API_SERVER_URL
const hotPosts = ref(null)
const router = useRouter();

function moveToBoard(id) {
  router.push(`/tripost/${id}`)
}

onMounted(async () => {
  await loadHotPosts()
})

async function loadHotPosts() {
  const response = await api.get(`/api/v1/triposts/hot`, {
    params: {
      size: 3,
    },
  })

  hotPosts.value = response.data
  console.log(response.data)
}
</script>

<style scoped>
/* 인기 여행 경로 섹션 */
.hotPost-card {
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  height: 100%;
}

.hotPost-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
  cursor: pointer;
}

.hotPost-thumbnail {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.hotPost-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.hotPost-card:hover .hotPost-img {
  transform: scale(1.05);
}

.hotPost-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  padding: 12px;
  display: flex;
  flex-direction: row-reverse;
}

.hotPost-likes {
  background-color: rgba(0, 0, 0, 0.6);
  color: white;
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.hotPost-content {
  padding: 20px;
}

.hotPost-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 12px;
  color: #333;
  line-height: 1.3;
}

.hotPost-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.hotPost-path {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 6px;
  font-size: 14px;
  color: #495057;
}

.hotPost-point {
  font-weight: 500;
}

.hotPost-waypoints {
  color: #6c757d;
  font-size: 12px;
}

.hotPost-meta {
  display: flex;
  flex-direction: row-reverse;
  align-items: center;
  font-size: 13px;
  color: #6c757d;
}

.hotPost-author {
  display: flex;
  align-items: center;
  gap: 6px;
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
