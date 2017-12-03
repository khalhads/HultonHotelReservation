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
 

public class Room extends BASESqlInterface {
	static Room instance = new Room();
	private int  m_id;
	private int  m_hotel_id;
	private int  m_room_no;
	private String  m_room_type;
	private float  m_price;
	private String  m_description;
	private int  m_floor_no;
	private int  m_max_people;

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

	public int  getRoomNo () {
		return m_room_no;
	}

	public void  setRoomNo (int val) {
		m_room_no = val;
	}

	public String  getRoomType () {
		return m_room_type;
	}

	public void  setRoomType (String val) {
		m_room_type = val;
	}

	public float  getPrice () {
		return m_price;
	}

	public void  setPrice (float val) {
		m_price = val;
	}

	public String  getDescription () {
		return m_description;
	}

	public void  setDescription (String val) {
		m_description = val;
	}

	public int  getFloorNo () {
		return m_floor_no;
	}

	public void  setFloorNo (int val) {
		m_floor_no = val;
	}

	public int  getMaxPeople () {
		return m_max_people;
	}

	public void  setMaxPeople (int val) {
		m_max_people = val;
	}

	 
	 
	public static Room getInstance() {
		return instance;
	}
	 
	 
 

	public boolean deleteById(Connection conn) {
		
		String stmt = "DELETE FROM Room WHERE ID = ?";
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
	
	static public List<Room> fetchWithJoin(Connection conn, String joindAndWhereStr, String ... params) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT t.id, t.hotel_id, t.room_no, t.room_type, t.price, t.description, t.floor_no, t.max_people FROM Room t ");
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
			ArrayList<Room> list = new ArrayList<Room>();
			while(rs.next()) {
				Room obj = instance.getNextRow(rs, 1);
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

 
	static public Room fetchById(Connection conn, String id) {
		String stmt =  "SELECT t.id, t.hotel_id, t.room_no, t.room_type, t.price, t.description, t.floor_no, t.max_people FROM Room t  WHERE ID = ?";
		PreparedStatement cs = null;
		ResultSet rs = null;

		try {
			cs = conn.prepareStatement(stmt);
	        cs.setString(1, id);
            rs = cs.executeQuery();
			Room newobj = null;
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
		String stmt = "INSERT into Room(id, hotel_id, room_no, room_type, price, description, floor_no, max_people ) VALUES (?,?,?,?,?,?,?,?)";
	    try {
	        cs = connection.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			int idx = 1;
			       	cs.setInt(idx++, m_id);
	    			       	cs.setInt(idx++, m_hotel_id);
	    			       	cs.setInt(idx++, m_room_no);
	    			       	cs.setString(idx++, m_room_type);
	    			       	cs.setFloat(idx++, m_price);
	    			       	cs.setString(idx++, m_description);
	    			       	cs.setInt(idx++, m_floor_no);
	    			       	cs.setInt(idx++, m_max_people);
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
				out.println("room_no = " + m_room_no);
				out.println("room_type = " + m_room_type);
				out.println("price = " + m_price);
				out.println("description = " + m_description);
				out.println("floor_no = " + m_floor_no);
				out.println("max_people = " + m_max_people);
	}
	 public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("id = " + m_id);
		sb.append(";hotel_id = " + m_hotel_id);
		sb.append(";room_no = " + m_room_no);
		sb.append(";room_type = " + m_room_type);
		sb.append(";price = " + m_price);
		sb.append(";description = " + m_description);
		sb.append(";floor_no = " + m_floor_no);
		sb.append(";max_people = " + m_max_people);
		return sb.toString();
    }

 
	public  Room getNextRow(ResultSet rs, int idx) throws SQLException {
			Room obj = new Room();
			 
			obj.m_id =  rs.getInt(idx++);
			obj.m_hotel_id =  rs.getInt(idx++);
			obj.m_room_no =  rs.getInt(idx++);
			obj.m_room_type =  rs.getString(idx++);
			obj.m_price =  rs.getFloat(idx++);
			obj.m_description =  rs.getString(idx++);
			obj.m_floor_no =  rs.getInt(idx++);
			obj.m_max_people =  rs.getInt(idx++);
			 
			return obj;
	}
}


