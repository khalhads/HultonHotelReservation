
package com.HultonHotelReservation;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.HultonHotelReservation.Impl.SigninRequest;
import com.HultonHotelReservation.dbaccess.BASESqlInterface;
import com.fasterxml.jackson.databind.ObjectMapper;


/* This is the JSRequestRouter class that is used in the AjaxJsonRequest class. This class holds an object of type Logger that is used to hold the request.
 */
public class JSRequestRouter {

	private static final Logger logger = LogManager.getLogger("AjaxJsonRequest");
	private static final long serialVersionUID = 1L;
	private static String REQUEST_JSON = "REQUEST_JSON";
	private static JSONParser parser = new JSONParser();

	/*
	 * Public method JSRequestRouter takes no parameters and prints out the statement initializing Request.
	 */
	public JSRequestRouter() {
		System.out.println("Initializing AjaxRequest ");
	}


	/* public method process, returns void, takes the following parameters HttpServletRequest request, HttpServletResponse response, HttpSession session,  JSONObject jsonrequest
	 *   */
	public void process(HttpServletRequest request, HttpServletResponse response, HttpSession session,  JSONObject jsonrequest)
			throws IOException {
		/*
		 * ObjectMapper class provides functionality for reading and writing JSON, either to and from basic POJOs (Plain Old Java Objects), 
		 * or to and from a general-purpose JSON Tree Model (JsonNode), as well as related functionality for performing conversions.
		 * It is also highly customizable to work both with different styles of JSON content, and to support more advanced Object concepts
		 * such as polymorphism and Object identity.
		 */
		ObjectMapper mapper = new ObjectMapper();

		// Set response type to JSON
		/* 
		 * The HttpServletResponse class Extends the ServletResponse interface to provide HTTP-specific functionality in sending a response.
		 *  For example, it has methods to access HTTP headers and cookies. Here we are using the .setContentType(String type) method which is used to 
		 *  set the content type of the response being sent to the client, if the response has not been committed yet. The given content type may include
		 *  a character encoding specification, for example, text/html;charset=UTF-8. The response's character encoding is only set from the given content 
		 *  type if this method is called before getWriter is called.
		 */
		response.setContentType("application/json");
		
		/*
		 * JsonObject class represents an immutable JSON object value, here we are creating a Json object and setting it equal to null.
		 */
		JSONObject jsonResponse = null;
		try {

			/*
			 * now we are using the execute method defined below and passing in all the parameters (request, response,session, jsonrequest) that are passed in when the 
			 * process function is called. See execute_method_definition below for more details.
			 */
			jsonResponse = execute(request, response,session, jsonrequest);

			logger.info("Returning Json  result  " + jsonResponse);

			// Send result as JSON to client
			mapper.writeValue(response.getOutputStream(), jsonResponse);
		} catch (Exception e) {
			jsonResponse = new JSONObject();
			jsonResponse.put("m_request_type", -1);
			jsonResponse.put("m_status_code", -1);
			jsonResponse.put("m_status_msg", "" + e);
			mapper.writeValue(response.getOutputStream(), jsonResponse);
		}
	}

		/* execute_method_definition
		 * This method takes the parameters (HttpServletRequest request, HttpServletResponse response, HttpSession session,  JSONObject jsonrequest) that were first
		 * passed in to the process function. Inside the execute method we first create a JSONObject called jsonResponse and set it to null, then a Connection 
		 * object called connection and set it to null as well, and finally a String called request_type and set it to null. 
		 */
	public JSONObject execute(HttpServletRequest request, HttpServletResponse response, HttpSession session, JSONObject jsonrequest) {

		JSONObject jsonResponse = null;
		Connection connection = null;
		String request_type = null;
		/*
		 * Now we set the String request_type equal to the value passed by jsonrequest.get("m_request_type") after we cast it to a type String. The JSONObject class 
		 * inherits the .get function from the HashMap class which the specified key is mapped, or null if this map contains no mapping for the key
		 */
		try {
			request_type = (String) jsonrequest.get("m_request_type");

			logger.info("Getting  connection ... ");
			connection = DBAccess. getDataSource().getConnection();
			UserAuth auth = (UserAuth) session.getAttribute("auth");

			
			switch (request_type) {
			
				case "adminsign":
				case "signin":
				case "signup": 
					SigninRequest req = new SigninRequest();
					jsonResponse = req.processSignRequest(session, connection, request_type, jsonrequest);
					break;
				case "fetch-db":
					jsonResponse = DBAccess.getHotelDb();
					break;
				case "logout":
					jsonResponse = new JSONObject();
					jsonResponse.put("m_status_code", 1);
					jsonResponse.put("m_status_msg", "");
					break;
				case "reserve":
					jsonResponse = DBAccess.reserveRequest(session, connection, request_type, jsonrequest);
					break;	
				case "save-review":
					jsonResponse = DBAccess.saveReview(session, connection, request_type, jsonrequest);
					break;
				case "check-room-reserved":
					jsonResponse = DBAccess.checkIfRoomReserved(session, connection, request_type, jsonrequest);
					break;
				case "customer-search":
					jsonResponse = DBAccess.searchForCustomer(session, connection, request_type, jsonrequest);
					break;
				case "fetch-reservations":
					jsonResponse = DBAccess.populateReservations(session, connection, jsonrequest);
					break;	
				default:
					throw new Exception("m_request_type = " + request_type + " is not supported");
			}

		} catch (Exception e) {
			jsonResponse = new JSONObject();
			jsonResponse.put("m_status_code", -1);
			jsonResponse.put("m_status_msg", e.getMessage());

		} finally {
			if (connection != null) {
				BASESqlInterface.closeJdbcResources(connection, null, null);
			}
		}
		if (jsonResponse == null) {
			jsonResponse = new JSONObject();
			jsonResponse.put("m_status_code", -1);
			jsonResponse.put("m_status_msg", "Execution failed of request: " + request_type);
		}
		jsonResponse.put("m_request_type", request_type);

		return jsonResponse;
	}
}
