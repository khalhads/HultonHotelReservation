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
 

public class Discount extends BASESqlInterface {
	static Discount instance = new Discount();
	private int  m_id;
	private int  m_room_id;
	private Date  m_fromdate;
	private Date  m_todate;
	private float  m_discountpct;
	private int  m_taken;

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

	public Date  getFromdate () {
		return m_fromdate;
	}

	public void  setFromdate (Date val) {
		m_fromdate = val;
	}

	public Date  getTodate () {
		return m_todate;
	}

	public void  setTodate (Date val) {
		m_todate = val;
	}

	public float  getDiscountpct () {
		return m_discountpct;
	}

	public void  setDiscountpct (float val) {
		m_discountpct = val;
	}

	public int  getTaken () {
		return m_taken;
	}

	public void  setTaken (int val) {
		m_taken = val;
	}

	 
	 
	public static Discount getInstance() {
		return instance;
	}
	 
 
	static public List<Discount> fetchWithJoin(Connection conn, String joindAndWhereStr, String ... params) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT t.id, t.room_id, t.fromdate, t.todate, t.discountpct, t.taken FROM Discount t ");
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
			ArrayList<Discount> list = new ArrayList<Discount>();
			while(rs.next()) {
				Discount obj = instance.getNextRow(rs, 1);
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

 
	static public Discount fetchById(Connection conn, String id) {
		String stmt =  "SELECT t.id, t.room_id, t.fromdate, t.todate, t.discountpct, t.taken FROM Discount t  WHERE ID = ?";
		PreparedStatement cs = null;
		ResultSet rs = null;

		try {
			cs = conn.prepareStatement(stmt);
	        cs.setString(1, id);
            rs = cs.executeQuery();
			Discount newobj = null;
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
		String stmt = "INSERT into Discount(id, room_id, fromdate, todate, discountpct, taken ) VALUES (?,?,?,?,?,?)";
	    try {
	        cs = connection.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			int idx = 1;
			       	cs.setInt(idx++, m_id);
	    			       	cs.setInt(idx++, m_room_id);
	    			       	cs.setDate(idx++, (java.sql.Date) m_fromdate);
	    			       	cs.setDate(idx++, (java.sql.Date) m_todate);
	    			       	cs.setFloat(idx++, m_discountpct);
	    			       	cs.setInt(idx++, m_taken);
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
				out.println("fromdate = " + m_fromdate);
				out.println("todate = " + m_todate);
				out.println("discountpct = " + m_discountpct);
				out.println("taken = " + m_taken);
	}
	 public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("id = " + m_id);
		sb.append(";room_id = " + m_room_id);
		sb.append(";fromdate = " + m_fromdate);
		sb.append(";todate = " + m_todate);
		sb.append(";discountpct = " + m_discountpct);
		sb.append(";taken = " + m_taken);
		return sb.toString();
    }

 
	public  Discount getNextRow(ResultSet rs, int idx) throws SQLException {
			Discount obj = new Discount();
			 
			obj.m_id =  rs.getInt(idx++);
			obj.m_room_id =  rs.getInt(idx++);
			obj.m_fromdate =  rs.getDate(idx++);
			obj.m_todate =  rs.getDate(idx++);
			obj.m_discountpct =  rs.getFloat(idx++);
			obj.m_taken =  rs.getInt(idx++);
			 
			return obj;
	}
}


