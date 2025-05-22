<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <!-- 헤더에서 연결 되는 모달(로그인) -->
   <div class="modal fade" id="loginModal" tabindex="-1" aria-labelledby="loginModalLabel" aria-hidden="true">
     <div class="modal-dialog modal-dialog-centered">
       <div class="modal-content">
         <div class="modal-header">
           <h1 class="modal-title fs-5" id="registerModalLabel">로그인</h1>
           <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
         </div>
         <form id="loginForm" method="post" action="/auth/login">
	         <div class="modal-body">
		           <div class="input-group mb-3">
		             <span class="input-group-text" id="input-label-id">아이디</span>
		             <input
		               id="login-input-id"
		               name="id"
		               type="text"
		               class="form-control"
		               placeholder="아이디 입력"
		               aria-label="Id"
		               aria-describedby="basic-addon1"
		             />
		           </div>
		           <div class="input-group mb-3">
		             <span class="input-group-text" id="input-label-pw">비밀번호</span>
		             <input
		               id="login-input-pw"
		               name="pw"
		               type="password"
		               class="form-control"
		               placeholder="비밀번호 입력"
		               aria-label="Password"
		               aria-describedby="basic-addon1"
		             />
		           </div>
		     </div>
	         <div class="modal-footer">
	           <button id="login-cancle-btn" type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">
	             닫기
	           </button>
	           <button id="find-pw-btn" type="button" class="btn btn-outline-success">비밀번호 찾기</button>
	           <button id="login-btn" type="submit" id="loginForm" class="btn btn-outline-primary" onclick="login()">로그인</button>
	         </div>
         </form>
       </div>
     </div>
   </div>
</html>
