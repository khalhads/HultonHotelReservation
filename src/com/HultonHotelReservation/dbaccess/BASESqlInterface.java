package com.HultonHotelReservation.dbaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.commons.dbcp2.BasicDataSource;

public abstract class BASESqlInterface {
	protected static final Logger logger = LogManager.getLogger("BASESqlInterface");
	private static final int MAX_SAFE_STRING_SIZE = 4000;
	private static final String INPUT_FORMAT = "yyyy-mm-dd";
	private static final String NEW_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	private DateFormat dateformat = DateFormat.getInstance();
	static final int INSERT_FLAG = 0;
	static final int UPDATE_FLAG = 1;
	private int serial_id = 0;
	private static final BasicDataSource dataSource = new BasicDataSource();
	
	public static void setJDBCEnv(String dburl, String dbuser, String dbpwd, String driver){
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(dburl);
		dataSource.setUsername(dbuser);
		dataSource.setPassword(dbpwd);
	}

	public int  getSerialId() {
		return serial_id;
	}
	protected void  setSerialId(int id) {
		serial_id = id;
	}
	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	public static java.sql.Date getSqlDate(java.util.Date utilDate)  throws ParseException {
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		return sqlDate;
	}
	////// INTERFACE
 
	abstract protected  BASESqlInterface getNextRow(ResultSet rs, int idx) throws SQLException;
	
	public static int parseInt(String str) {
		if (str == null || "".equals(str))
			return 0;
		return Integer.parseInt(str.trim());
	}

	public static double parseDouble(String str) {
		if (str == null || "".equals(str))
			return 0.0;
		return Double.parseDouble(str.trim());
	}
	public static  float parseFloat(String str) {
		if (str == null || "".equals(str))
			return 0;
		return (float) Double.parseDouble(str.trim());
	}
	public static String parseString(String str)  {
		if (str == null)
			return "";
		return str.trim();
	}

	Timestamp parseTimestamp(String str) throws ParseException {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date parsedDate = dateFormat.parse(str);
	    return new java.sql.Timestamp(parsedDate.getTime());
	}
	public Date parseDate(String str)throws ParseException {
		if (str == null)
			return null;
		return dateformat.parse(str);
	}
 
	public static String update(Connection connection, String stmt, String... args) throws SQLException {

		PreparedStatement cs = null;
		try {
			cs = connection.prepareStatement(stmt);
			int idx = 1;
			for (String arg : args)
				cs.setString(idx++, arg);

			cs.execute();

			return null;
		} catch (Exception e) {
			return ("Failed to execute: [" + stmt + "], exception: " + e);
		} finally {
			if (cs != null)
				try {
					cs.close();
				} catch (SQLException logOrIgnore) {
				}
		}
	}

	static public List<String[]> fetchQueryResult(Connection conn, String selectQuery, Object... params) {

		PreparedStatement cs = null;
		ResultSet rs = null;

		try {

			cs = conn.prepareStatement(selectQuery);
			if (params.length > 0) {
				int idx = 1;

				for (Object param : params) {
					cs.setObject(idx++, param);
				}
			}

			rs = cs.executeQuery();
			LinkedList<String[]> list = new LinkedList<String[]>();
			while (rs.next()) {
				String[] values = new String[rs.getMetaData().getColumnCount()];
				for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
					values[i] = rs.getString(i + 1);
				}
				list.add(values);
			}

			return list;
		} catch (Exception e) {
			System.err.println("Failed to execute: [" + selectQuery + "], exception: " + e);
			return null;
		} finally {
			try {
				if (rs != null)
					try {
						rs.close();
					} catch (SQLException ignore) {
					}
				if (cs != null)
					if (cs != null) {
						cs.close();
						cs = null;
					}
			} catch (Exception e) {
			}
		}
	}

	public static java.sql.Date convert2Date(String datestr) {
		String newDateString;

		DateFormat formatter = new SimpleDateFormat(INPUT_FORMAT);
		java.util.Date d;
		try {
			d = formatter.parse(datestr);
		} catch (ParseException e) {
			logger.error("Failed to format date string: " + datestr, e);
			return null;
		}
		java.sql.Date sqlDate = new java.sql.Date(d.getTime());
		return sqlDate;
	}

	static public boolean delete(Connection conn, String selectQuery, String... params) {

		PreparedStatement cs = null;

		try {
			cs = conn.prepareStatement(selectQuery);
			if (params.length > 0) {
				int idx = 1;

				for (String param : params) {
					cs.setString(idx++, param);
				}
			}
			cs.executeUpdate();
			return true;
		} catch (Exception e) {
			System.err.println("Failed to execute: [" + selectQuery + "], exception: " + e);
			return false;
		} finally {
			try {

				if (cs != null)
					if (cs != null) {
						cs.close();
						cs = null;
					}
			} catch (Exception e) {
			}
		}
	}

	public static void closeJdbcResources(Connection conn, Statement cs, ResultSet res) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Throwable e) {
			}
		}
		if (cs != null) {
			try {
				cs.close();
			} catch (Throwable e) {
			}
		}
		if (res != null) {
			try {
				res.close();
			} catch (Throwable e) {
			}
		}
	}
	private static HashMap<String, BASESqlInterface> allTables = new HashMap<String, BASESqlInterface>() {{
			put("breakfast", new Breakfast());
	put("discount", new Discount());
	put("phonebook", new Phonebook());
	put("customer", new Customer());
	put("serviceoption", new Serviceoption());
	put("service", new Service());
	put("review", new Review());
	put("admin", new Admin());
	put("creditcard", new Creditcard());
	put("reservation", new Reservation());
	put("hoteladdress", new Hoteladdress());
	put("breakfastoption", new Breakfastoption());
	put("roomoption", new Roomoption());
	put("room", new Room());
	put("hotel", new Hotel());

	}};
	
	 
	protected void setClobString(Connection conn, PreparedStatement prepStmt, int idx, String s) throws SQLException {
  		if (s == null || s.length() < MAX_SAFE_STRING_SIZE) {
   		 	prepStmt.setString(idx, s);
   			return;
 		}
  		Clob clob = conn.createClob();
  		clob.setString(1, s);
  		prepStmt.setClob(idx, clob);
	}
}

