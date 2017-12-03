package com.HultonHotelReservation.dbaccess;


import java.io.PrintStream;
import java.sql.CallableStatement;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;
import java.util.Date;
 

public class Creditcard extends BASESqlInterface {
	static Creditcard instance = new Creditcard();
	private int  m_id;
	private String  m_card_number;
	private String  m_person_name;
	private String  m_card_type;
	private String  m_billing_address;
	private String  m_expiration_date;
	private String  m_security_code;

	public int  getId () {
		return m_id;
	}

	public void  setId (int val) {
		m_id = val;
	}

	public String  getCardNumber () {
		return m_card_number;
	}

	public void  setCardNumber (String val) {
		m_card_number = val;
	}

	public String  getPersonName () {
		return m_person_name;
	}

	public void  setPersonName (String val) {
		m_person_name = val;
	}

	public String  getCardType () {
		return m_card_type;
	}

	public void  setCardType (String val) {
		m_card_type = val;
	}

	public String  getBillingAddress () {
		return m_billing_address;
	}

	public void  setBillingAddress (String val) {
		m_billing_address = val;
	}

	public String  getExpirationDate () {
		return m_expiration_date;
	}

	public void  setExpirationDate (String val) {
		m_expiration_date = val;
	}

	public String  getSecurityCode () {
		return m_security_code;
	}

	public void  setSecurityCode (String val) {
		m_security_code = val;
	}

	 
	 
	public static Creditcard getInstance() {
		return instance;
	}
	 
	 
 

	public boolean deleteById(Connection conn) {
		
		String stmt = "DELETE FROM CreditCard WHERE ID = ?";
		CallableStatement cs = null;
	 
		try {
			cs = conn.prepareCall(stmt);

	        cs.setInt(1, m_id);
 
			cs.execute();
			return true;
		} catch (Exception e) {
			System.err.println("Failed to execute: [" + stmt + "], exception: " + e);
			return false;
		} finally {
			closeJdbcResources(null, cs, null);
		}
	}
	
	static public List<Creditcard> fetchWithJoin(Connection conn, String joindAndWhereStr, String ... params) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT t.id, t.card_number, t.person_name, t.card_type, t.billing_address, t.expiration_date, t.security_code FROM CreditCard t ");
		sb.append(joindAndWhereStr );
		
		String stmt = sb.toString();
		PreparedStatement cs = null;
		ResultSet rs = null;

		try {
			cs = conn.prepareStatement(stmt);
        	if(params.length > 0) {
				int idx = 1;
				for(String arg : params)
					cs.setString(idx++, arg);
			}
			rs = cs.executeQuery();
			ArrayList<Creditcard> list = new ArrayList<Creditcard>();
			while(rs.next()) {
				Creditcard obj = instance.getNextRow(rs, 1);
				// obj.printRow(System.out);
				list.add(obj);
			}
			return list;
		} catch (Exception e) {
			System.err.println("Failed to execute: [" + stmt + "], exception: " + e);
			return null;
		}  finally {
			closeJdbcResources(null, cs, null);
		}
	}

 
	static public Creditcard fetchById(Connection conn, String id) {
		String stmt =  "SELECT t.id, t.card_number, t.person_name, t.card_type, t.billing_address, t.expiration_date, t.security_code FROM CreditCard t  WHERE ID = ?";
		PreparedStatement cs = null;
		ResultSet rs = null;

		try {
			cs = conn.prepareStatement(stmt);
	        cs.setString(1, id);
            rs = cs.executeQuery();
			Creditcard newobj = null;
            if(rs.next()) {
            	newobj = instance.getNextRow(rs, 1);
            }
			return newobj;
		} catch (Exception e) {
			System.err.println("Failed to execute: [" + stmt + "], exception: " + e);
			return null;
		} finally {
			closeJdbcResources(null, cs, rs);
		}
	}
	 
	 
	public  int insertRecord(Connection connection) throws SQLException {
	    PreparedStatement cs = null;
		String stmt = "INSERT into CreditCard(id, card_number, person_name, card_type, billing_address, expiration_date, security_code ) VALUES (?,?,?,?,?,?,?)";
	    try {
	        cs = connection.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			int idx = 1;
			       	cs.setInt(idx++, m_id);
	    			       	cs.setString(idx++, m_card_number);
	    			       	cs.setString(idx++, m_person_name);
	    			       	cs.setString(idx++, m_card_type);
	    			       	cs.setString(idx++, m_billing_address);
	    			       	cs.setString(idx++, m_expiration_date);
	    			       	cs.setString(idx++, m_security_code);
	    		 	cs.executeUpdate();
		 	int autoIncKeyFromApi = -1;

		    ResultSet rs = cs.getGeneratedKeys();
		
		    if (rs != null && rs.next()) {
		        autoIncKeyFromApi = rs.getInt(1);
		    } else {
		
		        System.err.println("Failed to execute: retrieve GeneratedKeys ");
		        autoIncKeyFromApi = 0;
		    }
			return autoIncKeyFromApi;
	    } catch (Exception e) {
			System.err.println("Failed to execute: [" + stmt + "], exception: " + e);
			return -1;
	    } finally {
	        closeJdbcResources(null, cs, null);
	    }
	}
 
	public void printRow(PrintStream out) throws SQLException {
				out.println("id = " + m_id);
				out.println("card_number = " + m_card_number);
				out.println("person_name = " + m_person_name);
				out.println("card_type = " + m_card_type);
				out.println("billing_address = " + m_billing_address);
				out.println("expiration_date = " + m_expiration_date);
				out.println("security_code = " + m_security_code);
	}
	 public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("id = " + m_id);
		sb.append(";card_number = " + m_card_number);
		sb.append(";person_name = " + m_person_name);
		sb.append(";card_type = " + m_card_type);
		sb.append(";billing_address = " + m_billing_address);
		sb.append(";expiration_date = " + m_expiration_date);
		sb.append(";security_code = " + m_security_code);
		return sb.toString();
    }

 
	public  Creditcard getNextRow(ResultSet rs, int idx) throws SQLException {
			Creditcard obj = new Creditcard();
			 
			obj.m_id =  rs.getInt(idx++);
			obj.m_card_number =  rs.getString(idx++);
			obj.m_person_name =  rs.getString(idx++);
			obj.m_card_type =  rs.getString(idx++);
			obj.m_billing_address =  rs.getString(idx++);
			obj.m_expiration_date =  rs.getString(idx++);
			obj.m_security_code =  rs.getString(idx++);
			 
			return obj;
	}
}


