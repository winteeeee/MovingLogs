<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>SSAFY TRIP</title>
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet"
      integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
      crossorigin="anonymous"
    />
    <link href="/css/main.css" rel="stylesheet" />
  </head>
  <body>
    <%@ include file="fragments/header.jsp" %>
    <section class="hero">
      <div class="overlay"></div>
      <div class="hero-content">
        <h1>당신의 여행, 지금 시작하세요!</h1>
        <p>아름다운 순간을 담고, 새로운 경험을 즐기세요.</p>
        <c:if test="${!empty loginUser }">
        	<a href="map.jsp" class="cta-btn">여행 정보 찾기</a>
        </c:if>
        <c:if test="${empty loginUser }">
        	<a href="#" class="cta-btn" data-bs-toggle="modal" data-bs-target="#loginModal">로그인 하고 여행 정보 찾기</a>
        </c:if>
      </div>
    </section>

    <section class="popular-destinations">
      <h2>🌍 나만의 핫 플레이스를 등록하세요!</h2>
      <div class="destination-container">
        <div class="destination">
          <img src="/img/seoul.jpg" alt="서울" />
          <h3>서울</h3>
          <p>대한민국의 수도! 최고의 관광도시</p>
        </div>
        <div class="destination">
          <img src="/img/busan.jpg" alt="부산" />
          <h3>부산</h3>
          <p>푸른 바다와 신비로운 자연을 경험하세요.</p>
        </div>
        <div class="destination">
          <img src="/img/gumi.jpg" alt="구미" />
          <h3>구미</h3>
          <p>우리나라 최고의 도시입니다..</p>
        </div>
      </div>
    </section>

    <section class="custom-trip">
      <div class="trip-content">
        <div class="trip-text">
          <h2>🗺️ 나만의 여행 경로를 만들어보세요!</h2>
          <p>
            원하는 여행지를 선택하고 맞춤형 여행 일정을 계획하세요. 쉽고 직관적인 인터페이스로 최고의 여행을 준비할 수
            있습니다!
          </p>
          <ul>
            <li>📍 원하는 여행지를 추가하고 경로 설정</li>
            <li>🕒 일정별 세부 일정 관리</li>
            <li>💾 저장하고 언제든지 수정 가능</li>
          </ul>
        </div>
      </div>
    </section>
  </body>
  <%@ include file="fragments/footer.jsp" %>
  <!--서비스 ID 설정-->
  <script
    type="text/javascript"
    src="https://sgisapi.kostat.go.kr/OpenAPI3/auth/javascriptAuth?consumer_key=214750f7a514477dbd7d"
  ></script>
  <!-- 
  <script src="assets/js/keys.js"></script>
  <script src="assets/js/common.js"></script>
  <script src="assets/js/kostat.js"></script>
  <script src="assets/js/enjoytrip.js"></script>
  <script src="assets/js/navbar.js"></script> -->
  <script
    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"
    integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy"
    crossorigin="anonymous"
  ></script>
</html>
