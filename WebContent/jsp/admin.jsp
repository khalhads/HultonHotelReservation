<!DOCTYPE html>
<html>
<head>
 
<title>
Hotel Reservation System
</title>
<link type = "text/css"  rel="stylesheet" href="${pageContext.request.contextPath}/css/HotelReservation.css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/lib/jquery-3.2.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/AjaxRouter.js"></script>


<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery-ui-1.12.1/jquery-ui.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery-ui-1.12.1/jquery-ui.theme.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery-ui-1.12.1/jquery-ui.css">
 
<script src="${pageContext.request.contextPath}/jquery-ui-1.12.1/jquery-ui.js" type="text/javascript"></script>
 



 <script>
// Get the modal
var modal = document.getElementById('sign-up');

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
};

function signMe(signform, request) {
	console.log("signUpobj %o", request);
	var ajaxRouter = new AjaxRouter();
	ajaxRouter.sendrequest(request, function(result){ 
		console.log("Got back %o", result);
		if(result.m_status_code < 0) {
			var form_error_message = $('#form-error_message', signform);
			form_error_message.html(result.m_status_msg);
		} else   window.location.href = "${pageContext.request.contextPath}/jsp/adminindex.jsp";
	});
	
}

$(function(){
	 
	
	$("#signinbtn").click(function(event){
		var signinform = $('#signin-form');
		var request = {

				  "username" : $("input[name='username']", signinform).val(),
				  "password" : $("input[name='password']", signinform).val(),
				  m_request_type : "adminsign"
		};;
		signMe(signinform, request);
	});
});
</script> 
<style>
body {
	//width :30%;
}
#hulton-image {
	position:absolute;
	width:99%;
	height:99%;
	z-index:-999;
}
#signin-form {
	width:400px !important;
	//height:50%;
	border: 5px double white;
	float:left;
	 display: inline-block;
}
#welcome {
    margin:30px;
     
	float:center;
	 display: inline-block;
    background-color: green;
	font-size:72px;
	color:GoldenRod;
	margin-left: auto;
  margin-right: auto;
     
    white-space: nowrap;
    text-align:center;
	
}
#login-header{
font-size:72px;
text-align:center;
font-weight: bold;
font-size:28px;
}

#showsignupbtn {
	float : right;
}
</style>
</head>
<body>
  <div class="imgcontainer">
    <img src="${pageContext.request.contextPath}/images/Hulton.jpg" alt="Royal Hotel" id="hulton-image" />
  </div>
  

  
 <div id="signin-form">
  
  <div class="container">
    <div id="login-header"> Hulton Admin Page</div>
    
    <input type="text" placeholder="Enter Email" name="username" required>

    
    <input type="password" placeholder="Enter Password" name="password" required>
	<div id="form-error_message"></div> 
    <button  class="okbtn" id="signinbtn">Login</button>   
    
	 
  </div>

 
</div> 
    
    <div id="welcome"> Welcome to the Hulton Hotel</div>
 


 
</body>
</html> 
 