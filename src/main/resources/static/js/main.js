// top left latitude and longitude.
// Start points are currently set to Rhode Island State House
var tLLat = 41.831080;
var tLLong = -71.414850;

// bottom right latitude and longitude.
// Start points currently set to Wyndham Garden Providence
var bRLat = 41.818704;
var bRLong = -71.390550;

// zoomLevels arbitrarily set for [0, 1, 2, 3], 
// where the larger the number the greater the zoom.
var zoomLevel = 0;

var canvas = document.getElementById("map");
var ctx = canvas.getContext("2d");

// setting the width and height will be tricky tricky shmumpkin shmicky
ctx.canvas.width = this.innerWidth;
ctx.canvas.height = this.innerHeight;

//arbitrarily we set all drawn lines to Red for now.
ctx.fillStyle = "#FF0000";
ctx.strokeStyle = "#FF0000";

// Is the mouse currently pressed down? Used for panning
var isDown = false;

// The previous position (in x and y coordinates) of the mouse. 
// Used for panning
var last_position = {};

// Caching the tiles as a map (though more of a list for our purposes) 
// of tile coordinates to the tiles to draw
var cache = {};

// cache format:
// var cache = {
// 	  {'l': left, 'r': right, 't': top, 'b': bottom} : {{startX: x, startY: y}: {endX: x, endY: y}}
// }

// The idea is we re-set the top left and top right coordinates,
// and then re-draw the map using a function that only takes in those coordinates.
//
// Each zoom will be a "1.5x zoom", where 1.5 is an arbitrary constant
// By 1.5x zoom, this means each time we zoom in, the new "map" has 1/1.5 the 
// original geographic length and 1/1.5 the original geographic height; 
// so the total geographic area is 1/2.25 = 4/9 the original
//
// Another way to look at it is when you zoom out, 
// the geographic area of the map is 2.25 times that of before 
// 
// Google Maps also zooms depending on where the mouse is positioned... 
// We decided to copy that as a cool swag functionality.
// How it works is wherever the cursor is pointing on a map, if you zoom,
// the cursor will still be pointing at the same thing.
// so if you zoom out to the U.S., and then you put your cursor on LA 
// when you zoom all the way in, your cursor will still be on LA
$("#map").on('mousewheel', function(e) {
	
  // zoomExp will equal 1 if the user scrolls up,
  // and -1 if the user scrolls down
  var zoomExp = e.originalEvent.wheelDelta/120;
  
  // zoomFactor will equal 1.5 if the user scrolls up,
  // and 1/1.5 = 2/3 if the user scrolls down
  var zoomFactor = Math.pow(1.5, zoomExp);
  
  // we are zooming in, and we are not currently at max zoom
  // or we are zooming out, and we are not currently at min zoom
  if (((zoomFactor > 1) && (zoomLevel != 3)) || 
		  ((zoomFactor < 1) && (zoomLevel != 0))) {
	// We want new tLLat, tLLong, bRLat, and bRLong which 
    // satisfy four restraints:
  
    // 1. [new bRLat] - [new tLLat] = zoomFactor*([old bRLat] - [old tLLat])
    // 2. [new tLLong] - [new bRLong] = zoomFactor*([old tLLong] - [old bRLong])
    // 3. (P_lat - tLLat)/(brLat - tLLat) stays constant
    // 4. (P_long - bRLong)/(tLLong - brLong) stays constant
    //
    // where P_lat and P_long refer to the coordinates the mouse is pointing at
  
    //geographic length of map
    var length = brLong - tLLong;
    // geographic height of map
    var height = tLLat - bRLat;
  
    // x and y coordinates of current cursor, adjusted to the 
    // leftmost and topmost coordinates of the canvas
    var cursorX = e.pageX - this.offsetLeft;
    var cursorY = e.pageY - this.offsetTop;
    
    // what fraction of the total canvas width is the x value?
    // what fraction of the total canvas height is the y value?
    var proportionLength = cursorX / this.innerWidth;
    var proportionHeight = cursorY / this.innerHeight;
    
    // the latitude and longitude of the cursor point
    var cursorLat = proportionHeight * height + bRLat;
    var cursorLong = proportionLength * lenght + tLLong;
    
    // So (cursorLat - bRLat)/height should not change after zooming.
    // We know height changes by a factor of zoomFactor
    // and cursorLat doesn't change.
    // Thus we can write
    // 
    //    (cursorLat - new_bRLat)/(zoomFactor * height) = 
    // 	                            (cursorLat - old_bRLat)/ height
    // -> (cursorLat - new_bRLat)/zoomFactor = cursorLat - old_ bRLat
    // -> cursorLat - new_bRLat = zoomFactor*(cursorLat - old_bRLat)
    // -> - new_bRLat = cursorLat(zoomFactor - 1) - zoomFactor*old_bRLat
    // -> new_bRLat = cursorLat(1 - zoomFactor) + zoomFactor*old_bRLat
    var newBRLat = cursorLat*(1 - zoomFactor) + zoomFactor*bRLat;
    var newTLLat = newbRLat + height*zoomFactor;
    
    // similar calculations are done to find newTLLong and newBRLong
    var newTLLong = cursorLong*(1 - zoomFactor) + zoomFactor*TLLong;
    var newBRLong = newTLLong + length*zoomFactor;
    
    tLLat = newTLLat;
    tLLong = newTLLong;
    bRLat = newBRLat;
    bRLong = newBRLong;
    
    zoom += zoomExp;    
    drawMap(tLLat, tLLong, bRLat, bRLong);
  }  
});

$("#map").on('mousedown', function(e) {
  isDown = true;
  last_position = { x: e.pageX, y: e.pageY}
});

$("#map").on('mousemove', function(e) {
  
  // only do stuff if mouse is currently pressed down
  if (!isDown) return;
  
  // we want to access the previous x and y value. 
  // we check that they're not undefined.
  
  // (though they should never be undefined when 
  // isDown is true, since isDown is only true when 
  // the mouse is pressed down)
  if ((typeof(last_position.x) != 'undefined') &
		  (typeof(last_position.y) != 'undefined')){
	  
	  // change in x and y values
	  var deltaX = e.pageX - last_position.x;
	  var deltaY = e.pageY - last_position.y;
	  
	  // what fraction of the total canvas width is the deltaX?
	  // what fraction of the total canvas height is the deltaY?
	  var proportionWidth = deltaX / this.innerWidth;
	  var proportionHeight = deltaY / this.innerHeight;
	  
	  //geographic length of map
	  var length = bRLong - tLLong;
	  // geographic height of map
	  var height = tLLat - bRLat; 
	  
	  var deltaLong = length*proportionWidth;
	  var deltaLat = height*proportionHeight;
	  
	  tLLat += deltaLat;
	  tLLong += deltaLong;
	  bRLat += deltaLat;
	  bRLong += deltaLong;
	  
	  drawMap(tLLat, tLLong, bRLat, bRLong);
  }
  last_position = {x: e.pageX, y: e.pageY};
  })

$("#map").on('mouseup', function(e) {
  isDown = false;
})

// draw the initial map
drawMap(tLLat, tLLong, bRLat, bRLong);

