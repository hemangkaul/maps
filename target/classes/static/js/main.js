// top left latitude and longitude.
// Start points are currently set to Rhode Island State House
var topLat = 41.831080;
var leftLong = -71.414850;

// bottom right latitude and longitude.
// Start points currently set to Wyndham Garden Providence
var bottomLat = 41.818704;
var rightLong = -71.390550;

// zoomLevels arbitrarily set for [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12], 
// where the larger the number the greater the zoom.
var zoomLevel = 5;

var canvas = document.getElementById("map");
var ctx = canvas.getContext("2d");

// setting the width and height will be tricky tricky shmumpkin shmicky
ctx.canvas.width = this.innerWidth * 0.7;
ctx.canvas.height = this.innerHeight;

//arbitrarily we set all drawn lines to Red for now.
ctx.fillStyle = "#FF0000";
ctx.strokeStyle = "#FF0000";

// Is the mouse currently pressed down? Used for panning
var isDown = false;

// The previous position (in x and y coordinates) of the mouse. 
// Used for panning
var last_position = {};

// Caching the tiles
// Since the cache key must be a string
// We can't store a map that likes like this:
// var impossibleMap = 
// {{'l': -71.34, 'r': -71.2, 't': 42.34, 'b': 42.2} : [List-of-ways-to-draw]}

// Thus we make two makes
// idCache is of form: {tileId : {'l': -71.34, 'r': -71.2, 't': 42.34, 'b': 42.2}}
// tileCache is of form: {tileID : [List-of-ways-to-draw]}

// To access the cache, we find the tile ID using idCache, and then use that to find
// the list of ways to draw in tileCache
var idCache = {};
var tileCache = {};


/**
 * **********************************
 * Zooming function
 * **********************************
 */


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
  
  // zoomFactor will equal 1/1.5 = 2/3 if the user scrolls up,
  // and 1.5 if the user scrolls down
  var zoomFactor = Math.pow(1.2, zoomExp*-1);
  
  // we are zooming in, and we are not currently at max zoom
  // or we are zooming out, and we are not currently at min zoom
  if (((zoomFactor < 1) && (zoomLevel < 12)) || 
		  ((zoomFactor > 1) && (zoomLevel > 0))) {
	// We want new topLat, leftLong, bottomLat, and rightLong which 
    // satisfy four restraints:
  
    // 1. [new bottomLat] - [new topLat] = zoomFactor*([old bottomLat] - [old topLat])
    // 2. [new leftLong] - [new rightLong] = zoomFactor*([old leftLong] - [old rightLong])
    // 3. (P_lat - topLat)/(brLat - topLat) stays constant
    // 4. (P_long - rightLong)/(leftLong - brLong) stays constant
    //
    // where P_lat and P_long refer to the coordinates the mouse is pointing at
  
    //geographic length of map
    var length = rightLong - leftLong;
    // geographic height of map
    var height = topLat - bottomLat;
  
    // x and y coordinates of current cursor, adjusted to the 
    // leftmost and topmost coordinates of the canvas
    var cursorX = e.pageX - this.offsetLeft;
    var cursorY = e.pageY - this.offsetTop;
        
    // what fraction of the total canvas width is the x value?
    // what fraction of the total canvas height is the y value?
    var proportionLength = cursorX / ctx.canvas.width;
    var proportionHeight = cursorY / ctx.canvas.height;
    
    console.log(ctx.canvas.width);
    console.log(ctx.canvas.height);
    
    // the latitude and longitude of the cursor point
    var cursorLat = topLat - proportionHeight * height;
    var cursorLong = proportionLength * length + leftLong;
    
    console.log(cursorX);
    console.log(cursorY);
    console.log(proportionLength);
    console.log(proportionHeight);
    
    // So (cursorLat - bottomLat)/height should not change after zooming.
    // We know height changes by a factor of zoomFactor
    // and cursorLat doesn't change.
    // Thus we can write
    // 
    //    (cursorLat - new_bottomLat)/(zoomFactor * height) = 
    // 	                            (cursorLat - old_bottomLat)/ height
    // -> (cursorLat - new_bottomLat)/zoomFactor = cursorLat - old_ bottomLat
    // -> cursorLat - new_bottomLat = zoomFactor*(cursorLat - old_bottomLat)
    // -> - new_bottomLat = cursorLat(zoomFactor - 1) - zoomFactor*old_bottomLat
    // -> new_bottomLat = cursorLat(1 - zoomFactor) + zoomFactor*old_bottomLat
    var newBottomLat = cursorLat*(1 - zoomFactor) + zoomFactor*bottomLat;
    var newTopLat = newBottomLat + height*zoomFactor;
    
    // similar calculations are done to find newTLLong and newBRLong
    var newLeftLong = cursorLong*(1 - zoomFactor) + zoomFactor*leftLong;
    var newRightLong = newLeftLong + length*zoomFactor;
    
    topLat = newTopLat;
    leftLong = newLeftLong;
    bottomLat = newBottomLat;
    rightLong = newRightLong;
    
    zoomLevel += zoomExp;
    
    // draw new map
    drawMap(topLat, leftLong, bottomLat, rightLong);
  }  
});

/**
 * ****************************************
 * Panning functions
 * ****************************************
 */
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
	  var proportionWidth = deltaX / ctx.canvas.width;
	  var proportionHeight = deltaY / ctx.canvas.height;
	  
	  //geographic length of map
	  var length = rightLong - leftLong;
	  // geographic height of map
	  var height = topLat - bottomLat; 
	  
	  var deltaLong = length*proportionWidth;
	  var deltaLat = height*proportionHeight;
	  
	  topLat += deltaLat;
	  leftLong -= deltaLong;
	  bottomLat += deltaLat;
	  rightLong -= deltaLong;
	  
	  drawMap(topLat, leftLong, bottomLat, rightLong);
  }
  last_position = {x: e.pageX, y: e.pageY};
  })

$("#map").on('mouseup', function(e) {
  isDown = false;
})

///**
// * ***************************************
// * Click to input the nearest intersection / point
// * ***************************************
// */
//
//$("#map").on('click', function(e) {
//	//geographic length of map
//    var length = rightLong - leftLong;
//    // geographic height of map
//    var height = topLat - bottomLat;
//  
//    // x and y coordinates of current cursor, adjusted to the 
//    // leftmost and topmost coordinates of the canvas
//    var cursorX = e.pageX - this.offsetLeft;
//    var cursorY = e.pageY - this.offsetTop;
//        
//    // what fraction of the total canvas width is the x value?
//    // what fraction of the total canvas height is the y value?
//    var proportionLength = cursorX / ctx.canvas.width;
//    var proportionHeight = cursorY / ctx.canvas.height;
//    
//    // the latitude and longitude of the cursor point
//    var cursorLat = topLat - proportionHeight * height;
//    var cursorLong = proportionLength * length + leftLong;
//    
//    // the latitude and longitude of the nearest node to the cursor point
//    
//    //Getting the nearest neighbor
//    var postParameters = {"lat": cursorLat, "lng" : cursorLong};
//	$.post("/nearestNeighbor", postParameters, function(responseJSON) {
//		// responseObject is a map / JS Dictionary / object
//		responseObject = JSON.parse(responseJSON);
//		nearestLat = responseObject.newLat;
//		nearestLng = responseObject.newLng;
//	})    
//})
//    
    
// draw the initial map
drawMap(topLat, leftLong, bottomLat, rightLong);

