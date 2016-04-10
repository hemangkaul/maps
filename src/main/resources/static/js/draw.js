/**
 * Drawing the board, which is split into tiles ;D.
 */


/**
 * gets the cache from tile if there. Otherwise, gets that sucker from the back end.
 */

// var cache = {
// 	  {'l': left, 'r': right, 't': top, 'b': bottom} :
//         [{startLng: lng, startLat: lat}: {endLng: lng, endLat: lat}]
// }
function getTile(lat, lng) {
	// 1. check if in front end cache. If so, return the map of latlng pairs
	for (var key in cache) {
		if (lng > key.l && lng < key.r && lat > key.b && lat < key.t) {
			return cache.key;
		}
	}
	
	// 2. if not, get it from the back end. Then add it to the front end cache.
	var postParameters = {"lat": lat, "lng" : lng};
	$.post("/tile", postParameters, function(responseJSON){
		responseObject = JSON.parse(responseJSON);
		var tileCoords = {'l': responseObject.l, 'r': responseObject.r, 
				't': responseObject.t, 'b': responseObject.b};		
		var tileWays = responseObject.map;
		cache.tileCoords = tileWays;
		return tileWays;
	});
}

/**
 * @param map, is an 'object literal', map of (lat, lng) pair to (lat, lng) pair
 */
function drawTile(map) {
	// geographic length of map
	var length = bRLong - tLLong;
	// geographic height of map
	var height = tLLat - bRLat; 
	
	for (var key in map){
		// convert each LatLng to an (x, y)
		startX = key.lng/length * ctx.canvas.width;
		startY = key.lat/height * ctx.canvas.height;
		endX = map.key.lng/length * ctx.canvas.width;
		endY = map.key.lat/height * ctx.canvas.height;
		
    	ctx.moveTo(startX, startY);
    	ctx.lineTo(endX, endY);
    	        
	}

}

/**
 * Drawing the map.
 */
function drawMap(tLLat, tLLong, bRLat, bRLong) {	
	
	// First we have to get the tiles each of the four corners are in
	
	// top left tile
	var tileTL = getTile(tLLat, tLLong);
    // bottom left tile
	var tileBL = getTile(bRLat, tLLong);
	//top right tile
	var tileTR = getTile(tLLat, bRLong);
	//bottom right tile
	var tileBR = getTile(bRLat, bRLong);
	
	drawTile(tileTL);
	drawTile(tileBL);
	drawTile(tileTR);
	drawTile(tileBR);
    ctx.stroke();
}