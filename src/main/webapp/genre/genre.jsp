<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html >
<head>
  <meta charset="UTF-8">
  <title>Ghetto jQuery Templating</title>
  
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/normalize/5.0.0/normalize.min.css">

  
      <style>
      /* NOTE: The styles were added inline because Prefixfree needs access to your styles and they must be inlined if they are on local disk! */
      * { box-sizing: border-box; }
body {
  padding: 1rem;
  background: #333;
}

section {
  background: white;
  padding: 1rem;
  overflow: hidden;
}


h2 {
  margin: 0 0 0.5rem 0;
  padding: 0 0 0.25rem 0;
  border-bottom: 2px solid #333;
}

.module {
  margin: 0 0 1rem 0;
}
.module-movie {
  float: left;
  width: 32%;
  margin-right: 2%;
  height: 391px;
  background-repeat: no-repeat;
  background-size: cover;
  position: relative;
}
.module-movie:nth-of-type(3n) {
  margin-right: 0;
}
.module-movie::after {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(transparent 20%, black);
  z-index: 1;
}
.movie-info {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  padding: 0.5rem;
  color: white;
  z-index: 2;
}
.movie-title {
  margin: 0 0 0.5rem 0;
}
.movie-title::before {
  content: "Title";
  display: block;
  font-size: 0.5rem;
  text-transform: uppercase;
  color: #999;
}
.movie-director::before {
  content: "Director";
  display: block;
  font-size: 0.5rem;
  text-transform: uppercase;
  color: #999;
}
.movie-director {
  margin: 0;
}

    </style>

  <script src="https://cdnjs.cloudflare.com/ajax/libs/prefixfree/1.0.7/prefixfree.min.js"></script>

</head>

<body>
  <script id="movie-template" type="text/x-handlebars-template">
  <div class='module module-movie' style='background-image: url({{movieImage}});'>
    <div class='movie-info'> 
      <h3 class='movie-title'>
        {{movieTitle}}
      </h3>
      <p class='movie-director'>
        {{movieDirector}}
      </p>
    </div>                      
  </div>
</script>
  
<section id="movies">
  <h2>${genre }</h2>
</section>
  <script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
<script src='https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/1.0.0/handlebars.min.js'></script>

    <script>
    var data = {
    		  movies: <%=request.getAttribute("json")%>
    		};

    		var i;

    		// Getting the template ready
    		var source = $("#movie-template").html();
    		var template = Handlebars.compile(source);

    		// Business Logic (how many, where they go, etc).
    		for (i = 0; i < data.movies.length; i++) {
    		    
    		  var html = template(data.movies[i]);
    		  $("#movies").append(html);
    		  
    		};
    </script>

</body>
</html>
