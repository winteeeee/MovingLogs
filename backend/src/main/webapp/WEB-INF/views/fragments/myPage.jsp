<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>EnjoyTrip</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="/css/mypage.css" />
  </head>
  <body>
    <!-- Navigation-->
    <nav class="navbar navbar-expand-lg navbar-light fixed-top" id="mainNav">
      <div class="container px-4 px-lg-5">
        <a class="navbar-brand" href="/">Enjoy Trip</a>
        <div class="collapse navbar-collapse" id="navbarResponsive">
          <ul class="navbar-nav ms-auto">
            <!-- <li class="nav-item"><a class="nav-link" href="#about">About</a></li> -->
            <li class="nav-item"><a class="nav-link" href="map.html">Map</a></li>
            <li class="nav-item"><a class="nav-link" href="login.html">Logout</a></li>
          </ul>
        </div>
      </div>
    </nav>

    <div class="container mt-4">
      <h2 class="text-center mb-4">마이페이지</h2>

      <div class="card p-4">
        <h4>회원 정보</h4>
        <p><strong>이메일:</strong> <span id="user-email">${loginUser.email }</span></p>
      </div>

      <div class="card p-4 mt-3">
        <h4>비밀번호 변경</h4>
        <form id="password-form" method="post" action="/user/change-pw">
          <div class="mb-3">
            <input type="password" class="form-control" id="new-password" name="pw" placeholder="새 비밀번호 입력" required />
          </div>
          <div class="mb-3">
            <input type="password" class="form-control" id="confirm-password"placeholder="새 비밀번호 확인" required />
          </div>
          <button type="submit" class="btn btn-primary w-100">비밀번호 변경</button>
        </form>
      </div>

      <div class="card p-4 mt-3">
        <h4>즐겨찾기 내역</h4>
        <ul id="reservation-list" class="list-group">
          <li class="list-group-item">즐겨찾기된 여행이 없습니다.</li>
        </ul>
      </div>
    </div>
	<script>
	  document.getElementById('password-form').addEventListener('submit', function (e) {
		    const newPassword = document.getElementById('new-password').value;
		    const confirmPassword = document.getElementById('confirm-password').value;

		    if (newPassword !== confirmPassword) {
		      e.preventDefault(); // 폼 제출 막기
		      alert('비밀번호가 일치하지 않습니다.');
		    }
		  });
	</script>
    <script src="/js/mypage.js"></script>
  </body>
</html>
