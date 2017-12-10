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
 

public class Admin extends BASESqlInterface {
	static Admin instance = new Admin();
	private int  m_id;
	private String  m_first_name;
	private String  m_last_name;
	private String  m_login_id;
	private String  m_password;

	public int  getId () {
		return m_id;
	}

	public void  setId (int val) {
		m_id = val;
	}

	public String  getFirstName () {
		return m_first_name;
	}

	public void  setFirstName (String val) {
		m_first_name = val;
	}

	public String  getLastName () {
		return m_last_name;
	}

	public void  setLastName (String val) {
		m_last_name = val;
	}

	public String  getLoginId () {
		return m_login_id;
	}

	public void  setLoginId (String val) {
		m_login_id = val;
	}

	public String  getPassword () {
		return m_password;
	}

	public void  setPassword (String val) {
		m_password = val;
	}

	 
	 
	public static Admin getInstance() {
		return instance;
	}
	 
 
	static public List<Admin> fetchWithJoin(Connection conn, String joindAndWhereStr, String ... params) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT t.id, t.first_name, t.last_name, t.login_id, t.password FROM Admin t ");
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
			ArrayList<Admin> list = new ArrayList<Admin>();
			while(rs.next()) {
				Admin obj = instance.getNextRow(rs, 1);
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

 
	static public Admin fetchById(Connection conn, String id) {
		String stmt =  "SELECT t.id, t.first_name, t.last_name, t.login_id, t.password FROM Admin t  WHERE ID = ?";
		PreparedStatement cs = null;
		ResultSet rs = null;

		try {
			cs = conn.prepareStatement(stmt);
	        cs.setString(1, id);
            rs = cs.executeQuery();
			Admin newobj = null;
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
		String stmt = "INSERT into Admin(id, first_name, last_name, login_id, password ) VALUES (?,?,?,?,?)";
	    try {
	        cs = connection.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			int idx = 1;
			       	cs.setInt(idx++, m_id);
	    			       	cs.setString(idx++, m_first_name);
	    			       	cs.setString(idx++, m_last_name);
	    			       	cs.setString(idx++, m_login_id);
	    			       	cs.setString(idx++, m_password);
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
				out.println("first_name = " + m_first_name);
				out.println("last_name = " + m_last_name);
				out.println("login_id = " + m_login_id);
				out.println("password = " + m_password);
	}
	 public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("id = " + m_id);
		sb.append(";first_name = " + m_first_name);
		sb.append(";last_name = " + m_last_name);
		sb.append(";login_id = " + m_login_id);
		sb.append(";password = " + m_password);
		return sb.toString();
    }

 
	public  Admin getNextRow(ResultSet rs, int idx) throws SQLException {
			Admin obj = new Admin();
			 
			obj.m_id =  rs.getInt(idx++);
			obj.m_first_name =  rs.getString(idx++);
			obj.m_last_name =  rs.getString(idx++);
			obj.m_login_id =  rs.getString(idx++);
			obj.m_password =  rs.getString(idx++);
			 
			return obj;
	}
}


