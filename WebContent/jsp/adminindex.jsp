<!DOCTYPE html>
<html>
<head>
 
<title>
Hotel Reservation System
</title>
<link type = "text/css"  rel="stylesheet" href="${pageContext.request.contextPath}/css/HotelReservation.css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/lib/jquery-3.2.1.js"></script>
 
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery-ui-1.12.1/jquery-ui.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery-ui-1.12.1/jquery-ui.theme.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery-ui-1.12.1/jquery-ui.css">
 
<script src="${pageContext.request.contextPath}/jquery-ui-1.12.1/jquery-ui.js" type="text/javascript"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/AjaxRouter.js"></script> 
 
</head>
<body>

<style>
 
 
.item-label {
	font-weight: bold;
	font-size:24px;
	color:GoldenRod;
}
 
.rooms {
	border: 2px double green;
}
</style>

<script type="text/javascript">
 
function parseBreakfasts(room, $room) {
	 if(room.breakfasts){
		 var $breakfasts = $("<table  ></table>");
		 for(var b = 0; b < room.breakfasts.length; b++ ) {
			 var breakfast = room.breakfasts[b];
			 var $breakfast = $("<tr id='" + breakfast.id + "'><td></td><td colspan='3' class='breakfast'>  " +  breakfast.description + ", type: " + breakfast.type + ", at $" + breakfast.price + "</td></tr>");
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
			 var $service = $("<tr id='" + service.id + "'><td></td><td colspan='3' class='service'>" +  service.type + ", at $" + service.price + "</td></tr>");
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
		var $room = $("<tr class='room-entry'  id='" + room.id + "'><td></td><td valign='top' colspan='4' class='room'>Room no: " +  room.room_no + ", " + room.floor_no + ", at $" + room.price +
				" with capcity" + room.max_people + ", Checkin on: " + room.checkin + ", Checkout on: " + room.checkout + "</td><td><table class='room-options'></table> </td></tr>");
		parseBreakfasts(room, $room);
		parseServices(room, $room);
		$('.rooms', $rooms).append($room);
		
	} 
	return $rooms;
}
 
 

 function dispayReservations(hotels)
 {
	 var $hotels = $('<table></table>');
	 for(var i = 0; i < hotels.length; i++ ) {
		 var hotel = hotels[i];
		 var $hotel = $("<tr id='" + hotel.id + "'><td colspan='5' class='item-label'> Reservation no: " +
				 		hotel.reservation_id + ", in hotel at: " + hotel.street + ", " + hotel.city + ", " + hotel.state + " " + hotel.country +
				 		"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button id='check-customer-in'>Checkin</button>&nbsp;&nbsp;&nbsp;<button id='check-customer-out'>Checkout</button></td></tr>");
		 var $hotelRooms = $("<tr><td></td><td class='hotel-rooms' colspan='4'></td></tr>");
		 var $rooms = parseRooms(hotel);
		 console.log("ROOMS %o", $rooms);
		 $('.hotel-rooms', $hotelRooms).append($rooms);
		 $hotels.append($hotel);
		 $hotels.append($hotelRooms);
	}
	$('#customer-reservation').html($hotels);	
	 
 }
 
	function listMyReservation(customer_id) {
		var request = {
	 			m_request_type : "fetch-reservations",
	 			m_customer_id : customer_id
	 	}
		var ajaxRouter = new AjaxRouter();
		ajaxRouter.sendrequest(request, function(result){ 
			console.log("Got back %o", result);
			if(result.m_status_code < 0) {
				var error_message_div = $('#reserve-error-message');
				error_message_div.html("<div class='error_message'>" + result.m_status_msg + "</div>");
			} else {
				if(result.hotels.length == 0) {
				
					displayAlertMessage("There are no reservations under your name currently. Please press on the 'New Reservation' Tab to create one");
				} else {
					dispayReservations(result.hotels);
					 
				}
			}
		}); 
	}
 
 
	$(function() {
		var request = {
				m_request_type : "fetch-db"
			};
			var ajaxRouter = new AjaxRouter();
			ajaxRouter.sendrequest(request, function(result) {
				console.log("Got back %o", result);
				if (result.m_status_code < 0) {
					displayAlertMessage(result.m_status_msg);
				}  
			});
		 var customername = $( "#customer-name" );
		 customername.autocomplete({
		      source: function( request, response ) {
		    	  var request = {
							m_request_type : "customer-search",
							name : customername.val()
					}
					var ajaxRouter = new AjaxRouter();
					ajaxRouter.sendrequest(request, function(result){ 
						console.log("Got back %o", result);
						if(result.m_status_code < 0) {
							displayAlertMessage(result.m_status_msg );
							return;
						} 
						response( $.map( result.entities, function( item ) {
				              return {
				                label: item.Title,
				                value: item.Id
				              }
				        }));
					});
		      },
		      select: function( event, ui ) {
		    	event.preventDefault();
		        if(ui.item ) {
		        	customername.val(ui.item.label);
		        	listMyReservation(ui.item.value);
		        }
		      }
		    });
	});
 
</script>

</head>
<body>
<div class="imgcontainer">
    <img src="${pageContext.request.contextPath}/images/hotel-reservation-service.jpg" alt="Royal Hotel" class="logo" />
</div>
 
	<label for="customer-name: ">Enter customer name:&nbsp;&nbsp;</label><input id="customer-name" name="customer-name" type text></input>
	<div id="customer-reservation"></div>
</body>
</html>
 
 