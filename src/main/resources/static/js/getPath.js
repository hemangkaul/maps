/**
 * Draws (highlights) the shortest route between two points.
 */

var clear = true;

("#submit").on('click', function() {
	$.post('/getPath', $("#inputForm").serialize(), function(responseObject) {
		do stuff
	})
})