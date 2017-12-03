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
 

public class Review extends BASESqlInterface {
	static Review instance = new Review();
	private int  m_id;
	private int  m_customer_id;
	private int  m_review_type;
	private int  m_rating;
	private String  m_description;

	public int  getId () {
		return m_id;
	}

	public void  setId (int val) {
		m_id = val;
	}

	public int  getCustomerId () {
		return m_customer_id;
	}

	public void  setCustomerId (int val) {
		m_customer_id = val;
	}

	public int  getReviewType () {
		return m_review_type;
	}

	public void  setReviewType (int val) {
		m_review_type = val;
	}

	public int  getRating () {
		return m_rating;
	}

	public void  setRating (int val) {
		m_rating = val;
	}

	public String  getDescription () {
		return m_description;
	}

	public void  setDescription (String val) {
		m_description = val;
	}

	 
	 
	public static Review getInstance() {
		return instance;
	}
	 
	 
 

	public boolean deleteById(Connection conn) {
		
		String stmt = "DELETE FROM Review WHERE ID = ?";
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
	
	static public List<Review> fetchWithJoin(Connection conn, String joindAndWhereStr, String ... params) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT t.id, t.customer_id, t.review_type, t.rating, t.description FROM Review t ");
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
			ArrayList<Review> list = new ArrayList<Review>();
			while(rs.next()) {
				Review obj = instance.getNextRow(rs, 1);
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

 
	static public Review fetchById(Connection conn, String id) {
		String stmt =  "SELECT t.id, t.customer_id, t.review_type, t.rating, t.description FROM Review t  WHERE ID = ?";
		PreparedStatement cs = null;
		ResultSet rs = null;

		try {
			cs = conn.prepareStatement(stmt);
	        cs.setString(1, id);
            rs = cs.executeQuery();
			Review newobj = null;
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
		String stmt = "INSERT into Review(id, customer_id, review_type, rating, description ) VALUES (?,?,?,?,?)";
	    try {
	        cs = connection.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			int idx = 1;
			       	cs.setInt(idx++, m_id);
	    			       	cs.setInt(idx++, m_customer_id);
	    			       	cs.setInt(idx++, m_review_type);
	    			       	cs.setInt(idx++, m_rating);
	    			       	cs.setString(idx++, m_description);
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
				out.println("customer_id = " + m_customer_id);
				out.println("review_type = " + m_review_type);
				out.println("rating = " + m_rating);
				out.println("description = " + m_description);
	}
	 public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("id = " + m_id);
		sb.append(";customer_id = " + m_customer_id);
		sb.append(";review_type = " + m_review_type);
		sb.append(";rating = " + m_rating);
		sb.append(";description = " + m_description);
		return sb.toString();
    }

 
	public  Review getNextRow(ResultSet rs, int idx) throws SQLException {
			Review obj = new Review();
			 
			obj.m_id =  rs.getInt(idx++);
			obj.m_customer_id =  rs.getInt(idx++);
			obj.m_review_type =  rs.getInt(idx++);
			obj.m_rating =  rs.getInt(idx++);
			obj.m_description =  rs.getString(idx++);
			 
			return obj;
	}
}


