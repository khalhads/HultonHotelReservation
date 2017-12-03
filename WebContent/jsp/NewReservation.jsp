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
	var countries = null;
	var states = null;
	var hotels = null;
	var rooms = null;
	var breakfasts = null;
	var services = null;
	
	var dialogReservation = null;
	var hotelChoice = null;
	var country_ids = null;
	var state_ids = null;
	var hotel_ids = null;
	var room_ids = null;
	var breakfast_ids = null;
	var service_ids = null;
	var dialogReservationHtml = null;
	
	var newreservationBtn = null;
	var chosen_hotel_id = null;
	
	function addCountriesChoices() {
			$.each(countries, function(key, value) {
			      country_ids.append("<option value='" + key + "'>"+ key + "</option>");
			});
			country_ids.on('change', function() {
				states = countries[this.value].states;
				state_ids.html();
				$.each(states, function(key, value) {
					state_ids.append("<option value='" + key + "'>"+ key + "</option>");
				});
			});
			state_ids.on('change', function() {
				hotels = states[this.value].hotels;
				hotel_ids.html();
				$.each(hotels, function(key, value) {
					var label =  value.street + ", " + value.city + ", " + value.state + ", " + value.country;
					var option = "<option value='" +  key + "'>" + label + "</option>";
					hotel_ids.append(option);
				});
			});
			hotel_ids.on('change', function() {
				chosen_hotel_id = this.value;
				rooms = hotels[this.value].rooms;
				breakfasts = hotels[this.value].breakfasts;
				services = hotels[this.value].services;
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
			} else
				countries = result.countries;
			addCountriesChoices();
			
		});
		newreservationBtn = $('#new-reservation');
		country_ids = $('#country_id');
		state_ids = $('#state_id');
		hotel_ids = $('#hotel_id');
		
		
		hotelChoice = $('#hotel-choice');
		dialogReservation = $('#dialog-reservation');
		dialogReservationHtml = dialogReservation.html();
		dialogReservation.html('');
		dialogReservation.hide();
		hotelChoice.show();
		
		newreservationBtn.click(function() {
			hotelChoice.hide();
			dialogReservation.show();
			
		 
			dialogReservation.html(dialogReservationHtml);
			
			room_ids = $('#room_id');
			breakfast_ids = $('#breakfast_id');
			service_ids = $('#service_id');
			
			room_ids.html('');
			breakfast_ids.html('');
			service_ids.html('');
			
			var completeBtn = $('#complete-reservation');
			var cancelBtn = $('#cancel-reservation');
			 
			
		 
			$.each(rooms, function(key, value) {
				var label =  "Room no: " +  value.room_no + ", " + value.floor_no + ", at $" + value.price + " with capcity" + value.max_people + '; ' + value.description;
				var option = "<option value='" +  key + "'>" + label + "</option>";
				room_ids.append(option);
			});
			$.each(breakfasts, function(key, value) {
				var label =  "Breakfast type: " + value.type + ", at $" + value.price + '; ' + value.description;
				var option = "<option value='" +  key + "'>" + label + "</option>";
				breakfast_ids.append(option);
			});
			$.each(services, function(key, value) {
				var label =  "Service type: " + value.type + ", at $" + value.price;
				var option = "<option value='" +  key + "'>" + label + "</option>";
				service_ids.append(option);
			});
			
			var reserveRoom = $('#reserve-room');
			var reservedRooms = $('#reserved-rooms');
			var reservedServices = null;
			var reservedBreakfasts = null;
			
			cancelBtn.click(function() {
				reservedRooms.html('');
				hotelChoice.show();
				dialogReservation.html('');
				dialogReservation.hide();
			});
			completeBtn.click(function() {
				var reservedRooms = $('.room-entry');
			 	var request = {
			 			m_request_type : "reserve"
			 			
			 	}
			 	var m_rooms = []
			 	for(var i=0; i< reservedRooms.length; i++) {
			 		var reservedRoom = $(reservedRooms[i]);
			 		var roomInfo = $('.room-info', reservedRoom);
			 		var breakfastInfo = $('.breakfasts', reservedRoom);
			 		var serviceInfo = $('.services', reservedRoom);
			 		var roomId = roomInfo.data('id');
			 		var roomCheckin = roomInfo.data('checkin');
			 		var roomCheckout = roomInfo.data('checkout');
			 		 
			 		var thisRoom = {
			 				id : "" + roomId,
			 				checkin : roomCheckin,
			 				checkout : roomCheckout
			 		};
			 		var roomBreakfasts = [];
			 		for(var b=0; b< breakfastInfo.length; b++) {
			 			var bentries = $('.breakfast-entry', breakfastInfo[b]);
			 			for(var be=0; be< bentries.length; be++) {
			 				var bentry = $(bentries[be]);
			 				console.log("breakfast id: " + bentry.data('id') + " Count: " + bentry.data('count'));
			 				roomBreakfasts.push({
			 					id : "" + bentry.data('id'),
			 					count : "" + bentry.data('count')
			 				});
			 			}
			 		}
			 		thisRoom.roomBreakfasts = roomBreakfasts;
			 		var roomServices = [];
			 		for(var s=0; s< serviceInfo.length; s++) {
			 			var sentries = $('.service-entry', serviceInfo[s]);
			 			for(var se=0; se< sentries.length; se++) {
			 				var sentry = $(sentries[se]);
			 				console.log("service id: " + sentry.data('id'));
			 				roomServices.push({
			 					id : "" + sentry.data('id')
			 				});
			 			}
			 		}
			 		thisRoom.roomServices = roomServices;
			 		m_rooms.push(thisRoom);
				}
			 	request.rooms = m_rooms;
			 	
				var card_owner 		= $('#card_owner').val();
				var card_no 		= $('#card_no').val();
				var card_type 		= $('#card_type').val();
				var security_code 	= $('#security_code').val();
				var expiration_date = $('#expiration_date').val();
				var billing_address = $('#billing_address').val();
				
				var cardInfo = {
						card_owner 		: card_owner,
						card_no 		: card_no,
						card_type 		: card_type,
						security_code 	: security_code,
						expiration_date : expiration_date,
						billing_address : billing_address
				};
					
				request.cardinfo = cardInfo;
				var ajaxRouter = new AjaxRouter();
				ajaxRouter.sendrequest(request, function(result){ 
					console.log("Got back %o", result);
					if(result.m_status_code < 0) {
						var error_message_div = $('#reserve-error-message');
						error_message_div.html("<div class='error_message'>" + result.m_status_msg + "</div>");
					} else {
						displayAlertMessage("Invoice number is " + result.m_invoice_id + ". Please keep for your reference" );
						reservedRooms.html('');
						hotelChoice.show();
						dialogReservation.html('');
						dialogReservation.hide();
					}
				}); 
			});
			 
			
			reserveRoom.click(function(){
				var room_id = room_ids.val();
				var room = rooms[room_id];
				var checkin = $('#checkin_date').val();
				var checkout = $('#checkout_date').val();
				
				var label =  "Room no: " +  room.room_no + ", " + room.floor_no + ", at $" + room.price +
							" with capcity" + room.max_people + ", Checkin on: " + checkin + ", Checkout on: " + checkout;
				 
			 	var roomEntry = `<tr>
			 						<td>
			 							<table class='room-entry' id='room-` + room.id + `'>
			 								<tr>
			 									<td class="room-info" colspan='5' data-id='` + room_id + 
			 											`' data-checkin='` + checkin + `' data-checkout='` + checkout + `'>` + label + `</td></tr> 
			 	  				 		    <tr>
			 	  				 		    	<td/><td>Breakfasts</td>
			 	  				 		    	<td colspan='3'>
			 	  				 		    		<table  class='breakfasts'></table>
			 	  				 		    	<td>
			 	  				 		    </tr> 
			 	     			 			<tr>
			 	     			 				<td/><td>Services</td>
			 	     			 				<td colspan='3'>
			 	     			 					<table  class='services'></table>
			 	     			 				<td>
			 	     			 			</tr>
			 	     			 		</table>
			 	     			 	</td>
			 	     			 </tr>`;
			 	     			 
			 	reservedRooms.append(roomEntry);
			 	var $roomEntry = $('#room-' + room.id);
			 	reservedServices = $('.services', $roomEntry);
			 	reservedBreakfasts = $('.breakfasts', $roomEntry);
			});
			
			var addBreakfast = $('#add-breakfast');
			var breakfastCount = $('#breakfast_count');
			addBreakfast.click(function(){
				var breakfast_id = breakfast_ids.val();
				var breakfast = breakfasts[breakfast_id];
				var count = breakfastCount.val();
				var label =  "Breakfast type: " + breakfast.type + ", at $" + breakfast.price + '; count ' + count;
			 	var td = "<tr><td class='breakfast-entry' colspan='5' data-count='" + count + "' data-id='" + breakfast_id + "'>" + label + "</td></tr>";
			 	reservedBreakfasts.append(td);
			});
			
			var addService = $('#add-service');
			 
			addService.click(function(){
				var service_id = service_ids.val();
				var service = services[service_id];
			 
				var label =  "Service type: " + service.type + ", at $" + service.price ;
			 	var td = "<tr><td class='service-entry' colspan='5' data-id='" + service_id + "'>" + label + "</td></tr>";
			 	reservedServices.append(td);
			});
		
		});
		
	});
</script>
<style>
body {
	 
	height: 100%;
}

#dialog-reservation {
	 
	height: 100%;
}
 
.add-button {
	width: 30px;
	height: 15px;
	padding: 2px;
    margin: 0;
}
.room-entry {
	border: 1px solid green;
} 
fieldset {
	width:95%;
}
</style>
</head>
<body>
	<div id='hotel-choice'>
		<fieldset class="ui-widget ui-widget-content ui-corner-all">
			<legend class="ui-widget ui-widget-header ui-corner-all">Please
				select a hotel</legend>
			<table>
				<tr>
					<td><label class="label" for='country_id'> Country </label> <select
						id='country_id' name='country_id' required>
							<option value='0'>None</option>
					</select></td>
					<td><label class="label" for='state_id'> State</label> <select
						id='state_id' name='state_id' required>
							<option value='0'>None</option>
					</select></td>
					<td><label class="label" for='hotel_id'> Hotel</label> <select
						id='hotel_id' name='hotel_id' required>
							<option value='0'>None</option>
					</select></td>
				</tr>
			</table>
			<button id="new-reservation">Reserve</button>
		</fieldset>
	</div>

	<div id='dialog-reservation'>

		<fieldset>
			<legend>Room Option</legend>
			<table>
				<tr>
					<td><label class="label" for='room_id'> Room </label></td>
					<td colspan="2"><select id='room_id' name='room_id' required>
							<option value='0'>None</option>
					</select></td>
				</tr>
				<tr>
					<td><label class="label" for='checkin_date'> Checkin
							Date </label></td>
					<td><input type="date" class='input-field' name='checkin_date'
						id='checkin_date' required /></td>
					<td><label class="label" for='checkout_date'>Checkout
							Date </label></td>
					<td><input type="date" class='input-field'
						name='checkout_date' id='checkout_date' required /></td>
						<td><button class='add-button' id='reserve-room'>Add </button></td>
				</tr>
			</table>
			<table id="reserved-rooms"> </table>
		</fieldset>
		<div id="reserve-error-message"> </div>
		<fieldset>
			<legend>Breakfast Option</legend>

			<table>
				<tr>
					<td><label class="label" for='breakfast_id'> Breakfast
					</label></td>
					<td colspan="2"><select id='breakfast_id' name='breakfast_id'
						required>
							<option value='0'>None</option>
					</select></td>
					<td><label class="label" for='breakfast_count'> Number of orders </label></td>
					<td><input maxlength="4" size="4" type='text' id='breakfast_count' name='breakfast_count' required/> </td>
					<td><button class='add-button' id='add-breakfast'>Add</button></td>
				</tr>
				
				
			</table>
			 
		</fieldset>
		<fieldset>
			<legend>Service Option</legend>

			<table>
				<tr>
					<td><label class="label" for='service_id'> Service </label></td>
					<td colspan="2"><select id='service_id' name='service_id'
						required>
							<option value='0'>None</option>
					</select></td>
					<td><button class='add-button' id='add-service'>Add</button></td>
				</tr>
			</table>
			 
		</fieldset>
		<fieldset>
			<legend>Creadit Card Info</legend>

			<table>
				<tr>
					<td><label class="label" for='card_owner'>Name on Card</label></td>
					<td><input maxlength="25" size="18"  type='text' class='input-field' name='card_owner'
						id='card_owner' required /></td>
					<td><label class="label" for='card_no'> Card No </label></td>
					<td><input  maxlength="25" size="18" type='text' class='input-field' name='card_no'
						id='card_no' required /></td>
					<td><label class="label" for='card_type'> Card Type </label> <select
						id='card_type' name='card_type' required>
							<option value='1'>AmericanExpress</option>
							<option value='2'>Visa</option>
							<option value='3'>MasterCard</option>
							<option value='1'>Discover</option>
					</select></td>
					<td><label class="label" for='security_code'>Security
							code</label></td>
					<td><input maxlength="4" size="4"  type='text' class='input-field'
						name='security_code' id='security_code' required /></td>
					<td><label class="label" for='expiration_date'>Expiration
							Date </label></td>
					<td><input type="date" class='input-field'
						name='expiration_date' id='expiration_date' required /></td>
				</tr>
			</table>
			<p class='data-entry'>
				<label class="label" for='billing_address'> Billing Address </label>
				<input type='text' class='input-field' name='billing_address'
					id='billing_address' required />
			</p>
		</fieldset>



		<button id='complete-reservation'>Complete</button>
		<button id='cancel-reservation' class="cancelbtn">Cancel</button>
	</div>
</body>
</html>
