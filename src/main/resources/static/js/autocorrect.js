/**
 * Javascript file for autocorrect functions. 
 * Detects changes in any of the input fields and autocorrects as needed.
 */

$("#streetOne").on('keyup', function() {
	var sOne = $("#streetOne").val();

	var postParameters = {"street": sOne};
	$.post("/autocorrect", postParameters, function(responseJSON){
		responseObject = JSON.parse(responseJSON);
		
		// make background green if not already
		$("#first").css('background', '#D0FBE0');
		
		//set text
		$("#first").text(responseObject.first);
		$("#second").text(responseObject.second);
		$("#third").text(responseObject.third);
		$("#fourth").text(responseObject.fourth);
		$("#fifth").text(responseObject.fifth);
	});
});

$("#crossOne").on('keyup', function() {
	var cOne = $("#crossOne").val();

	var postParameters = {"street": cOne};
	$.post("/autocorrect", postParameters, function(responseJSON){
		responseObject = JSON.parse(responseJSON);
		
		// make background green if not already
		$("#first").css('background', '#D0FBE0');
		
		// set text
		$("#first").text(responseObject.first);
		$("#second").text(responseObject.second);
		$("#third").text(responseObject.third);
		$("#fourth").text(responseObject.fourth);
		$("#fifth").text(responseObject.fifth);
	});
});

$("#streetTwo").on('keyup', function() {
	var sTwo = $("#streetTwo").val();

	var postParameters = {"street": sTwo};
	$.post("/autocorrect", postParameters, function(responseJSON){
		responseObject = JSON.parse(responseJSON);
		
		// make background green if not already
		$("#first").css('background', '#D0FBE0');
		
		// set text
		$("#first").text(responseObject.first);
		$("#second").text(responseObject.second);
		$("#third").text(responseObject.third);
		$("#fourth").text(responseObject.fourth);
		$("#fifth").text(responseObject.fifth);
	});
});

$("#crossTwo").on('keyup', function() {
	var cTwo = $("#crossTwo").val();

	var postParameters = {"street": cTwo};
	$.post("/autocorrect", postParameters, function(responseJSON){
		responseObject = JSON.parse(responseJSON);
		
		// make background green if not already
		$("#first").css('background', '#D0FBE0');
		
		// set text
		$("#first").text(responseObject.first);
		$("#second").text(responseObject.second);
		$("#third").text(responseObject.third);
		$("#fourth").text(responseObject.fourth);
		$("#fifth").text(responseObject.fifth);
	});
});