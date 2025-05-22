<template>
  <section class="regions-section">
    <div class="container">
      <div class="section-header">
        <h2 class="section-title">지역별 여행 정보</h2>
        <router-link to="/regions" class="view-all-link">
          전체보기 <i class="bi bi-arrow-right"></i>
        </router-link>
      </div>

      <div class="regions-grid">
        <div class="region-item" v-for="region in regions" :key="region.id">
          <div class="region-card">
            <img :src="region.image" :alt="region.name" class="region-img" />
            <div class="region-overlay">
              <h3 class="region-name">{{ region.name }}</h3>
              <p class="region-count">{{ region.postCount }}개의 여행 정보</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'

// 지역 데이터
const regions = ref([
  {
    id: 1,
    name: '서울',
    image: 'https://via.placeholder.com/300x200?text=Seoul',
    postCount: 256,
  },
  {
    id: 2,
    name: '부산',
    image: 'https://via.placeholder.com/300x200?text=Busan',
    postCount: 189,
  },
  {
    id: 3,
    name: '제주',
    image: 'https://via.placeholder.com/300x200?text=Jeju',
    postCount: 312,
  },
  {
    id: 4,
    name: '강원',
    image: 'https://via.placeholder.com/300x200?text=Gangwon',
    postCount: 145,
  },
  {
    id: 5,
    name: '경주',
    image: 'https://via.placeholder.com/300x200?text=Gyeongju',
    postCount: 98,
  },
  {
    id: 6,
    name: '전주',
    image: 'https://via.placeholder.com/300x200?text=Jeonju',
    postCount: 87,
  },
])
</script>

<style scoped>
/* 지역별 여행 정보 섹션 */
.regions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.region-card {
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  height: 180px;
  cursor: pointer;
  transition: transform 0.2s;
}

.region-card:hover {
  transform: scale(1.03);
}

.region-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.region-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20px;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.8) 0%, rgba(0, 0, 0, 0) 100%);
  color: white;
}

.region-name {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 4px;
}

.region-count {
  font-size: 14px;
  opacity: 0.9;
  margin: 0;
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

  .regions-grid {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  }

  .region-card {
    height: 150px;
  }

  .region-name {
    font-size: 18px;
  }
}
</style>
