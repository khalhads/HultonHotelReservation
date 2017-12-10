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
 

public class Service extends BASESqlInterface {
	static Service instance = new Service();
	private int  m_id;
	private int  m_hotel_id;
	private String  m_type;
	private float  m_price;

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

	public String  getType () {
		return m_type;
	}

	public void  setType (String val) {
		m_type = val;
	}

	public float  getPrice () {
		return m_price;
	}

	public void  setPrice (float val) {
		m_price = val;
	}

	 
	 
	public static Service getInstance() {
		return instance;
	}
	 
 
	static public List<Service> fetchWithJoin(Connection conn, String joindAndWhereStr, String ... params) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT t.id, t.hotel_id, t.type, t.price FROM Service t ");
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
			ArrayList<Service> list = new ArrayList<Service>();
			while(rs.next()) {
				Service obj = instance.getNextRow(rs, 1);
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

 
	static public Service fetchById(Connection conn, String id) {
		String stmt =  "SELECT t.id, t.hotel_id, t.type, t.price FROM Service t  WHERE ID = ?";
		PreparedStatement cs = null;
		ResultSet rs = null;

		try {
			cs = conn.prepareStatement(stmt);
	        cs.setString(1, id);
            rs = cs.executeQuery();
			Service newobj = null;
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
		String stmt = "INSERT into Service(id, hotel_id, type, price ) VALUES (?,?,?,?)";
	    try {
	        cs = connection.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			int idx = 1;
			       	cs.setInt(idx++, m_id);
	    			       	cs.setInt(idx++, m_hotel_id);
	    			       	cs.setString(idx++, m_type);
	    			       	cs.setFloat(idx++, m_price);
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
				out.println("type = " + m_type);
				out.println("price = " + m_price);
	}
	 public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("id = " + m_id);
		sb.append(";hotel_id = " + m_hotel_id);
		sb.append(";type = " + m_type);
		sb.append(";price = " + m_price);
		return sb.toString();
    }

 
	public  Service getNextRow(ResultSet rs, int idx) throws SQLException {
			Service obj = new Service();
			 
			obj.m_id =  rs.getInt(idx++);
			obj.m_hotel_id =  rs.getInt(idx++);
			obj.m_type =  rs.getString(idx++);
			obj.m_price =  rs.getFloat(idx++);
			 
			return obj;
	}
}


