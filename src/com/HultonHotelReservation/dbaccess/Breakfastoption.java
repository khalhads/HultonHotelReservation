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
 

public class Breakfastoption extends BASESqlInterface {
	static Breakfastoption instance = new Breakfastoption();
	private int  m_breakfast_id;
	private int  m_roomoption_id;
	private int  m_order_count;

	public int  getBreakfastId () {
		return m_breakfast_id;
	}

	public void  setBreakfastId (int val) {
		m_breakfast_id = val;
	}

	public int  getRoomoptionId () {
		return m_roomoption_id;
	}

	public void  setRoomoptionId (int val) {
		m_roomoption_id = val;
	}

	public int  getOrderCount () {
		return m_order_count;
	}

	public void  setOrderCount (int val) {
		m_order_count = val;
	}

	 
	 
	public static Breakfastoption getInstance() {
		return instance;
	}
	 
 
	static public List<Breakfastoption> fetchWithJoin(Connection conn, String joindAndWhereStr, String ... params) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT t.breakfast_id, t.roomoption_id, t.order_count FROM BreakfastOption t ");
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
			ArrayList<Breakfastoption> list = new ArrayList<Breakfastoption>();
			while(rs.next()) {
				Breakfastoption obj = instance.getNextRow(rs, 1);
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

 
	static public Breakfastoption fetchById(Connection conn, String id) {
		String stmt =  "SELECT t.breakfast_id, t.roomoption_id, t.order_count FROM BreakfastOption t  WHERE ID = ?";
		PreparedStatement cs = null;
		ResultSet rs = null;

		try {
			cs = conn.prepareStatement(stmt);
	        cs.setString(1, id);
            rs = cs.executeQuery();
			Breakfastoption newobj = null;
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
		String stmt = "INSERT into BreakfastOption(breakfast_id, roomoption_id, order_count ) VALUES (?,?,?)";
	    try {
	        cs = connection.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			int idx = 1;
			       	cs.setInt(idx++, m_breakfast_id);
	    			       	cs.setInt(idx++, m_roomoption_id);
	    			       	cs.setInt(idx++, m_order_count);
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
				out.println("breakfast_id = " + m_breakfast_id);
				out.println("roomoption_id = " + m_roomoption_id);
				out.println("order_count = " + m_order_count);
	}
	 public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("breakfast_id = " + m_breakfast_id);
		sb.append(";roomoption_id = " + m_roomoption_id);
		sb.append(";order_count = " + m_order_count);
		return sb.toString();
    }

 
	public  Breakfastoption getNextRow(ResultSet rs, int idx) throws SQLException {
			Breakfastoption obj = new Breakfastoption();
			 
			obj.m_breakfast_id =  rs.getInt(idx++);
			obj.m_roomoption_id =  rs.getInt(idx++);
			obj.m_order_count =  rs.getInt(idx++);
			 
			return obj;
	}
}


