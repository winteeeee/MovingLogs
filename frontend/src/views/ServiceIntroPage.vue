<template>
  <div class="service-intro-page">
    <!-- 히어로 섹션 -->
    <section class="hero-section">
      <div class="hero-content">
        <div class="hero-text">
          <h1 class="hero-title">
            <span class="brand-name">Moving Logs</span>
            <span class="hero-subtitle">당신의 여행을 기록하고 공유하세요</span>
          </h1>
          <p class="hero-description">
            여행 계획을 세우고, 나만의 여행 일정을 간편하게 공유해보세요.
            다른 사람들의 여행 코스를 참고하며 더 멋진 여행을 준비할 수 있어요!
          </p>
        </div>
        <div class="hero-visual">
          <div class="hero-image">
            <div class="travel-card-demo">
              <div class="demo-card">
                <div class="demo-header">
                  <div class="demo-title">제주도 3박 4일</div>
                  <div class="demo-likes">❤️ 1,234</div>
                </div>
                <div class="demo-spots">
                  <div class="demo-spot">📍 성산일출봉</div>
                  <div class="demo-spot">📍 한라산</div>
                  <div class="demo-spot">📍 우도</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 주요 기능 섹션 -->
    <section class="features-section">
      <div class="section-container">
        <h2 class="section-title">왜 Moving Logs를 선택해야 할까요?</h2>
        <div class="features-grid">
          <div class="feature-card">
            <div class="feature-icon">📋</div>
            <h3 class="feature-title">체계적인 여행 계획</h3>
            <p class="feature-description">
              체계적인 계획으로 시간과 비용을 절약하고 더 알찬 여행을 만들어보세요.
            </p>
          </div>

          <div class="feature-card">
            <div class="feature-icon">🤝</div>
            <h3 class="feature-title">계획 공유 & 참고</h3>
            <p class="feature-description">
              여행 계획을 다른 사람들과 공유하고, 많은 추천을 받은 계획을 참고할 수도 있어요!
              경험자들의 노하우를 활용해보세요.
            </p>
          </div>

          <div class="feature-card">
            <div class="feature-icon">🎯</div>
            <h3 class="feature-title">다양한 분야별 분류</h3>
            <p class="feature-description">
              관광지, 문화시설, 축제공연행사, 여행코스, 레포츠 등
              다양한 분야로 관광지를 분류하여 원하는 여행 스타일에 맞게 계획하세요.
            </p>
          </div>

          <div class="feature-card">
            <div class="feature-icon">🤖</div>
            <h3 class="feature-title">AI 기반 추천</h3>
            <p class="feature-description">
              AI 기반 관광지 추천 서비스로 당신의 취향과 여행 스타일에
              맞는 최적의 여행지를 추천받아보세요.
            </p>
          </div>

          <div class="feature-card">
            <div class="feature-icon">💬</div>
            <h3 class="feature-title">여행 커뮤니티 게시판</h3>
            <p class="feature-description">
              여행자들과 자유롭게 소통할 수 있는 게시판에서
              질문하고, 팁을 공유하며 여행 정보를 나눠보세요!
            </p>
          </div>

          <div class="feature-card">
            <div class="feature-icon">🗺️</div>
            <h3 class="feature-title">지도 기반 일정 구성</h3>
            <p class="feature-description">
              지도를 활용해 동선을 확인하며 일정을 구성하세요.
              효율적인 여행 루트를 한눈에 파악할 수 있어요!
            </p>
          </div>
        </div>
      </div>
    </section>

    <!-- 통계 섹션 -->
    <section class="stats-section">
      <div class="section-container">
        <h2 class="section-title">풍부한 관광지 데이터</h2>
        <p class="section-subtitle">전국 50,915개의 관광지 정보를 제공합니다</p>

        <div class="stats-content">
          <div class="stats-overview">
            <div class="stat-item">
              <div class="stat-number">50,915</div>
              <div class="stat-label">총 관광지</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">17</div>
              <div class="stat-label">시도</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">7</div>
              <div class="stat-label">카테고리</div>
            </div>
          </div>

          <!-- 지역별 관광지 분포 차트 -->
          <div class="chart-container">
            <h3 class="chart-title">지역별 관광지 분포</h3>
            <div class="chart-wrapper">
              <canvas ref="regionChart" class="region-chart"></canvas>
              <div class="chart-legend">
                <div class="legend-item" v-for="region in regionData" :key="region.name">
                  <div class="legend-color" :style="{ backgroundColor: region.color }"></div>
                  <span class="legend-label">{{ region.name }}</span>
                  <span class="legend-value">{{ region.value.toLocaleString() }}개</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 카테고리별 분류 -->
          <h3 class="chart-title">카테고리별 관광지 분포</h3>
          <div class="categories-grid">
            <div class="category-card" v-for="category in categories" :key="category.name">
              <div class="category-icon">{{ category.icon }}</div>
              <div class="category-name">{{ category.name }}</div>
              <div class="category-count">{{ category.count.toLocaleString() }}개</div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- AI 추천 섹션 -->
    <section class="ai-section">
      <div class="section-container">
        <div class="ai-content">
          <div class="ai-text">
            <h2 class="ai-title">AI가 골라주는 핵심 관광지 추천</h2>
            <p class="ai-description">
              지역과 관광지 유형을 설정하면,
              AI가 수많은 검색 결과 중 당신에게 딱 맞는 관광지를 추천하고 그 이유도 알려드린답니다!
            </p>
            <div class="ai-features">
              <div class="ai-feature">
                <span class="ai-feature-icon">📍</span>
                <span>지역 & 관광지 타입 기반 분석</span>
              </div>
              <div class="ai-feature">
                <span class="ai-feature-icon">🧠</span>
                <span>AI 핵심 추천 알고리즘</span>
              </div>
              <div class="ai-feature">
                <span class="ai-feature-icon">💡</span>
                <span>추천 이유 함께 제공</span>
              </div>
            </div>
          </div>
          <div class="ai-visual">
            <div class="ai-demo">
              <div class="ai-input">
                <div class="input-label">선택한 조건</div>
                <div class="input-tags">
                  <span class="tag">경상북도</span>
                  <span class="tag">구미시</span>
                  <span class="tag">관광지</span>
                </div>
              </div>
              <div class="ai-arrow">↓</div>
              <div class="ai-output">
                <div class="output-label">AI 추천 관광지</div>
                <div class="recommendation-list">
                  <div class="recommendation-item">
                    🏞️ 금오산도립공원
                    <div class="recommendation-reason">자연 경관이 뛰어나고 도심에서 가까워 당일치기 여행에 좋아요.</div>
                  </div>
                  <div class="recommendation-item">
                    🛕 선산읍성
                    <div class="recommendation-reason">역사적 유산과 한적한 산책로가 있어 조용한 여행을 원할 때 적합해요.</div>
                  </div>
                  <div class="recommendation-item">
                    🌉 구미 금오지
                    <div class="recommendation-reason">호수 주변 산책로와 야경이 아름다워 힐링 여행지로 추천돼요.</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA 섹션 -->
    <section class="cta-section">
      <div class="section-container">
        <div class="cta-content">
          <h2 class="cta-title">지금 바로 여행 계획을 시작해보세요!</h2>
          <p class="cta-description">
            Moving Logs와 함께 완벽한 여행을 계획하고 소중한 추억을 만들어보세요.
          </p>
          <div class="cta-actions">
            <router-link to="/planning" class="nav-link" active-class="active">
              <button class="button button-primary button-large">
                <span class="icon-start"></span>
                여행계획 짜러가기
              </button>
            </router-link>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'

const regionChart = ref(null)

// 지역별 관광지 데이터
const regionData = ref([
    { name: '서울', value: 7839, color: '#4a6bdf' },
    { name: '인천', value: 2052, color: '#ff7f50' },
    { name: '대전', value: 753, color: '#ffa500' },
    { name: '대구', value: 1167, color: '#20b2aa' },
    { name: '광주', value: 606, color: '#9370db' },
    { name: '부산', value: 2088, color: '#ff69b4' },
    { name: '울산', value: 656, color: '#6495ed' },
    { name: '세종특별자치시', value: 207, color: '#cd5c5c' },
    { name: '경기도', value: 9603, color: '#228b22' },
    { name: '강원특별자치도', value: 5372, color: '#8b4513' },
    { name: '충청북도', value: 1941, color: '#ff4500' },
    { name: '충청남도', value: 2608, color: '#2e8b57' },
    { name: '경상북도', value: 3666, color: '#1e90ff' },
    { name: '경상남도', value: 4010, color: '#d2691e' },
    { name: '전북특별자치도', value: 2605, color: '#ff6347' },
    { name: '전라남도', value: 3407, color: '#6a5acd' },
    { name: '제주도', value: 2335, color: '#40e0d0' }
  ]
)

// 카테고리별 데이터
const categories = ref([
  { name: '관광지', icon: '🏞️', count: 29003 },
  { name: '문화시설', icon: '🏛️', count: 2657 },
  { name: '축제공연행사', icon: '🎉', count: 1688 },
  { name: '여행코스', icon: '🗺️', count: 1069 },
  { name: '레포츠', icon: '🏄‍♂️', count: 4570 },
  { name: '숙박', icon: '🏨', count: 3583 },
  { name: '쇼핑', icon: '🛍️', count: 15826 }
])

// 원 그래프 그리기
function drawPieChart() {
  if (!regionChart.value) return

  const canvas = regionChart.value
  const ctx = canvas.getContext('2d')
  const centerX = canvas.width / 2
  const centerY = canvas.height / 2
  const radius = Math.min(centerX, centerY) - 20

  // 캔버스 초기화
  ctx.clearRect(0, 0, canvas.width, canvas.height)

  // 총합 계산
  const total = regionData.value.reduce((sum, item) => sum + item.value, 0)

  let currentAngle = -Math.PI / 2 // 12시 방향부터 시작

  regionData.value.forEach((region, index) => {
    const sliceAngle = (region.value / total) * 2 * Math.PI

    // 파이 조각 그리기
    ctx.beginPath()
    ctx.moveTo(centerX, centerY)
    ctx.arc(centerX, centerY, radius, currentAngle, currentAngle + sliceAngle)
    ctx.closePath()
    ctx.fillStyle = region.color
    ctx.fill()

    // 테두리 그리기
    ctx.strokeStyle = '#ffffff'
    ctx.lineWidth = 2
    ctx.stroke()

    currentAngle += sliceAngle
  })
}

onMounted(() => {
  nextTick(() => {
    if (regionChart.value) {
      // 캔버스 크기 설정
      regionChart.value.width = 300
      regionChart.value.height = 300
      drawPieChart()
    }
  })
})
</script>

<style scoped>
/* 기본 스타일 */
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

.service-intro-page {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
  color: #333;
  line-height: 1.6;
}

.section-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}

/* 히어로 섹션 */
.hero-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 80px 0;
  min-height: 600px;
  display: flex;
  align-items: center;
}

.hero-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  align-items: center;
}

.brand-name {
  display: block;
  font-size: 3.5rem;
  font-weight: 800;
  margin-bottom: 16px;
  background: linear-gradient(45deg, #ffffff, #f0f9ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-subtitle {
  display: block;
  font-size: 1.5rem;
  font-weight: 400;
  opacity: 0.9;
}

.hero-description {
  font-size: 1.2rem;
  margin: 24px 0 32px;
  opacity: 0.9;
  line-height: 1.7;
}

.hero-actions {
  display: flex;
  gap: 16px;
}

.hero-visual {
  display: flex;
  justify-content: center;
  align-items: center;
}

.travel-card-demo {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 24px;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.demo-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  color: #333;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.demo-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.demo-title {
  font-weight: 600;
  font-size: 1.1rem;
}

.demo-likes {
  color: #e91e63;
  font-size: 0.9rem;
}

.demo-spots {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.demo-spot {
  font-size: 0.9rem;
  color: #666;
}

/* 기능 섹션 */
.features-section {
  padding: 80px 0;
  background-color: #f8f9fa;
}

.section-title {
  text-align: center;
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 16px;
  color: #333;
}

.section-subtitle {
  text-align: center;
  font-size: 1.2rem;
  color: #666;
  margin-bottom: 60px;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 32px;
  margin-top: 60px;
}

.feature-card {
  background: white;
  padding: 32px 24px;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.feature-icon {
  font-size: 3rem;
  margin-bottom: 20px;
}

.feature-title {
  font-size: 1.3rem;
  font-weight: 600;
  margin-bottom: 16px;
  color: #333;
}

.feature-description {
  color: #666;
  line-height: 1.7;
}

/* 통계 섹션 */
.stats-section {
  padding: 80px 0;
  background: white;
}

.stats-content {
  margin-top: 60px;
}

.stats-overview {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 32px;
  margin-bottom: 60px;
}

.stat-item {
  text-align: center;
  padding: 24px;
  background: #f8f9fa;
  border-radius: 12px;
}

.stat-number {
  font-size: 3rem;
  font-weight: 700;
  color: #4a6bdf;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 1.1rem;
  color: #666;
}

/* 차트 컨테이너 */
.chart-container {
  background: white;
  border-radius: 12px;
  padding: 32px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  margin-bottom: 40px;
}

.chart-title {
  text-align: center;
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 32px;
  color: #333;
}

.chart-wrapper {
  display: grid;
  grid-template-columns: 300px 1fr;
  gap: 40px;
  align-items: center;
}

.region-chart {
  border-radius: 50%;
}

.chart-legend {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.9rem;
}

.legend-color {
  width: 16px;
  height: 16px;
  border-radius: 50%;
}

.legend-label {
  flex: 1;
  color: #333;
}

.legend-value {
  color: #666;
  font-weight: 500;
}

/* 카테고리 그리드 */
.categories-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 24px;
}

.category-card {
  background: #f8f9fa;
  padding: 24px;
  border-radius: 12px;
  text-align: center;
  transition: transform 0.3s ease;
}

.category-card:hover {
  transform: translateY(-2px);
}

.category-icon {
  font-size: 2.5rem;
  margin-bottom: 12px;
}

.category-name {
  font-weight: 600;
  margin-bottom: 8px;
  color: #333;
}

.category-count {
  color: #4a6bdf;
  font-weight: 600;
}

/* AI 섹션 */
.ai-section {
  padding: 80px 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.ai-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  align-items: center;
}

.ai-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 24px;
}

.ai-description {
  font-size: 1.2rem;
  margin-bottom: 32px;
  opacity: 0.9;
  line-height: 1.7;
}

.ai-features {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.ai-feature {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 1.1rem;
}

.ai-feature-icon {
  font-size: 1.5rem;
}

.ai-demo {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 32px;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.ai-input,
.ai-output {
  background: rgba(255, 255, 255, 0.9);
  color: #333;
  padding: 20px;
  border-radius: 12px;
  margin-bottom: 16px;
}

.ai-output {
  margin-bottom: 0;
}

.input-label,
.output-label {
  font-weight: 600;
  margin-bottom: 12px;
}

.input-tags {
  display: flex;
  gap: 8px;
}

.tag {
  background: #4a6bdf;
  color: white;
  padding: 4px 12px;
  border-radius: 16px;
  font-size: 0.85rem;
}

.ai-arrow {
  text-align: center;
  font-size: 1.5rem;
  margin: 8px 0;
}

.recommendation-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.recommendation-item {
  font-size: 0.9rem;
  color: #666;
}

/* CTA 섹션 */
.cta-section {
  padding: 80px 0;
  background: #f8f9fa;
  text-align: center;
}

.cta-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 16px;
  color: #333;
}

.cta-description {
  font-size: 1.2rem;
  color: #666;
  margin-bottom: 32px;
}

/* 버튼 스타일 */
.button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 12px 24px;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
  outline: none;
  text-decoration: none;
}

.button-large {
  padding: 16px 32px;
  font-size: 1.1rem;
}

.button-primary {
  background: #4a6bdf;
  color: white;
}

.button-primary:hover {
  background: #3a5bc9;
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(74, 107, 223, 0.3);
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .hero-content,
  .ai-content {
    grid-template-columns: 1fr;
    gap: 40px;
    text-align: center;
  }

  .brand-name {
    font-size: 2.5rem;
  }

  .hero-subtitle {
    font-size: 1.2rem;
  }

  .section-title {
    font-size: 2rem;
  }

  .button {
    width: 100%;
    max-width: 300px;
  }

  .stats-overview {
    grid-template-columns: 1fr;
  }

  .chart-wrapper {
    grid-template-columns: 1fr;
    text-align: center;
  }

  .chart-legend {
    grid-template-columns: 1fr;
    max-width: 300px;
    margin: 0 auto;
  }

  .features-grid,
  .categories-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 480px) {
  .service-intro-page {
    padding: 0;
  }

  .section-container {
    padding: 0 16px;
  }

  .hero-section {
    padding: 60px 0;
  }

  .features-section,
  .stats-section,
  .ai-section,
  .cta-section {
    padding: 60px 0;
  }
}
</style>
