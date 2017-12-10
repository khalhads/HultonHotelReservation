package com.HultonHotelReservation.Impl;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.HultonHotelReservation.DBAccess;
import com.HultonHotelReservation.UserAuth;
import com.HultonHotelReservation.dbaccess.Admin;
import com.HultonHotelReservation.dbaccess.Customer;


public class SigninRequest {
 	public SigninRequest() {
  
 	}
 	
 	public JSONObject processSignRequest(HttpSession session, Connection connection, String pageType,  JSONObject jsonrequest)
 			throws Exception {
 		JSONObject response = new JSONObject();
 		String username = (String) jsonrequest.get("username");
		String password = (String) jsonrequest.get("password");
		UserAuth userAuth = new UserAuth();
		 
		if ("adminsign".equals(pageType)) {
			List<Admin> adminList = Admin.fetchWithJoin(connection, " where t.login_id = ? and t.password = ?",
					username, password);
			System.out.println("customerList " + adminList);
			if (adminList == null || adminList.size() == 0) {
				
				response.put("m_status_code", -1);
				response.put("m_status_msg", "<div class='error_message'>Failed to login. Please try again</div>");
				 
				return response;
			}
		 
			Admin  admin = adminList.get(0);

			if (password.equals(admin.getPassword()) && username.equals(admin.getLoginId()))
			{
				
				userAuth.setUserId(admin.getId());
				userAuth.setLoginId(username);
				
				session.setAttribute("auth", userAuth);
				
				response.put("m_status_code", 1);
				response.put("m_status_msg", "");
				
			} else {
				response.put("m_status_code", -1);
				response.put("m_status_msg", "<div class='error_message'>Failed to login. Please try again</div>");	
				return response;
			}	
		}
		
		
		if ("signin".equals(pageType)) {
			
		 
			List<Customer> customerList = Customer.fetchWithJoin(connection, " where t.login_id = ? and t.password = ?",
					username, password);
			System.out.println("customerList " + customerList);
			if (customerList == null || customerList.size() == 0) {
				
				response.put("m_status_code", -1);
				response.put("m_status_msg", "<div class='error_message'>Failed to login. Please press the Signup button if you are not registered</div>");
				 
				return response;
			}
			System.out.println("customerList " + customerList);
			Customer customer = customerList.get(0);

			if (password.equals(customer.getPassword()) && username.equals(customer.getLoginId()))
			{
				
				userAuth.setUserId(customer.getId());
				userAuth.setLoginId(username);
				
				session.setAttribute("auth", userAuth);
				
				response.put("m_status_code", 1);
				response.put("m_status_msg", "");
				
			} else {
				response.put("m_status_code", -1);
				response.put("m_status_msg", "<div class='error_message'>Failed to login. Please press the Signup button if you are not registered</div>");	
				return response;
			}	
		}
		
		if ("signup".equals(pageType)) {
			String address = (String) jsonrequest.get("address");
			String passwordRepeat = (String) jsonrequest.get("passwordRepeat");
			String firstname = (String) jsonrequest.get("firstname");
			String lastname = (String) jsonrequest.get("lastname");
			
			Customer customer = new Customer();
			customer.setFirstName(firstname);
			customer.setLastName(lastname);
			customer.setAddress(address);
			customer.setLoginId(username);
			customer.setPassword(password);
			
			int user_id = customer.insertRecord(connection);

			
			userAuth.setUserId(user_id);
			userAuth.setLoginId(username);
			
			session.setAttribute("auth", userAuth);

			response.put("m_status_code", 1);
			response.put("m_status_msg", "");
		}
		
		
		return response; 
	}

}
