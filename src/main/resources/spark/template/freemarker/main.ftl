<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- In real-world webapps, css is usually minified and
         concatenated. Here, separate normalize from our code, and
         avoid minification for clarity. -->
    <link rel="stylesheet" href="css/normalize.css">
    <link rel="stylesheet" href="css/html5bp.css">
    <link rel="stylesheet" href="css/main.css">
  </head>
  <body>
  
  <section class = "mapsContainer">
  	<article class = "left-input">
	  	<form method="POST" action="/results">
	  		Start Point<br>
		    Street <input type="text" name = "streetOne" id = "streetOne"/><br>
		    Cross Street <input type="text" name = "crossOne" id = "crossOne"/><br>
		    End Point<br>
		    Street <input type="text" name = "streetTwo" id = "streetTwo"/><br>
		    Cross Street <input type="text" name = "crossTwo" id = "crossTwo"/>
		    <input type="submit" value="Find Path!"/>
		    <div id = "first"></div>
			<div id = "second"></div>
			<div id = "third"></div>
			<div id = "fourth"></div>
			<div id = "fifth"></div>
		</form>
  	</article>
  	<article class = "right-canvas">
  	<canvas id="compteur1" width="300" height="300"></canvas>
  	</article>
  		
     <!-- Again, we're serving up the unminified source for clarity. -->
     <script type="text/javascript" src="js/jquery-2.1.1.js"></script>
     <script type="text/javascript" src="js/main.js"></script>
  </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
       dealing with real world issues like old browsers.  -->
</html>