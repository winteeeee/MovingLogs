<template>
  <div class="login-success-container">
    <div class="login-success-content">
      <div class="loading-spinner"></div>
      <h2>로그인 성공</h2>
      <p>잠시만 기다려주세요. 메인 페이지로 이동합니다...</p>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/authStore.js'

const authStore = useAuthStore()
const router = useRouter()
const route = useRoute()
const token = route.query.token
const name = route.query.name

onMounted(() => {
  if (token) {
    // 토큰 저장
    authStore.setAuthInfo(token, name)
    console.log('토큰이 성공적으로 저장되었습니다.')

    // 잠시 후 루트 경로로 리다이렉트 (사용자에게 로딩 표시를 보여주기 위한 짧은 지연)
    setTimeout(() => {
      router.replace('/')
    }, 1000)
  } else {
    // 토큰이 없는 경우 즉시 루트 경로로 리다이렉트
    console.error('토큰이 URL 파라미터에 존재하지 않습니다.')
    router.replace('/')
  }
})
</script>

<style scoped>
.login-success-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f8f9fa;
}

.login-success-content {
  text-align: center;
  background-color: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  max-width: 400px;
  width: 100%;
}

h2 {
  margin: 1rem 0;
  color: #4a6bdf;
}

p {
  color: #6c757d;
  margin-bottom: 0;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #4a6bdf;
  border-radius: 50%;
  margin: 0 auto 1rem;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
