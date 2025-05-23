<template>
  <header class="site-header">
    <nav class="navbar navbar-expand-lg">
      <div class="container">
        <router-link to="/" class="navbar-brand">
          <div class="logo">
            <i class="bi bi-compass"></i>
            <span>여행로드</span>
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
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <router-link to="/" class="nav-link" active-class="active">홈</router-link>
            </li>
            <li class="nav-item">
              <router-link to="/board" class="nav-link" active-class="active"
                >여행 게시판</router-link
              >
            </li>
            <li class="nav-item">
              <router-link to="/routes" class="nav-link" active-class="active"
                >인기 경로</router-link
              >
            </li>
            <li class="nav-item">
              <router-link to="/regions" class="nav-link" active-class="active"
                >지역별 정보</router-link
              >
            </li>
            <li class="nav-item">
              <router-link to="/about" class="nav-link" active-class="active"
                >서비스 소개</router-link
              >
            </li>
          </ul>

          <div class="search-box d-none d-lg-flex me-3">
            <input type="text" placeholder="여행지 검색" class="search-input" />
            <button class="search-btn">
              <i class="bi bi-search"></i>
            </button>
          </div>

          <div v-if="authStore.isLoggedIn" class="user-menu">
            <div class="notification-icon me-3">
              <i class="bi bi-bell"></i>
              <span class="notification-badge">2</span>
            </div>

            <div class="dropdown">
              <button
                class="user-profile-btn"
                type="button"
                id="userMenuDropdown"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                <span class="user-name d-none d-lg-inline">{{ authStore.name }}</span>
                <i class="bi bi-chevron-down"></i>
              </button>

              <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userMenuDropdown">
                <li>
                  <router-link to="/profile" class="dropdown-item">
                    <i class="bi bi-person"></i> 내 프로필
                  </router-link>
                </li>
                <li>
                  <router-link to="/my-plans" class="dropdown-item">
                    <i class="bi bi-map"></i> 내 여행 경로
                  </router-link>
                </li>
                <li>
                  <router-link to="/bookmarks" class="dropdown-item">
                    <i class="bi bi-bookmark"></i> 저장한 게시글
                  </router-link>
                </li>
                <li>
                  <router-link to="/settings" class="dropdown-item">
                    <i class="bi bi-gear"></i> 설정
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
            <button class="btn btn-outline-primary me-2" @click="login">로그인</button>
          </div>
        </div>
      </div>
    </nav>
  </header>
</template>

<script setup>
import { ref } from 'vue'
import { useAuthStore } from '@/stores/authStore.js'
import axios from 'axios'

const authStore = useAuthStore()
const serverUrl = import.meta.env.VITE_API_SERVER_URL
const user = ref({
  name: '여행자',
})

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
      authStore.clearAuthInfo()
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

.search-box {
  display: flex;
  align-items: center;
  background-color: #f8f9fa;
  border-radius: 24px;
  overflow: hidden;
  transition: all 0.3s;
  width: 220px;
}

.search-box:focus-within {
  box-shadow: 0 0 0 2px rgba(253, 126, 20, 0.25);
  background-color: white;
}

.search-input {
  border: none;
  background: transparent;
  padding: 8px 16px;
  font-size: 14px;
  width: 100%;
  outline: none;
}

.search-btn {
  background: none;
  border: none;
  padding: 8px 12px;
  color: #6c757d;
  cursor: pointer;
  transition: color 0.2s;
}

.search-btn:hover {
  color: var(--primary-color);
}

.user-menu {
  display: flex;
  align-items: center;
}

.notification-icon {
  position: relative;
  font-size: 20px;
  color: #6c757d;
  cursor: pointer;
}

.notification-badge {
  position: absolute;
  top: -5px;
  right: -5px;
  background-color: var(--primary-color);
  color: white;
  font-size: 10px;
  font-weight: 600;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.user-profile-btn {
  display: flex;
  align-items: center;
  background: none;
  border: none;
  padding: 0;
  cursor: pointer;
}

.user-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #f1f3f5;
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
  .search-box {
    width: 100%;
    margin: 12px 0;
  }

  .auth-buttons,
  .user-menu {
    margin-top: 12px;
    justify-content: center;
  }
}
</style>
