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
 

public class Roomoption extends BASESqlInterface {
	static Roomoption instance = new Roomoption();
	private int  m_id;
	private int  m_room_id;
	private int  m_discount_id;
	private int  m_reservation_id;
	private Date  m_checkin;
	private Date  m_checkout;

	public int  getId () {
		return m_id;
	}

	public void  setId (int val) {
		m_id = val;
	}

	public int  getRoomId () {
		return m_room_id;
	}

	public void  setRoomId (int val) {
		m_room_id = val;
	}

	public int  getDiscountId () {
		return m_discount_id;
	}

	public void  setDiscountId (int val) {
		m_discount_id = val;
	}

	public int  getReservationId () {
		return m_reservation_id;
	}

	public void  setReservationId (int val) {
		m_reservation_id = val;
	}

	public Date  getCheckin () {
		return m_checkin;
	}

	public void  setCheckin (Date val) {
		m_checkin = val;
	}

	public Date  getCheckout () {
		return m_checkout;
	}

	public void  setCheckout (Date val) {
		m_checkout = val;
	}

	 
	 
	public static Roomoption getInstance() {
		return instance;
	}
	 
 
	static public List<Roomoption> fetchWithJoin(Connection conn, String joindAndWhereStr, String ... params) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT t.id, t.room_id, t.discount_id, t.reservation_id, t.checkin, t.checkout FROM RoomOption t ");
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
			ArrayList<Roomoption> list = new ArrayList<Roomoption>();
			while(rs.next()) {
				Roomoption obj = instance.getNextRow(rs, 1);
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

 
	static public Roomoption fetchById(Connection conn, String id) {
		String stmt =  "SELECT t.id, t.room_id, t.discount_id, t.reservation_id, t.checkin, t.checkout FROM RoomOption t  WHERE ID = ?";
		PreparedStatement cs = null;
		ResultSet rs = null;

		try {
			cs = conn.prepareStatement(stmt);
	        cs.setString(1, id);
            rs = cs.executeQuery();
			Roomoption newobj = null;
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
		String stmt = "INSERT into RoomOption(id, room_id, discount_id, reservation_id, checkin, checkout ) VALUES (?,?,?,?,?,?)";
	    try {
	        cs = connection.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			int idx = 1;
			       	cs.setInt(idx++, m_id);
	    			       	cs.setInt(idx++, m_room_id);
	    			       	cs.setInt(idx++, m_discount_id);
	    			       	cs.setInt(idx++, m_reservation_id);
	    			       	cs.setDate(idx++, (java.sql.Date) m_checkin);
	    			       	cs.setDate(idx++, (java.sql.Date) m_checkout);
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
				out.println("room_id = " + m_room_id);
				out.println("discount_id = " + m_discount_id);
				out.println("reservation_id = " + m_reservation_id);
				out.println("checkin = " + m_checkin);
				out.println("checkout = " + m_checkout);
	}
	 public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("id = " + m_id);
		sb.append(";room_id = " + m_room_id);
		sb.append(";discount_id = " + m_discount_id);
		sb.append(";reservation_id = " + m_reservation_id);
		sb.append(";checkin = " + m_checkin);
		sb.append(";checkout = " + m_checkout);
		return sb.toString();
    }

 
	public  Roomoption getNextRow(ResultSet rs, int idx) throws SQLException {
			Roomoption obj = new Roomoption();
			 
			obj.m_id =  rs.getInt(idx++);
			obj.m_room_id =  rs.getInt(idx++);
			obj.m_discount_id =  rs.getInt(idx++);
			obj.m_reservation_id =  rs.getInt(idx++);
			obj.m_checkin =  rs.getDate(idx++);
			obj.m_checkout =  rs.getDate(idx++);
			 
			return obj;
	}
}


