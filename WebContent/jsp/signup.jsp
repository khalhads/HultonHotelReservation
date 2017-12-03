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
		} else   window.location.href = "${pageContext.request.contextPath}/jsp/index.jsp";
	});
	
}

$(function(){
	$("#signupbtn").click(function(event){
		var signupform = $('#signup-form');
		var request = {
				  "page-type" : $("input[name='page-type']", signupform).val(),
				  "firstname" : $("input[name='firstname']", signupform).val(),
				  "lastname" : $("input[name='lastname']", signupform).val(),
				  "address" : $("input[name='address']", signupform).val(),
				  "username" : $("input[name='username']", signupform).val(),
				  "password" : $("input[name='password']", signupform).val(),
				  "passwordRepeat" : $("input[name='passwordRepeat']", signupform).val(),
				  m_request_type : "signup"
		};
		signMe(signupform, request)
	});
	
	$("#signinbtn").click(function(event){
		var signinform = $('#signin-form');
		var request = {

				  "username" : $("input[name='username']", signinform).val(),
				  "password" : $("input[name='password']", signinform).val(),
				  m_request_type : "signin"
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
    <div id="login-header"> Customer Login/Signup Form</div>
    
    <input type="text" placeholder="Enter Email" name="username" required>

    
    <input type="password" placeholder="Enter Password" name="password" required>
	<div id="form-error_message"></div> 
    <button  class="okbtn" id="signinbtn">Login</button>   
    
	<button  class="okbtn"  id="showsignupbtn" onclick="document.getElementById('sign-up').style.display='block'">Sign Up</button>
  </div>

 
</div> 
    
    <div id="welcome"> Welcome to the Hulton Hotel</div>
 


<!-- The Modal (contains the Sign Up form) -->
<div id="sign-up" class="modal">
 
  <span onclick="document.getElementById('sign-up').style.display='none'" class="close" title="Close Modal">&times;</span>
  <div id="signup-form" class="modal-content animate">
    <input type="hidden" name="page-type" value="signup">
    <div class="container">
    <label><b>First Name</b></label>
      <input type="text" placeholder="Enter First Name" name="firstname" required>
       <label><b>Last Name</b></label>
      <input type="text" placeholder="Enter Last Name" name="lastname" required>
      <label><b>Address</b></label>
      <input type="text" placeholder="Enter address" name="address" required>
      <label><b>Email</b></label>
      <input type="text" placeholder="Enter Email" name="username" required>

      <label><b>Password</b></label>
      <input type="password" placeholder="Enter Password" name="password" required>

      <label><b>Repeat Password</b></label>
      <input type="password" placeholder="Repeat Password" name="passwordRepeat" required>
      <input type="checkbox" checked="checked"> Remember me
      <p>By creating an account you agree to our <a href="#">Terms & Privacy</a>.</p>
 	
 	  <div id="form-error_message"></div> 
      <div class="clearfix">
        <button  class="okbtn" id="signupbtn">Sign Up</button>
        <button type="button" onclick="document.getElementById('sign-up').style.display='none'" class="cancelbtn">Cancel</button>
        
      </div>
    </div>
  </div>
</div> 
</body>
</html> 
 