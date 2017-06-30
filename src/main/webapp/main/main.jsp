<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sist movie Project</title>


<link rel='stylesheet prefetch'
	href='http://cdn.jsdelivr.net/instantsearch.js/1/instantsearch.min.css'>
<link rel='stylesheet prefetch'
	href='http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.css'>

<link rel="stylesheet" href="css/style.css">


</head>

<body>
	<div class="container">
		<div class="top">
			<div class="search-button">
				<i class="fa fa-search"></i>
			</div>
			<div class="input-container">
				<input type="text" id="search-box" />
				<div id="stats"></div>
			</div>
		</div>

		<jsp:include page="${main_jsp }"></jsp:include>
	</div>
		<script
			src='http://cdn.jsdelivr.net/instantsearch.js/1/instantsearch.min.js'></script>

		<script src="js/index.js"></script>
</body>
</html>
