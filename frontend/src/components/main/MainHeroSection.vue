<template>
  <section class="hero-section">
    <div class="container">
      <div class="row align-items-center">
        <div class="col-lg-6">
          <h1 class="hero-title">나만의 여행 경로를<br />쉽게 계획하고 공유하세요</h1>
          <p class="hero-subtitle">여행로드와 함께 특별한 여행 경험을 만들어보세요</p>
          <div class="hero-buttons">
            <button class="btn btn-primary btn-lg me-3">
              <i class="bi bi-map"></i> 경로 만들기
            </button>
            <button class="btn btn-outline-light btn-lg">
              <i class="bi bi-info-circle"></i> 서비스 소개
            </button>
          </div>
        </div>
        <div class="col-lg-6 d-none d-lg-block">
          <img
            src="https://via.placeholder.com/600x400?text=Travel+Map"
            alt="여행 지도"
            class="hero-image"
          />
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
</script>

<style scoped>
.hero-section {
  background: linear-gradient(135deg, var(--primary-color) 0%, #e8590c 100%);
  color: white;
  padding: 80px 0;
  margin-bottom: 60px;
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

.hero-image {
  width: 100%;
  border-radius: 12px;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
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
