<template>
  <section class="popular-routes-section">
    <div class="container">
      <div class="section-header">
        <h2 class="section-title">인기 여행 이야기</h2>
        <router-link to="/routes" class="view-all-link">
          전체보기 <i class="bi bi-arrow-right"></i>
        </router-link>
      </div>

      <div class="row">
        <div class="col-md-6 col-lg-4 mb-4" v-for="route in popularRoutes" :key="route.id">
          <div class="route-card">
            <div class="route-thumbnail">
              <img :src="route.thumbnail" :alt="route.title" class="route-img" />
              <div class="route-overlay">
                <span class="route-region">{{ route.region }}</span>
                <span class="route-likes">
                    <i class="bi bi-heart-fill"></i> {{ route.likes }}
                  </span>
              </div>
            </div>
            <div class="route-content">
              <h3 class="route-title">{{ route.title }}</h3>
              <div class="route-info">
                <div class="route-path">
                  <span class="route-point">{{ route.departure }}</span>
                  <i class="bi bi-arrow-right"></i>
                  <span v-if="route.waypoints.length > 0" class="route-waypoints">
                      {{ route.waypoints.length }}개 경유지
                    </span>
                  <i v-if="route.waypoints.length > 0" class="bi bi-arrow-right"></i>
                  <span class="route-point">{{ route.destination }}</span>
                </div>
                <div class="route-meta">
                    <span class="route-duration">
                      <i class="bi bi-calendar3"></i> {{ route.duration }}
                    </span>
                  <span class="route-author">
                      <img
                        :src="route.author.avatar"
                        :alt="route.author.name"
                        class="author-avatar"
                      />
                      {{ route.author.name }}
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
import { ref } from 'vue'

// 인기 여행 경로 데이터
const popularRoutes = ref([
  {
    id: 1,
    title: '제주도 3박 4일 동쪽 여행 코스',
    region: '제주',
    departure: '제주공항',
    destination: '제주공항',
    waypoints: ['성산일출봉', '우도', '비자림', '함덕해수욕장'],
    duration: '3박 4일',
    likes: 245,
    thumbnail: 'https://via.placeholder.com/400x300?text=Jeju',
    author: {
      name: '제주사랑',
      avatar: 'https://via.placeholder.com/40?text=User1',
    },
  },
  {
    id: 2,
    title: '부산 해안도로 드라이브 코스',
    region: '부산',
    departure: '해운대',
    destination: '태종대',
    waypoints: ['광안리', '송도', '영도'],
    duration: '1박 2일',
    likes: 189,
    thumbnail: 'https://via.placeholder.com/400x300?text=Busan',
    author: {
      name: '바다여행자',
      avatar: 'https://via.placeholder.com/40?text=User2',
    },
  },
  {
    id: 3,
    title: '서울 근교 당일치기 힐링 코스',
    region: '경기',
    departure: '서울역',
    destination: '서울역',
    waypoints: ['남한산성', '광주 화담숲', '이천 도자기마을'],
    duration: '당일',
    likes: 156,
    thumbnail: 'https://via.placeholder.com/400x300?text=Seoul',
    author: {
      name: '주말탐험가',
      avatar: 'https://via.placeholder.com/40?text=User3',
    },
  },
])
</script>

<style scoped>
/* 인기 여행 경로 섹션 */
.route-card {
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  height: 100%;
}

.route-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.route-thumbnail {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.route-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.route-card:hover .route-img {
  transform: scale(1.05);
}

.route-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  padding: 12px;
  display: flex;
  justify-content: space-between;
}

.route-region {
  background-color: var(--primary-color);
  color: white;
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.route-likes {
  background-color: rgba(0, 0, 0, 0.6);
  color: white;
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.route-content {
  padding: 20px;
}

.route-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 12px;
  color: #333;
  line-height: 1.3;
}

.route-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.route-path {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 6px;
  font-size: 14px;
  color: #495057;
}

.route-point {
  font-weight: 500;
}

.route-waypoints {
  color: #6c757d;
  font-size: 12px;
}

.route-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  color: #6c757d;
}

.route-duration {
  display: flex;
  align-items: center;
  gap: 4px;
}

.route-author {
  display: flex;
  align-items: center;
  gap: 6px;
}

.author-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  object-fit: cover;
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
