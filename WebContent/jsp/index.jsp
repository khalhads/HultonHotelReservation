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
 
<style type="text/css">
 
#main-tabs {
	position: fixed;
	top: 135px;
	left: -0;
	right: 10px;
	bottom: 5px;
}
 
#main-content {
	//position: relative;
	//top: -10px;
	//left: -15px;
	//right: 15px;
	height: 100%;
	//width: 100%;
}

.logout {
   float:right!important;
}
.nowrap {
	white-space: nowrap;
}

body {

	overflow: hidden;
}

.ui-tabs .ui-tabs-nav li {
	height: 20px;
	font-size: 16px;
}

.ui-tabs .ui-tabs-nav li a {
	position: relative;
	top: -10px;
	padding: 0 1em;
}
</style>

<script type="text/javascript">
	var maintabs = null;
 
	$(function() {
	 
		maintabs = $("#main-tabs");
		$("#main-loading").hide();
		maintabs.show();
		// alert("cacheobj:" +top.cacheobj.toSource());
		maintabs.tabs();
		$('#logout').click(function() {
			var request = {
		 			m_request_type : "logout"
		 	}
			 var ajaxRouter = new AjaxRouter();
	        ajaxRouter.sendrequest(request, function(result){
	                console.log("Got back %o", result);
	                window.location.href = "${pageContext.request.contextPath}/jsp/signup.jsp";
	        });

		});
	});
 
</script>

</head>
<body>
<div class="imgcontainer">
    <img src="${pageContext.request.contextPath}/images/hotel-reservation-service.jpg" alt="Royal Hotel" class="logo" />
</div>
	<div id="main-tabs">
		<ul>
			<li><a href="#new-reservation">New Reservation</a></li>
			<li><a href="#list-reservation">List Reservation</a></li>
		
			<li class="logout"><a id="logout" href="#';">Logout</a></li>
			 
		</ul>
		<div id="main-content">
 			<iframe id="new-reservation" name="new-reservation"
						src="${pageContext.request.contextPath}/jsp/NewReservation.jsp" width="100%" height="97%"  frameBorder="0" />
			</iframe>
			  
			<iframe id="list-reservation" name="list-reservation"
						src="${pageContext.request.contextPath}/jsp/ListReservations.jsp"  width="100%" height="97%" frameBorder="0" />
			</iframe>
		</div>
	</div>
	 
</body>
</html>
 
 