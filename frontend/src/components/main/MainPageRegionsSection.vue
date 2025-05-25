<template>
  <section class="regions-section">
    <div class="container">
      <div class="section-header">
        <h2 class="section-title">지역별 여행 정보</h2>
      </div>

      <div class="regions-grid">
        <div class="region-item" v-for="region in regions" :key="region.sidoCode">
          <div class="region-card" @click="moveToMap(region.sidoCode)">
            <img :src="region.image" :alt="region.name" class="region-img" />
            <div class="region-overlay">
              <h3 class="region-name">{{ region.name }}</h3>
              <p class="region-count">{{ region.count }}개의 관광지</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter();

function moveToMap(sidoCode) {
  router.push({
    name: 'PlanningPage',
    query: {
      sidoCode: sidoCode
    }
  })
}

// 지역 데이터
const regions = ref([
  {
    "sidoCode": 1,
    "name": "서울",
    "image": "/seoul.jpg",
    "count": 7839
  },
  {
    "sidoCode": 2,
    "name": "인천",
    "image": "/incheon.jpg",
    "count": 2052
  },
  {
    "sidoCode": 3,
    "name": "대전",
    "image": "/daejeon.jpg",
    "count": 753
  },
  {
    "sidoCode": 4,
    "name": "대구",
    "image": "/daegu.jpg",
    "count": 1167
  },
  {
    "sidoCode": 5,
    "name": "광주",
    "image": "/gwangju.jpg",
    "count": 606
  },
  {
    "sidoCode": 6,
    "name": "부산",
    "image": "/busan.jpg",
    "count": 2088
  },
  {
    "sidoCode": 7,
    "name": "울산",
    "image": "/ulsan.jpg",
    "count": 656
  },
  {
    "sidoCode": 39,
    "name": "제주도",
    "image": "/jeju.jpg",
    "count": 2335
  }
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
