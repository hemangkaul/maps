/**
 * Drawing the board, which is split into tiles ;D.
 */


/**
 * gets the cache from tile if there. Otherwise, gets that sucker from the back end.
 */

var tileWidth = 0.0318947229999352;
var tileHeight = 0.019371145999997452;
	
function getTile(lat, lng) {
	// 1. check if in front end cache. If so, return the map of latlng pairs
	$.each(idCache, function(key, value) {
		if (lng > value.l && lng < value.r && lat > value.b && lat < value.t) {
			drawTile(tileCache[key]);
		}
	})

	// 2. if not, get it from the back end. Then add it to the front end cache.
	var postParameters = {"lat": lat, "lng" : lng};
	$.post("/tile", postParameters, function(responseJSON){
		// responseObject is a map / JS Dictionary / object
		responseObject = JSON.parse(responseJSON);

		// We will add tileCoords to our cache
		var tileCoords = {'l': responseObject.l, 'r': responseObject.r, 
				't': responseObject.t, 'b': responseObject.b};
		// tileWays is the List of ways
		var tileWays = JSON.parse(responseObject.ways);
		idCache[responseObject.id] = tileCoords;
		tileCache[responseObject.id] = tileWays;
		drawTile(tileWays);
	});
}

/**
 * @param map, is an 'object literal', map of (lat, lng) pair to (lat, lng) pair
 */
function drawTile(wayList) {
	// geographic length of map
	var length = rightLong - leftLong;
	// geographic height of map
	var height = topLat - bottomLat;
	
	$.each(wayList, function(index, value) {
		var parsedWay = JSON.parse(value);
		
		startX = (parsedWay.startLongitude - leftLong)/length * ctx.canvas.width;
		// We have to do (topLat - lat) instead of (lat - bottomLat) since the coordinates
		// on the canvas start at (0, 0) in the top left corner.
		startY = (topLat - parsedWay.startLatitude)/height * ctx.canvas.height;
		endX = (parsedWay.endLongitude - leftLong)/length * ctx.canvas.width;
		endY = (topLat - parsedWay.endLatitude)/height * ctx.canvas.height;
		
		ctx.moveTo(startX, startY);
    	ctx.lineTo(endX, endY); 
	})
//	$.each(wayMap, function(key, value) {
//		// convert each LatLng to an (x, y)
//		var parsedKey = JSON.parse(key);
//		var parsedVal = JSON.parse(value);
//		
//		startX = (parsedKey.lng - leftLong)/length * ctx.canvas.width;
//		startY = (parsedKey.lat - bottomLat)/height * ctx.canvas.height;
//		endX = (parsedVal.lng - leftLong)/length * ctx.canvas.width;
//		endY = (parsedVal.lat - bottomLat)/height * ctx.canvas.height;
//		
//    	ctx.moveTo(startX, startY);
//    	ctx.lineTo(endX, endY);    	        
//	})
	
	ctx.stroke();
}

/**
 * Drawing the map.
 */
function drawMap(topLat, leftLong, bottomLat, rightLong) {	
	
	// clear previous canvas
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    
    var curLat = bottomLat;
    var curLng = leftLong;
    
    // draw new canvas
	ctx.beginPath();
	
	while (curLat - tileHeight <= topLat) {
		while (curLng - tileWidth<= rightLong) {
			getTile(curLat, curLng);
			curLng += tileWidth; 
		}
		curLat += tileHeight;
		curLng = leftLong;
	}
	
	// if there is currently a search query for the shortest path, 
	// we draw that too
	drawShortestPath();
	
	ctx.closePath();
//	console.log(idCache);
}