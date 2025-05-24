<template>
  <section class="feature-highlight-section">
    <div class="container">
      <div class="row align-items-center">
        <div class="col-lg-6 mb-4 mb-lg-0">
          <img
            src="/Journey-amico.png"
            alt="모바일 앱"
            class="feature-image"
          />
        </div>
        <div class="col-lg-6">
          <h2 class="feature-title">
            쉽고 직관적인<br />
            여행 경로 플랫폼
          </h2>
          <p class="feature-description">
            Moving Logs는 복잡한 여행 계획을 간편하게 시각화하고 관리할 수 있도록 돕습니다.
            자신만의 여행 루트를 설계해보세요.
          </p>
          <ul class="feature-list">
            <li><i class="bi bi-check-circle"></i> 지역 맞춤 명소 추천</li>
            <li><i class="bi bi-check-circle"></i> 경로 시각화</li>
            <li><i class="bi bi-check-circle"></i> 나만의 여행 기록 저장 및 공유</li>
          </ul>
        </div>
      </div>
    </div>
  </section>
</template>


<script setup>

</script>

<style scoped>
.feature-highlight-section {
  background-color: #f8f9fa;
  padding: 60px 0;
}

.feature-image {
  width: 50%;
  display: block;
  margin: 0 auto;
}

.feature-title {
  font-size: 2.2rem;
  font-weight: 700;
  margin-bottom: 20px;
  color: #343a40;
  line-height: 1.3;
}

.feature-description {
  font-size: 1.1rem;
  color: #495057;
  margin-bottom: 25px;
  line-height: 1.6;
}

.feature-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.feature-list li {
  margin-bottom: 10px;
  font-size: 1rem;
  color: #495057;
  display: flex;
  align-items: center;
}

.feature-list i {
  color: var(--primary-color);
  margin-right: 8px;
  font-size: 18px;
}

@media (max-width: 991.98px) {
  .feature-title {
    font-size: 1.8rem;
  }
}
</style>
