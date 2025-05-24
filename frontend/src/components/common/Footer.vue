<template>
  <footer class="site-footer">
    <div class="container">
      <div class="row justify-content-center text-center text-lg-start">
        <!-- Moving Logs 설명 -->
        <div class="col-12 col-md-6 col-lg-4 mb-4">
          <div class="footer-logo">
            <i class="bi bi-compass"></i>
            <span>Moving Logs</span>
          </div>
          <p class="footer-description">
            Moving Logs는 자신만의 여행 경로를 쉽게 계획하고 공유할 수 있는 플랫폼입니다.
            특별한 여행 경험을 만들고 다른 여행자들과 소통해보세요.
          </p>
        </div>

        <!-- 서비스 -->
        <div class="col-6 col-md-3 col-lg-2 mb-4">
          <h5 class="footer-title">서비스</h5>
          <ul class="footer-links">
            <!-- TODO 연결 -->
            <li>여행 게시판</li>
            <li>공지사항</li>
            <li>
              <router-link to="/service-intro" class="nav-link" active-class="active">
                서비스 소개
              </router-link>
            </li>
          </ul>
        </div>

        <!-- 개발자 -->
        <div class="col-6 col-md-3 col-lg-2 mb-4">
          <h5 class="footer-title">개발자</h5>
          <ul class="footer-links">
            <li><a href="https://github.com/DoDoGaMaRu" target="_blank">백대환 (Github)</a></li>
            <li><a href="https://github.com/winteeeee" target="_blank">한성민 (Github)</a></li>
          </ul>
        </div>
      </div>

      <div class="footer-bottom">
        <p class="copyright">© 2025 SSAFY All rights reserved.</p>
      </div>
    </div>
  </footer>
</template>


<style scoped>
.site-footer {
  background-color: #212529;
  color: #adb5bd;
  padding: 60px 0 20px;
  text-align: center;
}

.footer-logo {
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 22px;
  color: white;
  margin-bottom: 15px;
}

.footer-logo i {
  font-size: 24px;
  margin-right: 8px;
  color: var(--primary-color);
}

.footer-description {
  line-height: 1.6;
  max-width: 300px;
  margin: 0 auto 20px;
}

.footer-title {
  color: white;
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 20px;
}

.footer-links {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-links li {
  margin-bottom: 10px;
}

.footer-links a {
  color: #adb5bd;
  text-decoration: none;
  transition: color 0.2s;
}

.footer-links a:hover {
  color: white;
}

.footer-bottom {
  padding-top: 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  text-align: center;
}

@media (min-width: 992px) {
  .footer-description {
    margin-left: 0;
    margin-right: 0;
  }

  .site-footer .row {
    justify-content: center;
  }

  .footer-logo {
    justify-content: flex-start;
  }
}

</style>
