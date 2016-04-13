/**
 * Draws (highlights) the shortest route between two points.
 */

// we use the clear variable to clear the map... i.e. 
// when clear is true, no shortest paths are drawn!
var clear = true;
var wayList = [];

("#submit").on('click', function() {
	$.post('/getPath', $("#inputForm").serialize(), function(responseJSON) {
		responseObject = JSON.parse(responseJSON);
		
		// Set the error message!
		$("#first").text(responseObject.message);
		$("#second").text("");
		$("#third").text("");
		$("#fourth").text("");
		$("#fifth").text("");
		
		// If there is an error message, make the background red!
		if (responseObject.message != "") {
			$("#first").css('background', '#ED7FAD');
			clear = true;
		}
		else { // we have found a valid path!
			clear = false;
			wayList = JSON.parse(responseObject.ways);
			drawShortestPath();
		}
	});		
})

function drawShortestPath() {
	
	// blue, not red, for the path!
	ctx.fillStyle = "#0000FF";
	// geographic length of map
	var length = rightLong - leftLong;
	// geographic height of map
	var height = topLat - bottomLat;
	
	if (clear == true)
		return;
	
	$.each(wayList, function(index, value) {
		
		var parsedWay = JSON.parse(value);
		
		var sLng = parsedWay.startLongitude;
		var sLat = parsedWay.startLatitude;
		var eLng = parsedWay.endLongitude;
		var eLat = parsedWay.endLatitude;
		
		// If either the start point is in the current canvas display,
		// or the end point is in the current canvas display, we draw it
		if ((sLng >= leftLong && sLng <= rightLong && 
				sLat >= bottomLat && sLat <= topLat) ||
			(eLng >= leftLong && eLng <= rightLong &&
					eLat >= bottomLat && sLat <=topLat)) {
			
			startX = (sLng - leftLong)/length * ctx.canvas.width;
			// We have to do (topLat - lat) instead of (lat - bottomLat) since the coordinates
			// on the canvas start at (0, 0) in the top left corner.
			startY = (topLat - sLat)/height * ctx.canvas.height;
			endX = (eLng - leftLong)/length * ctx.canvas.width;
			endY = (topLat - eLat)/height * ctx.canvas.height;
			
			ctx.moveTo(startX, startY);
	    	ctx.lineTo(endX, endY); 	
		}
	})
	
	ctx.stroke();
}

function clearPath() {
	clear == true;
}