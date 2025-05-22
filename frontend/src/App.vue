<template>
  <div>
    <Header />
    <!-- <MainPage /> -->
    <!-- <BoardMainPage /> -->
    <!-- <PostWritePage :editPostId="1"/> -->
    <!-- <PostDetailPage /> -->
    <!-- <RouteCreatePage /> -->
    <!-- <MyTravelPlanPage /> -->
    <PlanUpdatePage />
    <Footer />
  </div>
</template>

<script setup>
import MainPage from '@/views/MainPage.vue'
import BoardMainPage from '@/views/BoardMainPage.vue'
import RouteCreatePage from '@/views/RouteCreatePage.vue'
import PostWritePage from '@/views/PostWritePage.vue'
import PostDetailPage from '@/views/PostDetailPage.vue'
import MyTravelPlanPage from '@/views/MyTripPlanPage.vue'
import PlanUpdatePage from '@/views/PlanUpdatePage.vue'
import Header from '@/components/common/Header.vue'
import Footer from '@/components/common/Footer.vue'
</script>

<style scoped>
:root {
  /* 기존 디자인의 색상 변수 */
  --primary-color: #fd7e14;
  --primary-hover: #e8590c;
  --secondary-color: #495057;
  --light-bg: #f8f9fa;
  --border-color: #e9ecef;
  --text-color: #212529;
  --text-secondary: #6c757d;
}

body {
  background-color: #f8f9fa;
  font-family: 'Noto Sans KR', sans-serif;
}

/* 부트스트랩 기본 스타일 오버라이드 */
.btn-primary {
  background-color: var(--primary-color);
  border-color: var(--primary-color);
}

.btn-primary:hover,
.btn-primary:focus,
.btn-primary:active {
  background-color: var(--primary-hover);
  border-color: var(--primary-hover);
}

.btn-outline-primary {
  color: var(--primary-color);
  border-color: var(--primary-color);
}

.btn-outline-primary:hover {
  background-color: var(--primary-color);
  border-color: var(--primary-color);
}

.bg-primary {
  background-color: var(--primary-color) !important;
}

.text-primary {
  color: var(--primary-color) !important;
}
</style>
