<template>
  <section class="app-promo-section">
    <div class="container">
      <div class="row align-items-center">
        <div class="col-lg-6 mb-4 mb-lg-0">
          <img
            src="https://via.placeholder.com/600x400?text=Mobile+App"
            alt="모바일 앱"
            class="app-image"
          />
        </div>
        <div class="col-lg-6">
          <h2 class="app-title">언제 어디서나<br />여행 계획을 세우세요</h2>
          <p class="app-description">
            여행로드 모바일 앱으로 오프라인에서도 여행 경로를 확인하고 실시간으로 여행 정보를
            공유하세요. GPS 기반 내비게이션과 주변 명소 추천 기능으로 더 편리한 여행을 즐겨보세요.
          </p>
          <div class="app-buttons">
            <a href="#" class="app-button">
              <i class="bi bi-apple"></i>
              <div class="app-button-text">
                <span class="app-button-small">Download on the</span>
                <span class="app-button-large">App Store</span>
              </div>
            </a>
            <a href="#" class="app-button">
              <i class="bi bi-google-play"></i>
              <div class="app-button-text">
                <span class="app-button-small">GET IT ON</span>
                <span class="app-button-large">Google Play</span>
              </div>
            </a>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>

</script>

<style scoped>
/* 앱 소개 섹션 */
.app-promo-section {
  background-color: #f8f9fa;
}

.app-image {
  width: 100%;
  border-radius: 12px;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
}

.app-title {
  font-size: 2.2rem;
  font-weight: 700;
  margin-bottom: 20px;
  color: #333;
  line-height: 1.2;
}

.app-description {
  font-size: 1.1rem;
  color: #495057;
  margin-bottom: 30px;
  line-height: 1.6;
}

.app-buttons {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

.app-button {
  display: flex;
  align-items: center;
  background-color: #333;
  color: white;
  text-decoration: none;
  padding: 10px 20px;
  border-radius: 8px;
  transition: background-color 0.2s;
}

.app-button:hover {
  background-color: #000;
  color: white;
}

.app-button i {
  font-size: 24px;
  margin-right: 10px;
}

.app-button-text {
  display: flex;
  flex-direction: column;
}

.app-button-small {
  font-size: 10px;
  opacity: 0.8;
}

.app-button-large {
  font-size: 16px;
  font-weight: 500;
}

section {
  padding: 60px 0;
}

.view-all-link i {
  margin-left: 6px;
}

/* 반응형 스타일 */
@media (max-width: 991.98px) {
  .app-title {
    font-size: 1.8rem;
  }
}

@media (max-width: 767.98px) {
  section {
    padding: 40px 0;
  }
}
</style>
