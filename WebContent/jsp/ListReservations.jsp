<!DOCTYPE html>
<html>
<head>

<title>Hotel Reservation System</title>
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/css/HotelReservation.css" />
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/css/HotelReservation.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/lib/jquery-3.2.1.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/AjaxRouter.js"></script>


<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/jquery-ui-1.12.1/jquery-ui.min.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/jquery-ui-1.12.1/jquery-ui.theme.min.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/jquery-ui-1.12.1/jquery-ui.css">

<script
	src="${pageContext.request.contextPath}/jquery-ui-1.12.1/jquery-ui.js"
	type="text/javascript"></script>



<script>
function parseBreakfasts(room, $room) {
	 if(room.breakfasts){
		 var $breakfasts = $("<table  ></table>");
		 for(var b = 0; b < room.breakfasts.length; b++ ) {
			 var breakfast = room.breakfasts[b];
			 var $breakfast = $("<tr id='" + breakfast.id + "'><td></td><td colspan='3' class='breakfast'> <a class='breakfastlink' href='#'>" +  breakfast.description + ", type: " + breakfast.type + ", at $" + breakfast.price + "</a></td></tr>");
			 $breakfasts.append($breakfast);
		 }
		 $('.room-options', $room).append($("<tr><td class='item-label'>Breakfasts</td> </tr>"));
		 var breakfastList = $("<tr><td></td><td class='breakfasts' colspan='3'><td></tr>");
		 $('.breakfasts', breakfastList).append($breakfasts);
	 
		 $('.room-options', $room).append(breakfastList);
	 }
 
}
function parseServices(room, $room) {
	 if(room.services){
		 var $services = $("<table ></table>");
		 for(var b = 0; b < room.services.length; b++ ) {
			 var service = room.services[b];
			 var $service = $("<tr id='" + service.id + "'><td></td><td colspan='3' class='service'><a class='servicelink' href='#'> " +  service.type + ", at $" + service.price + "</a></td></tr>");
			 $services.append($service);
		 }
		 $('.room-options', $room).append($("<tr><td class='item-label'>Services</td> </tr>"));
		 var serviceList = $("<tr><td></td><td  class='services' colspan='3'><td></tr>");
		 $('.services', serviceList).append($services);
	 
		 $('.room-options', $room).append(serviceList);
	 }
}
function parseRooms(hotel){
	var $rooms = $("<tr><td></td><td colspan='4'><table class='rooms'></table></td></tr>");

	for(var r = 0; r < hotel.rooms.length; r++ ) {
		var room = hotel.rooms[r];
		var $room = $("<tr class='room-entry'  id='" + room.id + "'><td></td><td valign='top' colspan='4' class='room'><a class='roomlink' href='#'>Room no: " +  room.room_no + ", " + room.floor_no + ", at $" + room.price +
				" with capcity" + room.max_people + ", Checkin on: " + room.checkin + ", Checkout on: " + room.checkout + "</a></td><td><table class='room-options'></table> </td></tr>");
		parseBreakfasts(room, $room);
		parseServices(room, $room);
		$('.rooms', $rooms).append($room);
		
	} 
	return $rooms;
}
 
var  reviewdialog = null;
var reviewmessage = null;
var reviewtype = null;   
var reviewItemId = null;  

 function dispayReservations(hotels)
 {
	 var $hotels = $('<table></table>');
	 for(var i = 0; i < hotels.length; i++ ) {
		 var hotel = hotels[i];
		 var $hotel = $("<tr id='" + hotel.id + "'><td colspan='5' class='item-label'> Reservation no: " + hotel.reservation_id + ", in hotel at: " + hotel.street + ", " + hotel.city + ", " + hotel.state + " " + hotel.country + "</td></tr>");
		 var $hotelRooms = $("<tr><td></td><td class='hotel-rooms' colspan='4'></td></tr>");
		 var $rooms = parseRooms(hotel);
		 console.log("ROOMS %o", $rooms);
		 $('.hotel-rooms', $hotelRooms).append($rooms);
		 $hotels.append($hotel);
		 $hotels.append($hotelRooms);
	}
	$('#customer-reservation').html($hotels);	
	$('.servicelink').click(function (event) {
		reviewItemId = $(this).closest('tr').attr('id');
		reviewtype = "S";
		reviewdialog.dialog( "open" );
	});
	$('.breakfastlink').click(function (event) {
		reviewItemId = $(this).closest('tr').attr('id');
		reviewtype = "B";
		reviewdialog.dialog( "open" );
	});
	$('.roomlink').click(function (event) {
		reviewItemId = $(this).closest('tr').attr('id');
		reviewtype = "R";
		reviewdialog.dialog( "open" );
	});
 }
 
	function listMyReservation() {
		var request = {
	 			m_request_type : "fetch-reservations"
	 	}
		var ajaxRouter = new AjaxRouter();
		ajaxRouter.sendrequest(request, function(result){ 
			console.log("Got back %o", result);
			if(result.m_status_code < 0) {
				var error_message_div = $('#reserve-error-message');
				error_message_div.html("<div class='error_message'>" + result.m_status_msg + "</div>");
			} else {
				if(result.hotels.length == 0) {
					reviewmessage.hide();
					displayAlertMessage("There are no reservations under your name currently. Please press on the 'New Reservation' Tab to create one");
				} else {
					dispayReservations(result.hotels);
					reviewmessage.show();
				}
			}
		}); 
	}
	 $(function(){
		 // listMyReservation();
		 $('#refresh-listing').button().click(listMyReservation);
		 reviewmessage = $('#reviewmessage');
		 reviewmessage.hide();
		 reviewdialog = $( "#dialog-form" ).dialog({
		        autoOpen: false,
		        height: 400,
		        width: 650,
		        modal: true,
		        buttons: {
		          "Create a review": function() {
		        	  var request = {
		      	 			m_request_type : "save-review",
		      	 			m_review_type : reviewtype,
		      	 			m_rating : $('#rating').val(),
		      	 			m_item_id : reviewItemId,
		      	 			m_description : $('#review-description').val()
		      	 	}
		      		var ajaxRouter = new AjaxRouter();
		      		ajaxRouter.sendrequest(request, function(result){ 
		      			console.log("Got back %o", result);
		      		});
		        	  
		        	reviewdialog.dialog( "close" );
		          },
		          Cancel: function() {
		        	  reviewdialog.dialog( "close" );
		          }
		        },
		        close: function() {
		        	 reviewdialog.dialog( "close" );
		        }
		      });
		 
		   
		    
	 });
</script>
<style>
#reviewmessage {
 background-color: green;
	font-size:24px;
	color:GoldenRod;
	bottom-margin:24px;
	width:auto;
}
 
.item-label {
	font-weight: bold;
	font-size:24px;
	color:GoldenRod;
}
 
.rooms {
	border: 2px double green;
}
</style>
</head>
<body>

	<div id="reviewmessage">Please click on a room, breakfast or service link to create a review</div>
	<button id="refresh-listing">Refresh List</button>
	
	<div id="customer-reservation"></div>

<div id="dialog-form" title="Create a  Review">
  <p class="validateTips">All form fields are required.</p>
 
  <form>
    <fieldset>
      <label for="rating">Rating</label>
       <select id="rating" name="rating">
		  <option value="1">1</option>
		  <option value="2">2</option>
		  <option value="3">3</option>
		  <option value="4">4</option>
		   <option value="5">5</option>
		  <option value="6">6</option>
		  <option value="7">7</option>
		  <option value="8">8</option>
		   <option value="9">9</option>
		  <option value="10">10</option>
		</select> 
		<p>
		<label for="description">Description</label>
      <input type="text" name="description" id="review-description"  class="text ui-widget-content ui-corner-all">
      </p>
 
      <!-- Allow form submission with keyboard without duplicating the dialog button -->
      <input type="submit" tabindex="-1" style="position:absolute; top:-1000px">
    </fieldset>
  </form>
</div>

</body>
</html>
