$(".field").on('keyup', function() {
	var aOne = $("#actorOne").val();
	var aTwo = $('#actorTwo').val();

	var postParameters = {"aOne": aOne, "aTwo": aTwo};
	$.post("/autocorrect", postParameters, function(responseJSON){
		responseObject = JSON.parse(responseJSON);
		$("#firstOne").text(responseObject.first);
		$("#secondOne").text(responseObject.second);
		$("#thirdOne").text(responseObject.third);
		$("#fourthOne").text(responseObject.fourth);
		$("#fifthOne").text(responseObject.fifth);
		$("#firstTwo").text(responseObject.firstTwo);
		$("#secondTwo").text(responseObject.secondTwo);
		$("#thirdTwo").text(responseObject.thirdTwo);
		$("#fourthTwo").text(responseObject.fourthTwo);
		$("#fifthTwo").text(responseObject.fifthTwo);	
	});
});