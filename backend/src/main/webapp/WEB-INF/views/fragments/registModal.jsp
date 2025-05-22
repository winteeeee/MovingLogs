<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <!-- 헤더에서 연결 되는 모달(회원가입) -->
   <div class="modal fade" id="registerModal" tabindex="-1" aria-labelledby="registerModalLabel" aria-hidden="true">
     <div class="modal-dialog modal-dialog-centered">
       <div class="modal-content">
         <div class="modal-header">
           <h1 class="modal-title fs-5" id="registerModalLabel">회원가입</h1>
           <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
         </div>
         <div class="modal-body">
           <div class="input-group mb-3">
             <span class="input-group-text" id="input-label-id">아이디</span>
             <input
               id="input-id"
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
               id="input-pw"
               type="password"
               class="form-control"
               placeholder="비밀번호 입력"
               aria-label="Password"
               aria-describedby="basic-addon1"
             />
           </div>
           <div class="input-group mb-3">
             <span class="input-group-text" id="input-label-pw-check">비밀번호 확인</span>
             <input
               id="input-pw-check"
               type="password"
               class="form-control"
               placeholder="비밀번호 입력"
               aria-label="PasswordCheck"
               aria-describedby="basic-addon1"
             />
           </div>
           <div class="input-group mb-3">
             <span class="input-group-text">이메일</span>
             <input id="input-email1" type="text" class="form-control" placeholder="이메일 입력" aria-label="Email1" />
             <span id="email-middle-label" class="input-group-text">@</span>
             <input id="input-email2" type="text" class="form-control" placeholder="플랫폼 입력" aria-label="Email2" />
           </div>
         </div>
         <div class="modal-footer">
           <button id="register-cancle-btn" type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">
             닫기
           </button>
           <button id="register-btn" type="button" class="btn btn-outline-primary" onclick="regist()">회원가입</button>
         </div>
       </div>
     </div>
   </div>
   <script src="assets/js/registModal.js"></script>
</html>
