package com.HultonHotelReservation;

import java.io.IOException;
import java.text.ParseException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
 
/**
 * Servlet implementation class AjaxJsonRequest
 */
@WebServlet("/AjaxJsonRequest")
public class AjaxJsonRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static JSRequestRouter jsRequestRouter = new JSRequestRouter();
	private static JSONObject getFieldParameters(String jsonstr) {

		JSONParser parser = new JSONParser();
		JSONObject jsonrequest = null;
		if (jsonstr == null)
			jsonstr = "{}";

		try {
			jsonrequest = (JSONObject) parser.parse(jsonstr);
		} catch (Exception e) {
			jsonrequest = new JSONObject();
		}
		return jsonrequest;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		Enumeration<String> params = request.getParameterNames(); 
		while(params.hasMoreElements()){
		 String paramName = params.nextElement();
		 System.out.println("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
		}
		System.out.println("calling JsonRequetHandler.process");
		String jsonstr = request.getParameter("REQUEST_JSON");
		if(jsonstr == null) {
			request.getRequestDispatcher("/jsp/signup.jsp").forward(request, response);
			return;
		}
		JSONObject jsonrequest = getFieldParameters(jsonstr);

		jsRequestRouter.process(request, response, session,  jsonrequest);
	}
}
