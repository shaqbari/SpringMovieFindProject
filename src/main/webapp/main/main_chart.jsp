<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html >
<head>
  <meta charset="UTF-8">
  <title>animated, overlapped chart</title>
  <style type="text/css">
  #moviecards ul {
     margin: 0;
    padding: 0;
	 margin-left: auto;
	  margin-right: auto;
	  display: block;
	  
	}
	
	#moviecards li {
	  font-family: 'Roboto',sans-serif;
	    border-radius: 2px;
	    display: inline-block;
	    width: 150px;
	    margin-bottom: 5px;
	    background-color: #FFF;
	    color: #212121;
	  box-shadow:0 1px 2px #999;
	    transition:box-shadow .3s ease-in;
	    list-style-type: none;
	 
	}
	
	#moviecards p {
	    font-family: 'Roboto',sans-serif;
	    font-weight: 550;
	  text-align: center;
	    margin-bottom: 2px;
	    margin-top: 2px;
	
	    color: #212121;
	    list-style-type: none;
	}
	
	#moviecards li:hover {
	   box-shadow:0 5px 15px rgba(0,0,0,0.3);
	}
  </style>
</head>

<body>
 <div id="wrapper">
	<ul id="moviecards">
	   <c:forEach var="vo" items="${rList }" varStatus="s">
	     <c:if test="${s.index<3 }">
		  <li><a href="#"><img alt="" height="140px" src="${vo.poster }" width="150px" /></a>
		   <p>${vo.title }(${vo.reserve }%)</p>
		  </li>
		 </c:if>
		</c:forEach>
	</ul>
</div>
</body>
</html>