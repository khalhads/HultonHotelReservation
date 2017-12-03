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
 

public class Serviceoption extends BASESqlInterface {
	static Serviceoption instance = new Serviceoption();
	private int  m_service_id;
	private int  m_roomoption_id;

	public int  getServiceId () {
		return m_service_id;
	}

	public void  setServiceId (int val) {
		m_service_id = val;
	}

	public int  getRoomoptionId () {
		return m_roomoption_id;
	}

	public void  setRoomoptionId (int val) {
		m_roomoption_id = val;
	}

	 
	 
	public static Serviceoption getInstance() {
		return instance;
	}
	 
	 
	
	static public List<Serviceoption> fetchWithJoin(Connection conn, String joindAndWhereStr, String ... params) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT t.service_id, t.roomoption_id FROM ServiceOption t ");
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
			ArrayList<Serviceoption> list = new ArrayList<Serviceoption>();
			while(rs.next()) {
				Serviceoption obj = instance.getNextRow(rs, 1);
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

 
	static public Serviceoption fetchById(Connection conn, String id) {
		String stmt =  "SELECT t.service_id, t.roomoption_id FROM ServiceOption t  WHERE ID = ?";
		PreparedStatement cs = null;
		ResultSet rs = null;

		try {
			cs = conn.prepareStatement(stmt);
	        cs.setString(1, id);
            rs = cs.executeQuery();
			Serviceoption newobj = null;
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
		String stmt = "INSERT into ServiceOption(service_id, roomoption_id ) VALUES (?,?)";
	    try {
	        cs = connection.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			int idx = 1;
			       	cs.setInt(idx++, m_service_id);
	    			       	cs.setInt(idx++, m_roomoption_id);
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
				out.println("service_id = " + m_service_id);
				out.println("roomoption_id = " + m_roomoption_id);
	}
	 public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("service_id = " + m_service_id);
		sb.append(";roomoption_id = " + m_roomoption_id);
		return sb.toString();
    }

 
	public  Serviceoption getNextRow(ResultSet rs, int idx) throws SQLException {
			Serviceoption obj = new Serviceoption();
			 
			obj.m_service_id =  rs.getInt(idx++);
			obj.m_roomoption_id =  rs.getInt(idx++);
			 
			return obj;
	}
}


