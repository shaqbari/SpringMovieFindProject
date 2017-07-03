<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
  <div class="ais-hits1" data-reactroot="">
   <div class="ais-hits--item1">
    <c:forEach var="vo" items="${list }">
    <article class="movie">
    <img class="movie-image" src="${vo.poster}" />
    <div class="movie-meta">
      <div class="movie-title">
          ${vo.title }
        <span class="movie-year">
           ${ vo.director }
        </span>
      </div>

      <div class="movie-rating">
         ${vo.star }
      </div>

      <div class="movie-genres">
        ${vo.genre }
      </div>
      <div class="movie-genres">
        ${vo.actor }
      </div>
      <div class="movie-genres">
        ${vo.story }
      </div>
    </div>
   </article>
   </c:forEach>
     <div id="pagination">
        <a href="main.do">목록</a>
     </div>
   </div>
   </div>
</body>
</html>