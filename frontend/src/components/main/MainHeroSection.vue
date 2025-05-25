<template>
  <section class="hero-section">
    <transition name="fade">
      <div
        class="hero-background"
        :key="currentIndex"
        :style="{ backgroundImage: `url('${backgroundImages[currentIndex]}')` }"
      ></div>
    </transition>
    <div class="background-overlay"></div>

    <div class="container content-layer">
      <div class="row align-items-center">
        <div class="col-lg-6 text-container">
          <h1 class="hero-title">나만의 여행 경로를<br />쉽게 계획하고 공유하세요</h1>
          <p class="hero-subtitle">Moving Logs와 함께 특별한 여행 경험을 만들어보세요</p>
          <div class="hero-buttons">
            <button class="btn btn-primary btn-lg me-3">
              <router-link to="/planning" class="nav-link" active-class="active"
                ><i class="bi bi-map"></i> 경로 만들기</router-link
              >
            </button>
            <button class="btn btn-outline-light btn-lg">
              <router-link to="/service-intro" class="nav-link" active-class="active">
                <i class="bi bi-info-circle"></i> 서비스 소개
              </router-link>
            </button>
          </div>
        </div>
        <div class="col-lg-6 d-none d-lg-block">
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const backgroundImages = [
  'https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?q=80&w=2670&auto=format&fit=crop',
  'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?q=80&w=2670&auto=format&fit=crop',
  'https://images.unsplash.com/photo-1506744038136-46273834b3fb?q=80&w=2670&auto=format&fit=crop',
  'https://images.unsplash.com/photo-1488646953014-85cb44e25828?q=80&w=2670&auto=format&fit=crop'
];

const currentIndex = ref(0);
const nextIndex = ref(1);

onMounted(() => {
  setInterval(() => {
    nextIndex.value = (currentIndex.value + 1) % backgroundImages.length;
    setTimeout(() => {
      currentIndex.value = nextIndex.value;
    }, 1000);
  }, 5000);
});
</script>


<style scoped>
.hero-section {
  position: relative;
  overflow: hidden;
  padding: 180px 0;
  margin-bottom: 60px;
  color: white;
}

.hero-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
  opacity: 1;
  z-index: 1;
}

.content-layer {
  position: relative;
  z-index: 4;
}

.background-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 2;
}

.fade-enter-active, .fade-leave-active {
  transition: opacity 1s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
.fade-enter-to, .fade-leave-from {
  opacity: 1;
}

.hero-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 20px;
  line-height: 1.2;
}

.hero-subtitle {
  font-size: 1.2rem;
  margin-bottom: 30px;
  opacity: 0.9;
}

.hero-buttons {
  margin-bottom: 20px;
}

section {
  padding: 60px 0;
}

.view-all-link i {
  margin-left: 6px;
}

/* 반응형 스타일 */
@media (max-width: 991.98px) {
  .hero-title {
    font-size: 2rem;
  }
}

@media (max-width: 767.98px) {
  .hero-section {
    padding: 60px 0;
    margin-bottom: 40px;
  }

  section {
    padding: 40px 0;
  }
}
</style>
