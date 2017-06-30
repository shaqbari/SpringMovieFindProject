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
			<div data-reactroot="" class="ais-hits">
					<div class="ais-hits--item">
							<article class="movie">
								<img class="movie-image" src="${vo.poster }" />
								<div class="movie-meta">
									<div class="movie-title">
										${vo.title }
									</div>
									<div class="movie-rating">${vo.star }</div>
									<div class="movie-genres">${vo.genre }</div>
								</div>
							</article>
					<div id="pagination">
						<a href="#">추천영화</a>&nbsp;
						<a href="main.do?page=${page }">목록</a>
					</div>
					</div>
				</div>
</body>
</html>