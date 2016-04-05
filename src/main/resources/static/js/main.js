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
ctx.canvas.width = canvas.width;
ctx.canvas.height = canvas.height;

//arbitrarily we set all drawn lines to Red for now.
ctx.fillStyle = "#FF0000";
ctx.strokeStyle = "#FF0000";

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
// so if you zoom out to the U.S., and you put your cursor on LA 
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
    // 3. (Px - tLLat)/(brLat - tLLat) stays constant
    // 4. (Py - bRLong)/(tLLong - brLong) stays constant
    //
    // where Px, Py is the position of the cursor when zooming
  
    //geographic length of map
    var length = brLat - tLLat;
    // geographic height of map
    var height = tLLong - bRLong;
  
    // x and y coordinates of current cursor, adjusted to the 
    // leftmost and topmost coordinates of the canvas
    var cursorX = e.pageX - this.offsetLeft;
    var cursorY = e.pageY - this.offsetTop;
	  
  }
  
  
  
  
  
  var exp = e.originalEvent.wheelDelta / 120;
  var fillFactor = Math.pow(2, exp);
  ctx.clearRect(tlX, tlY, percFilled, percFilled);
  percFilled = percFilled * fillFactor;
  ctx.fillRect(tlX, tlY, percFilled, percFilled);
});

$("#map").on('mousedown', function(e) {
  isDown = true;
  var x = e.pageX - this.offsetLeft;
  var y = e.pageY - this.offsetTop;
  ctx.moveTo(x, y);
});

$("#map").on('mousemove', function(e) {
  if (!isDown) return;
  var x = e.pageX - this.offsetLeft;
  var y = e.pageY - this.offsetTop;
  ctx.lineTo(x, y);
  ctx.stroke();
})

$("#map").on('mouseup', function(e) {
  isDown = false;
})