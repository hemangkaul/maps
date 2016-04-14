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

		// right now all the ways are in JSON format. Rather than parsing 
		// it every time we draw the way, we parse it upfront and store it
		// already parsed
		var deJSONedWays = deJSON(JSON.parse(responseObject.ways));
		
		
		// storing in the cache
		idCache[responseObject.id] = tileCoords;
		tileCache[responseObject.id] = deJSONedWays;
		drawTile(deJSONedWays);
	});
}

/**
 * converts a list of JSON ways into a list of ways that are easily accessible by JQuery
 * @param wayList
 */
function deJSON(wayList) {
	
	var deJSONedList = [];
	// for each way, we parse it and add it to deJSONedList
	$.each(wayList, function(index, value) {
		deJSONedList.push(JSON.parse(value));
	})
	
	return deJSONedList;
}

/**
 * @param map, is an 'object literal', map of (lat, lng) pair to (lat, lng) pair
 */
function drawTile(wayList) {
	// geographic length of map
	var length = rightLong - leftLong;
	// geographic height of map
	var height = topLat - bottomLat;
	
	ctx.beginPath();
	$.each(wayList, function(index, value) {
		wayColor(value);
		startX = (value.startLongitude - leftLong)/length * ctx.canvas.width;
		// We have to do (topLat - lat) instead of (lat - bottomLat) since the coordinates
		// on the canvas start at (0, 0) in the top left corner.
		startY = (topLat - value.startLatitude)/height * ctx.canvas.height;
		endX = (value.endLongitude - leftLong)/length * ctx.canvas.width;
		endY = (topLat - value.endLatitude)/height * ctx.canvas.height;
		
		ctx.moveTo(startX, startY);
    	ctx.lineTo(endX, endY); 
	})
	ctx.closePath();
	ctx.stroke();
}

/**
 * Drawing the map.
 */
function drawMap() {	
	
	// clear previous canvas
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    
    var curLat = bottomLat;
    var curLng = leftLong;
    
    // draw new canvas
	
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
}

///**
// * Overall Draw Function
// */
//function draw(topLat, leftLong, bottomLat, rightLong) {
//	$.when(drawMap(topLat, leftLong, bottomLat, rightLong)).then(drawShortestPath());
//}

