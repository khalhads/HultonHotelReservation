package com.HultonHotelReservation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserAuth {
	private static final Logger logger = LogManager.getLogger("UserAuth");
 

	private String loginId;
	private int user_id;
	
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public int getUserId() {
		return user_id;
	}
	public void setUserId(int user_id) {
		this.user_id = user_id;
	}
	
  
}
