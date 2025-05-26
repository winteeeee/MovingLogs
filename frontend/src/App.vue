<template>
  <div class="layout">
    <Header />
    <main class="content">
      <RouterView />
    </main>
    <Footer v-if="route.path !== '/planning'" />
  </div>
</template>

<script setup>
import Header from '@/components/common/Header.vue'
import Footer from '@/components/common/Footer.vue'
import { useRoute } from 'vue-router'
const route = useRoute()
</script>

<style scoped>
.layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.content {
  flex: 1; /* main 영역이 남는 높이 채움 */
}

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
