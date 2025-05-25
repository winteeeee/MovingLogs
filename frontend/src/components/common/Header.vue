<template>
  <header class="site-header">
    <nav class="navbar navbar-expand-lg">
      <div class="container">
        <router-link to="/" class="navbar-brand">
          <div class="logo">
            <i class="bi bi-compass"></i>
            <span>Moving Logs</span>
          </div>
        </router-link>

        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarContent"
          aria-controls="navbarContent"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <i class="bi bi-list"></i>
        </button>

        <div class="collapse navbar-collapse" id="navbarContent">
          <ul class="navbar-nav me-auto mt-2 mb-lg-0">
            <li class="nav-item ps-5">
              <router-link to="/" class="nav-link" active-class="active">홈</router-link>
            </li>
            <li class="nav-item ps-4">
              <router-link to="/tripost/board" class="nav-link" active-class="active"
                >여행 커뮤니티</router-link
              >
            </li>
            <!-- TODO 연결 -->
            <li class="nav-item ps-3">
              <router-link to="/routes" class="nav-link" active-class="active"
                >공지사항</router-link
              >
            </li>
            <li class="nav-item ps-3">
              <router-link to="/service-intro" class="nav-link" active-class="active"
                >서비스 소개</router-link
              >
            </li>
          </ul>

          <div v-if="authStore.isLoggedIn" class="user-menu">
            <div class="dropdown">
              <button
                class="user-profile-btn"
                type="button"
                id="userMenuDropdown"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                <span class="user-name d-none d-lg-inline fancy-greeting">
                  안녕하세요, {{ authStore.name }}님!
                </span>
                <i class="bi bi-chevron-down"></i>
              </button>

              <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userMenuDropdown">
                <li>
                  <router-link to="/my-plans" class="dropdown-item">
                    <i class="bi bi-map"></i> 내 여행 계획
                  </router-link>
                </li>
                <li>
                  <!-- TODO 연결 -->
                  <router-link to="/bookmarks" class="dropdown-item">
                    <i class="bi bi-bookmark"></i> 작성한 게시글
                  </router-link>
                </li>
                <li><hr class="dropdown-divider" /></li>
                <li>
                  <button class="dropdown-item" @click="logout">
                    <i class="bi bi-box-arrow-right"></i> 로그아웃
                  </button>
                </li>
              </ul>
            </div>
          </div>

          <div v-else class="auth-buttons">
            <button class="kakao-login-btn" @click="login">
              <img src="https://developers.kakao.com/assets/img/about/logos/kakaolink/kakaolink_btn_small.png" alt="카카오 로그인" class="kakao-icon" />
              카카오 로그인
            </button>
          </div>
        </div>
      </div>
    </nav>
  </header>
</template>

<script setup>
import { useAuthStore } from '@/stores/authStore.js'
import axios from 'axios'
import { router } from '@/router/index.js'

const authStore = useAuthStore()
const serverUrl = import.meta.env.VITE_API_SERVER_URL

function login() {
  window.location.href = `${serverUrl}/oauth2/authorization/kakao`
}

function logout() {
  console.log('로그아웃 처리')
  axios
    .post(`${serverUrl}/api/v1/auth/logout`, null, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`,
      },
    })
    .then((res) => {
      console.log('로그아웃 성공: ', res.data)
    })
    .catch((err) => {
      console.log('로그아웃 실패: ', err)
    })
    .finally(() => {
      alert("로그아웃 되었습니다.");
      authStore.clearAuthInfo()
      router.push('/')
    })
}
</script>

<style scoped>
.site-header {
  background-color: white;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  position: sticky;
  top: 0;
  z-index: 100;
}

.navbar {
  padding: 12px 0;
}

.logo {
  display: flex;
  align-items: center;
  font-weight: 700;
  font-size: 22px;
  color: var(--primary-color);
}

.logo i {
  font-size: 24px;
  margin-right: 8px;
}

.nav-link {
  font-weight: 500;
  color: #495057;
  padding: 8px 16px;
  transition: color 0.2s;
}

.nav-link:hover,
.nav-link.active {
  color: var(--primary-color);
  font-weight: 600;
  border-bottom: 2px solid var(--primary-color); /* 예시 */
}

.user-menu {
  display: flex;
  align-items: center;
}

.user-profile-btn {
  display: flex;
  align-items: center;
  background: none;
  border: none;
  padding: 0;
  cursor: pointer;
}

.user-name {
  margin: 0 8px;
  font-weight: 500;
  color: #495057;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  font-size: 14px;
}

.dropdown-item i {
  font-size: 16px;
  color: #6c757d;
}

.dropdown-item:active {
  background-color: var(--primary-color);
}

.dropdown-item:active i {
  color: white;
}

.navbar-toggler {
  border: none;
  color: #495057;
  font-size: 24px;
  padding: 4px;
}

.navbar-toggler:focus {
  box-shadow: none;
}

@media (max-width: 991.98px) {
  .auth-buttons,
  .user-menu {
    margin-top: 12px;
    justify-content: center;
  }
}

.kakao-login-btn {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 10px 24px;
  font-size: 1rem;
  font-weight: 600;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  color: #3C1E1E;
  background: linear-gradient(135deg, #fff8e1, #fff3cd);
  border: 1.5px solid #f7d600;
  border-radius: 30px;
  box-shadow: 0 4px 8px rgb(247 214 0 / 0.15);
  cursor: pointer;
  transition: background 0.3s ease, box-shadow 0.3s ease;
}

.kakao-login-btn:hover {
  background: linear-gradient(135deg, #fff3cd, #f7d600);
  box-shadow: 0 6px 12px rgb(247 214 0 / 0.35);
}

.kakao-icon {
  width: 22px;
  height: 22px;
  opacity: 0.85;
  transition: opacity 0.3s ease;
}

.kakao-login-btn:hover .kakao-icon {
  opacity: 1;
}

.fancy-greeting {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  font-weight: 600;
  font-size: 1.1rem;
  color: #3a3a3a; /* 진한 그레이 */
  padding: 4px 12px;
  border-radius: 12px;
  transition: box-shadow 0.3s ease;
  user-select: none;
}

.fancy-greeting:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  cursor: default;
}
</style>
