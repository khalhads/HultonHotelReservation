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
 

public class Hoteladdress extends BASESqlInterface {
	static Hoteladdress instance = new Hoteladdress();
	private int  m_id;
	private int  m_hotel_id;
	private String  m_street;
	private String  m_city;
	private String  m_state;
	private String  m_country;
	private String  m_zip;

	public int  getId () {
		return m_id;
	}

	public void  setId (int val) {
		m_id = val;
	}

	public int  getHotelId () {
		return m_hotel_id;
	}

	public void  setHotelId (int val) {
		m_hotel_id = val;
	}

	public String  getStreet () {
		return m_street;
	}

	public void  setStreet (String val) {
		m_street = val;
	}

	public String  getCity () {
		return m_city;
	}

	public void  setCity (String val) {
		m_city = val;
	}

	public String  getState () {
		return m_state;
	}

	public void  setState (String val) {
		m_state = val;
	}

	public String  getCountry () {
		return m_country;
	}

	public void  setCountry (String val) {
		m_country = val;
	}

	public String  getZip () {
		return m_zip;
	}

	public void  setZip (String val) {
		m_zip = val;
	}

	 
	 
	public static Hoteladdress getInstance() {
		return instance;
	}
	 
 
	static public List<Hoteladdress> fetchWithJoin(Connection conn, String joindAndWhereStr, String ... params) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT t.id, t.hotel_id, t.street, t.city, t.state, t.country, t.zip FROM HotelAddress t ");
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
			ArrayList<Hoteladdress> list = new ArrayList<Hoteladdress>();
			while(rs.next()) {
				Hoteladdress obj = instance.getNextRow(rs, 1);
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

 
	static public Hoteladdress fetchById(Connection conn, String id) {
		String stmt =  "SELECT t.id, t.hotel_id, t.street, t.city, t.state, t.country, t.zip FROM HotelAddress t  WHERE ID = ?";
		PreparedStatement cs = null;
		ResultSet rs = null;

		try {
			cs = conn.prepareStatement(stmt);
	        cs.setString(1, id);
            rs = cs.executeQuery();
			Hoteladdress newobj = null;
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
		String stmt = "INSERT into HotelAddress(id, hotel_id, street, city, state, country, zip ) VALUES (?,?,?,?,?,?,?)";
	    try {
	        cs = connection.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			int idx = 1;
			       	cs.setInt(idx++, m_id);
	    			       	cs.setInt(idx++, m_hotel_id);
	    			       	cs.setString(idx++, m_street);
	    			       	cs.setString(idx++, m_city);
	    			       	cs.setString(idx++, m_state);
	    			       	cs.setString(idx++, m_country);
	    			       	cs.setString(idx++, m_zip);
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
				out.println("hotel_id = " + m_hotel_id);
				out.println("street = " + m_street);
				out.println("city = " + m_city);
				out.println("state = " + m_state);
				out.println("country = " + m_country);
				out.println("zip = " + m_zip);
	}
	 public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("id = " + m_id);
		sb.append(";hotel_id = " + m_hotel_id);
		sb.append(";street = " + m_street);
		sb.append(";city = " + m_city);
		sb.append(";state = " + m_state);
		sb.append(";country = " + m_country);
		sb.append(";zip = " + m_zip);
		return sb.toString();
    }

 
	public  Hoteladdress getNextRow(ResultSet rs, int idx) throws SQLException {
			Hoteladdress obj = new Hoteladdress();
			 
			obj.m_id =  rs.getInt(idx++);
			obj.m_hotel_id =  rs.getInt(idx++);
			obj.m_street =  rs.getString(idx++);
			obj.m_city =  rs.getString(idx++);
			obj.m_state =  rs.getString(idx++);
			obj.m_country =  rs.getString(idx++);
			obj.m_zip =  rs.getString(idx++);
			 
			return obj;
	}
}


