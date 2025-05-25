<template>
  <div class="main-page">
    <!-- 히어로 섹션 -->
    <MainHeroSection/>
    <!-- 인기 여행 게시판 섹션 -->
    <MainPagePopularRoutesSection/>
    <!-- 최신 게시글 섹션 -->
    <MainPageLatestPostsSection/>
    <!-- 지역별 여행 정보 섹션 -->
    <MainPageRegionsSection/>
    <!-- 플랫폼 소개 섹션 -->
    <MainPagePromoSection/>
  </div>
</template>

<script setup>
import MainHeroSection from '@/components/main/MainHeroSection.vue'
import MainPagePopularRoutesSection from '@/components/main/MainPagePopularRoutesSection.vue'
import MainPageLatestPostsSection from '@/components/main/MainPageLatestPostsSection.vue'
import MainPageRegionsSection from '@/components/main/MainPageRegionsSection.vue'
import MainPagePromoSection from '@/components/main/MainPagePromoSection.vue'
</script>

<style scoped>
/* 섹션 공통 스타일 */
section {
  padding: 60px 0;
}

.view-all-link i {
  margin-left: 6px;
}

@media (max-width: 767.98px) {
  section {
    padding: 40px 0;
  }
}
</style>
