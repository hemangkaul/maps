/**
 * Changes color of path depending on traffic
 */

var traffic = (document.getElementById("traffic").getAttribute('value') == 'true');

$( document ).ready(function() {
  if (traffic) {
	  var curUnix = 0.0;
	  while true {
		  if ((Date.now()  - curUnix) >= 1000) {
			  curUnix = Date.now();
			  $.post("/traffic", function(responseJSON) {
				  // responseObject is a map / JS Dictionary / object
				  responseObject = JSON.parse(responseJSON);
				  
				  //responseObject.ways is a HashMap of ways to traffic values
				  drawTraffic(JSON.parse(responseObject.ways));
			  })
		  }
	  }
  }
});

/**
 * Returns the color we should draw the traffic way.
 * @param way
 * @returns
 */
function wayColor(trafficVal) {
	if (trafficVal < 2) {
		ctx.strokeStyle("black");
		ctx.lineWidth = 1;
	} 
	else if (trafficVal < 4) {
		ctx.strokeStyle("yellow");
		ctx.lineWidth = 3;
	}
	else if (trafficVal < 6) {
		ctx.strokeStyle("orange");
		ctx.lineWidth = 5;
	}
	else {
		ctx.strokeStyle("red");
		ctx.lineWidth = 7;
	}
}

/** 
 * Draws traffic onto the map.
 * @param wayMap
 * @returns
 */
function drawTraffic(wayMap) {
	
	// geographic length of map
	var length = rightLong - leftLong;
	// geographic height of map
	var height = topLat - bottomLat;
	
	ctx.beginPath();
	$.each(wayMap, function(key, value) {
		
		var parsedWay = JSON.parse(key);
		
		var sLng = parsedWay.startLongitude;
		var sLat = parsedWay.startLatitude;
		var eLng = parsedWay.endLongitude;
		var eLat = parsedWay.endLatitude;
		
		// Set the color we want to draw the traffic
		
		wayColor(value);	
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
	
	ctx.closePath();
	ctx.stroke();
	
	//reset color and line width
	ctx.strokeStyle = "#000000";
	ctx.lineWidth = 1;
}
