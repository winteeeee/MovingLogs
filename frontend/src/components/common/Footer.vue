<template>
  <footer class="site-footer">
    <div class="container">
      <div class="row">
        <div class="col-lg-4 mb-4 mb-lg-0">
          <div class="footer-logo">
            <i class="bi bi-compass"></i>
            <span>여행로드</span>
          </div>
          <p class="footer-description">
            여행로드는 여행자들이 자신만의 여행 경로를 쉽게 계획하고 공유할 수 있는 플랫폼입니다.
            특별한 여행 경험을 만들고 다른 여행자들과 소통해보세요.
          </p>
          <div class="social-links">
            <a href="#" class="social-link"><i class="bi bi-facebook"></i></a>
            <a href="#" class="social-link"><i class="bi bi-instagram"></i></a>
            <a href="#" class="social-link"><i class="bi bi-twitter"></i></a>
            <a href="#" class="social-link"><i class="bi bi-youtube"></i></a>
          </div>
        </div>

        <div class="col-sm-6 col-lg-2 mb-4 mb-lg-0">
          <h5 class="footer-title">서비스</h5>
          <ul class="footer-links">
            <li><a href="#">여행 게시판</a></li>
            <li><a href="#">인기 경로</a></li>
            <li><a href="#">지역별 정보</a></li>
            <li><a href="#">여행 팁</a></li>
            <li><a href="#">모바일 앱</a></li>
          </ul>
        </div>

        <div class="col-sm-6 col-lg-2 mb-4 mb-lg-0">
          <h5 class="footer-title">회사</h5>
          <ul class="footer-links">
            <li><a href="#">서비스 소개</a></li>
            <li><a href="#">이용약관</a></li>
            <li><a href="#">개인정보처리방침</a></li>
            <li><a href="#">자주 묻는 질문</a></li>
            <li><a href="#">문의하기</a></li>
          </ul>
        </div>

        <div class="col-lg-4">
          <h5 class="footer-title">뉴스레터 구독</h5>
          <p class="footer-newsletter-text">최신 여행 정보와 특별한 혜택을 받아보세요.</p>
          <div class="newsletter-form">
            <input type="email" placeholder="이메일 주소" class="newsletter-input" />
            <button class="newsletter-btn">구독</button>
          </div>
        </div>
      </div>

      <div class="footer-bottom">
        <p class="copyright">© 2023 여행로드. All rights reserved.</p>
        <div class="footer-bottom-links">
          <a href="#">이용약관</a>
          <a href="#">개인정보처리방침</a>
          <a href="#">쿠키 정책</a>
        </div>
      </div>
    </div>
  </footer>
</template>

<style scoped>
/* 푸터 */
.site-footer {
  background-color: #212529;
  color: #adb5bd;
  padding: 60px 0 20px;
}

.footer-logo {
  display: flex;
  align-items: center;
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
  margin-bottom: 20px;
  line-height: 1.6;
}

.social-links {
  display: flex;
  gap: 15px;
  margin-bottom: 30px;
}

.social-link {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  background-color: rgba(255, 255, 255, 0.1);
  color: white;
  border-radius: 50%;
  transition: background-color 0.2s;
}

.social-link:hover {
  background-color: var(--primary-color);
  color: white;
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

.footer-newsletter-text {
  margin-bottom: 15px;
}

.newsletter-form {
  display: flex;
}

.newsletter-input {
  flex: 1;
  padding: 10px 15px;
  border: none;
  border-radius: 4px 0 0 4px;
  font-size: 14px;
}

.newsletter-btn {
  background-color: var(--primary-color);
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 0 4px 4px 0;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.newsletter-btn:hover {
  background-color: var(--primary-hover);
}

.footer-bottom {
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.copyright {
  margin-bottom: 10px;
}

.footer-bottom-links {
  display: flex;
  gap: 20px;
}

.footer-bottom-links a {
  color: #adb5bd;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.2s;
}

.footer-bottom-links a:hover {
  color: white;
}

/* 반응형 스타일 */
@media (max-width: 991.98px) {
  .footer-bottom {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
}
</style>
