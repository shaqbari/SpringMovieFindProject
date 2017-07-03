<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="http://code.jquery.com/jquery.js"></script>
<script type="text/javascript">
$(function(){
	$('.genre_span').click(function(){
		var mno=$(this).attr("value");
		$('#gfrm'+mno).submit();
	});
});
</script>
</head>
<body>
  <div class="content">
    <div class="facets">
      <div class="facet">
        <div class="facet-title">장르</div>
        <div id="genres">
         <ul style="list-style-type: none">
           <c:forEach var="genre" items="${gList }" varStatus="mno">
             <form method="post" action="genre.do" id="gfrm${mno.index }">
             <li><span class="genre_span" value="${mno.index }">${genre }</span>
               <input type=hidden name="data" value="${genre }">
             </li>
             </form>
           </c:forEach>
         </ul>
       </div>
       </div>
       <div class="facet">
        <div class="facet-title">예매율</div>
        <div id="ratings">
          <jsp:include page="main_chart.jsp"></jsp:include>
        </div>
     
      </div>
    </div>
   <div class="ais-hits" data-reactroot="">
   <div class="ais-hits--item">
     <c:forEach var="vo" items="${mList}">
    <article class="movie">
    <a href="detail.do?mno=${vo.mno }&page=${curpage}">
    <img class="movie-image" src="<c:out value='${vo.poster}'/>" /></a>
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
      
    </div>
   </article>
  </c:forEach>
     <div id="pagination">
       <a href="main.do?page=${curpage>1?curpage-1:curpage }">이전</a>&nbsp;
       <a href="main.do?page=${curpage<totalpage?curpage+1:curpage }">다음</a>&nbsp;&nbsp;
       ${curpage } page / ${totalpage } pages
      </div>
   </div>
   </div>
    
  </div>
</body>
</html>