USE HultonHotelReservation;

insert into Hotel (id ) values(1), (2);

insert into Customer (first_name, last_name, address, login_id, password)
values('tester', 'tester', 'Lab', 'tester', 'tester');
			 
insert into Admin (  id , first_name, last_name , login_id,password)
	values(1, 'John', 'Doe', 'john', 'johndoe'),
			(2, 'Jane', 'Doe', 'jane', 'janedoe');
            
insert into PhoneBook (id, hotel_id, phone_no ) 
	values(1, 1, '999-999-9999'), (2, 1, '999-999-8888'),
		  (3, 2, '999-999-7777'), (4, 2, '999-999-6666');

insert into HotelAddress (id, hotel_id,street, city ,State ,country,zip ) 
	values(1, 1, '1 Main st', 'Newark', 'New Jersey','USA', '08564'), 
		  (2, 2, '2 Main st', 'New York', 'New York','USA', '10002');


insert into Service (id, hotel_id, price, type) 
	values(1, 1, 23.60,  'Valet Parking'), 
		  (2, 1, 25.90, 'Car Wash'),
          (3, 1, 30.99,'Laundry'), 
		  (4, 2, 30.99,'Laundry');
                  
    
  insert into Room ( id , hotel_id ,room_no, price, description, room_type,floor_no, max_people)   
    values(1, 1, 111, 99.99, "Facing South", "Single", 1, 2), 
		  (2, 1, 211, 99.99, "Facing East", "Double", 2, 3),
          (3, 2, 311, 99.99, "Facing West", "Double", 3, 3), 
		  (4, 2, 411, 199.99, "Facing North","Suite", 4, 6);
 
 insert into Discount ( id ,room_id ,fromDate, toDate, discountpct, taken)   
    values(1, 1, STR_TO_DATE('01-12-2018', '%d-%m-%Y'), STR_TO_DATE('01-30-2018', '%d-%m-%Y'), 10.5, 0), 
		  (2, 4, STR_TO_DATE('02-19-2018', '%d-%m-%Y'), STR_TO_DATE('03-30-2018', '%d-%m-%Y'), 15.5, 0);
   
          
    
  insert into Breakfast ( id ,hotel_id , price, description, type)   
    values(1, 1, 25.95, "Egg & toast",  "Regular"), 
		  (2, 1, 35.95, "Egg & toast + Tea and Orange Juice", "Gourmet"),
          (3, 2, 45.95, "Order whatever you like", "Supreme"), 
		  (4, 1, 35.95, "Egg & toast + Tea and Orange Juice",  "Gourmet");
          

select h.hotel_id, h.street, h.city, h.state,h.country,
       r.room_no, r.room_type, r.price, r.description, r.floor_no, r.max_people  
FROM HotelAddress h left join Room r on r.hotel_id = h.hotel_id;

select h.hotel_id, h.street, h.city, h.state,h.country,
       r.room_no, r.room_type, r.price, r.description, r.floor_no, r.max_people  
FROM HotelAddress h , Room r where r.hotel_id = h.hotel_id order by h.country, h.state;

select h.hotel_id, h.state, h.country,  
	   b.id, b.description, b.price, b.type  
FROM HotelAddress h , Breakfast b where b.hotel_id = h.hotel_id   order by h.country, h.state;

select  b.id,  b.hotel_id, b.description, b.price, b.type FROM Breakfast b order by b.hotel_id;
 
 /*
select * from Hotel;
select * from PhoneBook;
select * from HotelAddress;
select * from ServiceOption;
select * from RoomOption;
select * from Room;
select * from Discount;
select * from Customer;
select * from Reservation;
select * from Admin;
select * from Review;

select rm.hotel_id, rm.room_no, rm.room_type, rm.price, rm.description, rm.floor_no, rm.max_people, rm.id,
	ro.id, ro.discount_id, ro.reservation_id, ro.checkIn, ro.checkOut Date
				 FROM   Room rm, RoomOption ro, Reservation rs 
				 where rm.id = ro.room_id AND ro.reservation_id = rs.id AND rs.customer_id = 1
				 order by rm.hotel_id;
                
                
select rm.hotel_id, rm.id, ro.id, ro.discount_id, ro.reservation_id, ro.checkIn, ro.checkOut Date 
FROM   Room rm, RoomOption ro, Reservation rs 
where rm.id = ro.room_id AND ro.reservation_id = rs.id AND rs.customer_id = 1             

*/

