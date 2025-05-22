<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>

<html lang="en">
	<head>
		<link href="/css/navbar.css" rel="stylesheet" />
	</head>
   <!-- 헤더 -->
   <header>
     <nav class="navbar navbar-expand-lg bg-body-tertiary">
       <div class="container-fluid">
         <a class="navbar-brand" href="#">
           <div id="navbar-brand-div">SSAFY TRIP</div>
         </a>
         <button
           class="navbar-toggler"
           type="button"
           data-bs-toggle="collapse"
           data-bs-target="#navbarSupportedContent"
           aria-controls="navbarSupportedContent"
           aria-expanded="false"
           aria-label="Toggle navigation"
         >
           <span class="navbar-toggler-icon"></span>
         </button>
         <div class="collapse navbar-collapse" id="navbarSupportedContent">
           <ul class="navbar-nav me-auto mb-2 mb-lg-0">
           <c:if test="${empty loginUser }">
	           <li class="nav-item">
	               <a class="nav-link active" aria-current="page" href="#">
	                 <button
	                   id="nav-bar-register"
	                   type="button"
	                   class="btn btn-light nav-item-btn"
	                   data-bs-toggle="modal"
	                   data-bs-target="#registerModal"
	                 >
	                   회원가입
	                 </button>
	               </a>
	             </li>
	             <li class="nav-item">
	               <a class="nav-link active" aria-current="page" href="#">
	                 <button
	                   id="nav-bar-login"
	                   type="button"
	                   class="btn btn-light nav-item-btn"
	                   data-bs-toggle="modal"
	                   data-bs-target="#loginModal"
	                 >
	                   로그인
	                 </button>
	               </a>
	             </li>
           </c:if>
           <c:if test="${!empty loginUser}">
             <li class="nav-item" >
                <a class="nav-link active" aria-current="page" href="/user/my-page">
                    <button id="nav-bar-myinfo" type="button" class="btn btn-light nav-item-btn">
                      내 정보 보기
                    </button>
                </a>
              </li>
             <li class="nav-item">
               <a class="nav-link active" aria-current="page" href="/auth/logout">
                 <button
                   id="nav-bar-logout"
                   type="button"
                   class="btn btn-light nav-item-btn"
                   data-bs-toggle="modal"
                   data-bs-target="#myInfoModal"
                 >
                   로그아웃
                 </button>
               </a>
             </li>
           </c:if>
           </ul>
         </div>
       </div>
     </nav>
   </header>
   <script>
   	let msg = "${msg}"
   	if (msg) {
   		alert(msg)
   	}
   </script>
   <script src="assets/js/headerModal.js"></script>
   <%@ include file="registModal.jsp" %>
   <%@ include file="loginModal.jsp" %>
</html>
