package com.HultonHotelReservation;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.dbcp2.BasicDataSource;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.HultonHotelReservation.dbaccess.BASESqlInterface;
import com.HultonHotelReservation.dbaccess.Breakfast;
import com.HultonHotelReservation.dbaccess.Breakfastoption;
import com.HultonHotelReservation.dbaccess.Creditcard;
import com.HultonHotelReservation.dbaccess.Customer;
import com.HultonHotelReservation.dbaccess.Hotel;
import com.HultonHotelReservation.dbaccess.Reservation;
import com.HultonHotelReservation.dbaccess.Review;
import com.HultonHotelReservation.dbaccess.Room;
import com.HultonHotelReservation.dbaccess.Roomoption;
import com.HultonHotelReservation.dbaccess.Service;
import com.HultonHotelReservation.dbaccess.Serviceoption;

public class DBAccess {
	private static final long serialVersionUID = 1L;
	public static BasicDataSource dataSource;

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			BasicDataSource ds = new BasicDataSource();
			ds.setUrl("jdbc:mysql://cs336-hoteldbms.cwop6c6w5v0u.us-east-2.rds.amazonaws.com/HultonHotelReservation");
			ds.setUsername("HotelDBMS");
			ds.setPassword("password");

			ds.setMinIdle(5);
			ds.setMaxIdle(10);
			ds.setMaxOpenPreparedStatements(100);

			dataSource = ds;
		} catch (ClassNotFoundException e) {
			System.err.println("Failed to create connection pool: exception " + e);
		}
	}

	///////////////////// Main processing function
	///////////////////// /////////////////////////////////////

	public static BasicDataSource getDataSource() {
		return dataSource;
	}

	////////////////////// /////////////////////////////////////////////

	public static boolean checkIfCustomerLoggedIn(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		UserAuth auth = (UserAuth) session.getAttribute("auth");

		if (auth == null) {
			// request.getContextPath() +
			request.getRequestDispatcher("signup.jsp").forward(request, response);
			return false;
		}
		return true;
	}

	private static JSONObject s_hotel_db = null;
	private static HashMap<String, JSONObject> byHotel = new HashMap<String, JSONObject>();
	private static HashMap<String, JSONObject> byRoom = new HashMap<String, JSONObject>();
	private static HashMap<String, JSONObject> byBreakfast = new HashMap<String, JSONObject>();
	private static HashMap<String, JSONObject> byService = new HashMap<String, JSONObject>();

	private static JSONObject fetchHotelDb() {
		Connection connection = null;
		JSONObject jsondb = new JSONObject();
		JSONObject countries = new JSONObject();
		jsondb.put("countries", countries);

		try {
			connection = dataSource.getConnection();

			populateRooms(connection, jsondb, countries);
			populateBreakfasts(connection, jsondb, countries);
			populateServices(connection, jsondb, countries);
			jsondb.put("m_status_code", 1);
			jsondb.put("m_status_msg", "");

		} catch (Exception e) {
			System.err.println("Failed to fetch Hotel db: " + e);
			jsondb.put("m_status_code", -1);
			jsondb.put("m_status_msg", "Failed to fetch Hotel db: " + e);
			return jsondb;
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		System.out.println("Return Hotel db: " + jsondb.toJSONString());
		return jsondb;
	}

	public static JSONObject populateReservations(HttpSession session, Connection connection, JSONObject jsonrequest) {
		JSONArray hotels = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
		String m_customer_id = (String) jsonrequest.get("m_customer_id");
		
		jsonResponse.put("m_status_code", 1);
		jsonResponse.put("m_status_msg", "");
		
		jsonResponse.put("hotels", hotels);
		String ReservationQuery = "select rm.hotel_id, rm.id, ro.id, ro.discount_id, ro.reservation_id, ro.checkIn, ro.checkOut, rs.id "
				+ "FROM   Room rm, RoomOption ro, Reservation rs "
				+ "where rm.id = ro.room_id AND ro.reservation_id = rs.id AND rs.customer_id = ? "
				+ "order by rs.id";
		 
		if(m_customer_id == null) {
			UserAuth userAuth = (UserAuth) session.getAttribute("auth");
			if (userAuth == null)
				return jsonResponse;
			m_customer_id = "" + userAuth.getUserId();
		}
		
		List<String[]> rows = Hotel.fetchQueryResult(connection, ReservationQuery, m_customer_id);

		if (rows == null || rows.size() == 0)
			return jsonResponse;

		JSONObject nextReservation = null;
		JSONArray rooms = null;
		String reservation_id = "";
		for (int i = 0; i < rows.size(); i++) {
			String[] fields = rows.get(i);
			if (!reservation_id.equals(fields[7])) {
				reservation_id = fields[7];
				nextReservation = new JSONObject(byHotel.get(fields[0]));
				hotels.add(nextReservation);
				rooms = new JSONArray();
				nextReservation.put("rooms", rooms);
				nextReservation.put("reservation_id", reservation_id);

			}
			JSONObject room = new JSONObject(byRoom.get(fields[1]));
			room.put("checkin", fields[5]);
			room.put("checkout", fields[6]);
			rooms.add(room);
			JSONArray breakfasts = new JSONArray();
			JSONArray services = new JSONArray();
			room.put("breakfasts", breakfasts);
			room.put("services", services);

			List<Breakfastoption> blist = Breakfastoption.fetchWithJoin(connection,
					", RoomOption ro where t.roomoption_id = ro.id AND ro.room_id = ?", fields[1]);
			List<Serviceoption> slist = Serviceoption.fetchWithJoin(connection,
					", RoomOption ro where t.roomoption_id = ro.id AND ro.room_id = ?", fields[1]);

			for (Breakfastoption bo : blist) {
				breakfasts.add(new JSONObject(byBreakfast.get("" + bo.getBreakfastId())));
			}
			for (Serviceoption so : slist) {
				services.add(new JSONObject(byService.get("" + so.getServiceId())));
			}

		}

		return jsonResponse;
	}

	private static void populateRooms(Connection connection, JSONObject jsondb, JSONObject countries) {
		String hotelDbQuery = "select h.hotel_id, h.street, h.city, h.state, h.country, "
				+ "r.room_no, r.room_type, r.price, r.description, r.floor_no, r.max_people, r.id "
				+ " FROM HotelAddress h , Room r where r.hotel_id = h.hotel_id" + "  order by h.country, h.state";

		List<String[]> rows = Hotel.fetchQueryResult(connection, hotelDbQuery);

		if (rows == null || rows.size() == 0)
			return;
		processRooms(rows, jsondb, countries);
	}

	private static void processRooms(List<String[]> rows, JSONObject jsondb, JSONObject countries) {
		String countryName = "";
		String stateName = "";
		JSONObject states = new JSONObject();
		String hotelId = "";
		JSONObject hotels = new JSONObject();
		JSONObject rooms = new JSONObject();

		for (int i = 0; i < rows.size(); i++) {
			String[] fields = rows.get(i);
			if (countryName.equals(fields[4])) {
				if (stateName.equals(fields[3])) {
					if (hotelId.equals(fields[0])) {
						addRoom(rooms, fields);
					} else {
						hotelId = fields[0];

						addHotel(hotels, rooms, fields);
					}
				} else {
					stateName = fields[3];
					hotelId = fields[0];
					hotels = new JSONObject();
					rooms = new JSONObject();
					addState(states, hotels, rooms, fields);
				}
			} else {
				countryName = fields[4];
				stateName = fields[3];
				hotelId = fields[0];
				states = new JSONObject();
				hotels = new JSONObject();
				rooms = new JSONObject();
				addCountry(countries, states, hotels, rooms, fields);
			}
		}
	}

	private static JSONObject getHotel(JSONObject countries, String hotel_id) {
		JSONObject hotel = null;

		Set<Entry<String, Object>> countriesSet = countries.entrySet();
		for (Entry<String, Object> centry : countriesSet) {
			JSONObject country = (JSONObject) centry.getValue();
			JSONObject states = (JSONObject) country.get("states");
			Set<Entry<String, Object>> statesSet = states.entrySet();

			for (Entry<String, Object> sentry : statesSet) {
				JSONObject state = (JSONObject) sentry.getValue();
				JSONObject hotels = (JSONObject) state.get("hotels");
				if (hotels.containsKey(hotel_id))
					return (JSONObject) hotels.get(hotel_id);
			}
		}
		return hotel;
	}

	private static void addBreakfast(JSONObject breakfasts, String[] fields) {
		JSONObject breakfast = new JSONObject();
		breakfasts.put(fields[0], breakfast);
		breakfast.put("id", fields[0]);
		breakfast.put("description", fields[2]);
		breakfast.put("price", fields[3]);
		breakfast.put("type", fields[4]);

		byBreakfast.put(fields[0], breakfast);
	}

	private static void populateBreakfasts(Connection connection, JSONObject jsondb, JSONObject countries) {
		String breakfastDbQuery = "select  b.id,  b.hotel_id, b.description, b.price, b.type FROM Breakfast b order by b.hotel_id";

		List<String[]> rows = Hotel.fetchQueryResult(connection, breakfastDbQuery);

		if (rows == null || rows.size() == 0)
			return;
		processBreakfasts(rows, jsondb, countries);
	}

	private static void processBreakfasts(List<String[]> rows, JSONObject jsondb, JSONObject countries) {
		JSONObject hotel = null;
		JSONObject breakfasts = null;
		String hotel_id = "";
		for (int i = 0; i < rows.size(); i++) {
			String[] fields = rows.get(i);
			if (hotel_id.equals(fields[1])) {
				addBreakfast(breakfasts, fields);
			} else {
				hotel = getHotel(countries, fields[1]);
				if (hotel != null) {
					hotel_id = fields[1];
					breakfasts = new JSONObject();
					hotel.put("breakfasts", breakfasts);
					addBreakfast(breakfasts, fields);
				}
			}

		}
	}

	private static void addService(JSONObject services, String[] fields) {
		JSONObject service = new JSONObject();
		services.put(fields[0], service);
		service.put("id", fields[0]);
		service.put("hotel_id", fields[1]);
		service.put("type", fields[2]);
		service.put("price", fields[3]);

		byService.put(fields[0], service);
	}

	private static void populateServices(Connection connection, JSONObject jsondb, JSONObject countries) {
		String serviceDbQuery = "select  s.id, s.hotel_id,  s.type, s.price FROM Service s order by s.hotel_id";

		List<String[]> rows = Hotel.fetchQueryResult(connection, serviceDbQuery);

		if (rows == null || rows.size() == 0)
			return;
		populateServices(rows, jsondb, countries);
	}

	private static void populateServices(List<String[]> rows, JSONObject jsondb, JSONObject countries) {
		JSONObject hotel = null;
		JSONObject services = null;
		String hotel_id = "";
		for (int i = 0; i < rows.size(); i++) {
			String[] fields = rows.get(i);
			if (hotel_id.equals(fields[1])) {
				addService(services, fields);
			} else {
				hotel = getHotel(countries, fields[1]);
				if (hotel != null) {
					hotel_id = fields[1];
					services = new JSONObject();
					hotel.put("services", services);
					addService(services, fields);
				}
			}

		}
	}

	private static void addCountry(JSONObject countries, JSONObject states, JSONObject hotels, JSONObject rooms,
			String[] fields) {
		JSONObject country = new JSONObject();
		countries.put(fields[4], country);

		country.put("id", fields[4]);
		country.put("states", states);

		addState(states, hotels, rooms, fields);
	}

	private static void addState(JSONObject states, JSONObject hotels, JSONObject rooms, String[] fields) {
		JSONObject state = new JSONObject();
		states.put(fields[3], state);

		state.put("id", fields[3]);

		state.put("hotels", hotels);

		state.put("country", fields[4]);
		addHotel(hotels, rooms, fields);
	}

	private static void addHotel(JSONObject hotels, JSONObject rooms, String[] fields) {
		JSONObject hotel = new JSONObject();

		hotels.put(fields[0], hotel);

		hotel.put("id", fields[0]);
		hotel.put("rooms", rooms);
		hotel.put("street", fields[1]);
		hotel.put("city", fields[2]);
		hotel.put("state", fields[3]);
		hotel.put("country", fields[4]);

		JSONObject newHotel = new JSONObject(hotel);
		byHotel.put(fields[0], newHotel);

		addRoom(rooms, fields);

	}

	private static java.sql.Date strToSqlDate(String datestr) throws ParseException {
		java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(datestr);
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		return sqlDate;
	}

	private static void addRoom(JSONObject rooms, String[] fields) {
		JSONObject room = new JSONObject();

		rooms.put(fields[11], room);
		room.put("id", fields[11]);
		room.put("room_no", fields[5]);
		room.put("type", fields[6]);
		room.put("price", fields[7]);
		room.put("description", fields[8]);
		room.put("floor_no", fields[9]);
		room.put("max_people", fields[10]);

		byRoom.put(fields[11], room);
	}

	public static JSONObject getHotelDb() {
		if (s_hotel_db == null) {
			s_hotel_db = fetchHotelDb();

		}
		return s_hotel_db;
	}

	 
	public static JSONObject reserveRequest(HttpSession session, Connection connection, String request_type,
			JSONObject jsonrequest) throws ParseException, SQLException {
		System.out.println("Request: " + jsonrequest.toJSONString());
		JSONArray jrooms = (JSONArray) jsonrequest.get("rooms");
		UserAuth userAuth = (UserAuth) session.getAttribute("auth");
		int reservationId = -1;
		try {
			connection.setAutoCommit(false); // transaction block start

			JSONObject jcard = (JSONObject) jsonrequest.get("cardinfo");
			Creditcard creditCard = new Creditcard();
			creditCard.setPersonName((String) jcard.get("card_owner"));
			creditCard.setCardNumber((String) jcard.get("card_no"));
			creditCard.setCardType((String) jcard.get("card_type"));
			creditCard.setSecurityCode((String) jcard.get("security_code"));
			creditCard.setExpirationDate((String) jcard.get("expiration_date"));
			creditCard.setBillingAddress((String) jcard.get("billing_address"));
			int cardRecordId = creditCard.insertRecord(connection);

			Reservation reservation = new Reservation();
			reservation.setCustomerId(userAuth.getUserId());
			reservation.setCreatedate(new java.sql.Date(new java.util.Date().getTime()));

			reservation.setCreditcardId(cardRecordId);
			reservationId = reservation.insertRecord(connection);
			double totalprice = 0.0;

			List<Roomoption> rooms = new ArrayList<Roomoption>();
			for (int i = 0; i < jrooms.size(); i++) {
				Roomoption roomoption = new Roomoption();
				String str;
				JSONObject jroom = (JSONObject) jrooms.get(i);
				str = (String) jroom.get("id");
				roomoption.setRoomId(Integer.parseInt(str));

				JSONObject obj = byRoom.get(str);
				str = (String) obj.get("price");
				totalprice += BASESqlInterface.parseFloat(str);

				str = (String) jroom.get("checkin");

				roomoption.setCheckin(strToSqlDate(str));

				str = (String) jroom.get("checkout");

				roomoption.setCheckout(strToSqlDate(str));
				roomoption.setReservationId(reservationId);
				System.out.println("Request: " + jsonrequest.toJSONString());
				int roomRecordId = roomoption.insertRecord(connection);

				JSONArray jbreakfasts = (JSONArray) jroom.get("roomBreakfasts");
				List<Breakfastoption> breakfasts = new ArrayList<Breakfastoption>();
				for (int b = 0; b < jbreakfasts.size(); b++) {
					Breakfastoption breakfastoption = new Breakfastoption();
					JSONObject jbreakfast = (JSONObject) jbreakfasts.get(b);
					str = (String) jbreakfast.get("id");
					breakfastoption.setBreakfastId(Integer.parseInt(str));

					obj = byBreakfast.get(str);
					str = (String) obj.get("price");
					totalprice += BASESqlInterface.parseFloat(str);

					str = (String) jbreakfast.get("count");
					breakfastoption.setOrderCount(Integer.parseInt(str));
					breakfastoption.setRoomoptionId(roomRecordId);
					breakfastoption.insertRecord(connection);
				}
				JSONArray jservices = (JSONArray) jroom.get("roomServices");
				List<Serviceoption> services = new ArrayList<Serviceoption>();
				for (int s = 0; s < jservices.size(); s++) {
					Serviceoption servicetoption = new Serviceoption();
					JSONObject jservice = (JSONObject) jservices.get(s);
					str = (String) jservice.get("id");
					servicetoption.setServiceId(Integer.parseInt(str));

					obj = byService.get(str);
					str = (String) obj.get("price");
					totalprice += BASESqlInterface.parseFloat(str);

					servicetoption.setRoomoptionId(roomRecordId);
					servicetoption.insertRecord(connection);
				}
			}
			reservation.update(connection, "update Reservation set total_cost = ? where id = ?", "" + totalprice,
					"" + reservationId);
			System.out.println("Reservation Done");
			connection.commit();
		} finally {
			connection.setAutoCommit(true);
		}
		JSONObject jsonResponse = new JSONObject();
		jsonResponse = new JSONObject();

		jsonResponse.put("m_status_code", 1);
		jsonResponse.put("m_status_msg", "");
		jsonResponse.put("m_invoice_id", "" + reservationId);
		return jsonResponse;
	}

	public static JSONObject saveReview(HttpSession session, Connection connection, String request_type,
			JSONObject jsonrequest) {
		JSONObject jsonResponse = new JSONObject();
		try {

			String rating = (String) jsonrequest.get("m_rating");
			String description = (String) jsonrequest.get("m_description");
			String review_type = (String) jsonrequest.get("m_review_type");
			String item_id = (String) jsonrequest.get("m_item_id");
			
			Review review = new Review();

			review.setItemId(Integer.parseInt(item_id));
			review.setDescription(description);
			review.setRating(Integer.parseInt(rating));
			review.setReviewType(review_type);
			review.insertRecord(connection);

			jsonResponse.put("m_status_code", 1);
			jsonResponse.put("m_status_msg", "");
		} catch (Exception e) {
			jsonResponse.put("m_status_code", -1);
			jsonResponse.put("m_status_msg", "Failed to add a Review: " + e);
		}
		return jsonResponse;
	}

	public static JSONObject checkIfRoomReserved(HttpSession session, Connection connection, String request_type,
			JSONObject jsonrequest) {
		JSONObject jsonResponse = new JSONObject();
		try {
			String checkin = (String) jsonrequest.get("checkin");
			String checkout = (String) jsonrequest.get("checkout");
			String room_id = (String) jsonrequest.get("room_id");
			Review review = new Review();
			List<Roomoption> list = Roomoption.fetchWithJoin(connection, " where t.room_id = ? AND (" +
																				"(t.checkIn <= STR_TO_DATE(?, '%Y-%m-%d') AND t.checkIn >= STR_TO_DATE(?, '%Y-%m-%d')) OR " +
																				"(t.checkIn <= STR_TO_DATE(?, '%Y-%m-%d') AND t.checkIn >= STR_TO_DATE(?, '%Y-%m-%d'))" +
																				")", room_id, checkin, checkin,  checkout, checkout);

			if(list != null && list.size() > 0) {
				jsonResponse.put("m_status_code", -1);
				jsonResponse.put("m_status_msg", "This room is reserved from: " + list.get(0).getCheckin() + " to: " + list.get(0).getCheckout());
			} else {
				jsonResponse.put("m_status_code", 1);
				jsonResponse.put("m_status_msg", "");
			}
			
		} catch (Exception e) {
			jsonResponse.put("m_status_code", -1);
			jsonResponse.put("m_status_msg", "Failed to add a Review: " + e);
		}
		return jsonResponse;
	}

	public static JSONObject searchForCustomer(HttpSession session, Connection connection, String request_type,
			JSONObject jsonrequest) {
		JSONObject jsonResponse = new JSONObject();
		try {
			String name = (String) jsonrequest.get("name");
			name = "%" + name + "%";
			List<Customer> customers = Customer.fetchWithJoin(connection, " where CONCAT(t.first_name, ' ', t.last_name) like ?", name);

			if(customers != null && customers.size() == 0) {
				jsonResponse.put("m_status_code", -1);
				jsonResponse.put("m_status_msg", "No customer found ");
			} else {
				jsonResponse.put("m_status_code", 1);
				jsonResponse.put("m_status_msg", "");
				JSONArray entities = new JSONArray();
				for(Customer c : customers) {
					JSONObject entity = new JSONObject();
				 
					entity.put("Id", c.getId() + "");
					entity.put("Title", c.getFirstName() + " " + c.getLastName());
					entities.add(entity);
				}
				jsonResponse.put("entities", entities);
			}
			
		} catch (Exception e) {
			jsonResponse.put("m_status_code", -1);
			jsonResponse.put("m_status_msg", "Failed to add a Review: " + e);
		}
		return jsonResponse;
	}

}
