<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE>
<html >
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script>

</script>
</head>
<body>
	<div class="content">
			<div class="facets">
				<div class="facet">
					<div class="facet-title">장르</div>
					<div id="genres">
						<ul style="list-style-type: none">
							<c:forEach var="genre" items="${gList }">
								<li>${genre }</li>
							</c:forEach>
						</ul>
					</div>
				</div>
		</div>

				<div data-reactroot="" class="ais-hits">
					<div class="ais-hits--item">
						<c:forEach var="vo" items="${mList }">
							<article class="movie">
								<a href="detail.do?mno=${vo.mno }&page=${page}">
									<img class="movie-image" src="${vo.poster }" />
								</a>
								<div class="movie-meta">
									<div class="movie-title">
										${vo.title }
									</div>
									<div class="movie-rating">${vo.star }</div>
									<div class="movie-genres">${vo.genre }</div>
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