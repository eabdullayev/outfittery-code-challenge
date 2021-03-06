--init db

--init time_slot table
INSERT INTO TIME_SLOTS(ID, "TIME_INDEX","TIME") VALUES(seq_time_slot_id.nextVal, 0, '09:00')
INSERT INTO TIME_SLOTS(ID, "TIME_INDEX","TIME") VALUES(seq_time_slot_id.nextVal, 1, '09:30')
INSERT INTO TIME_SLOTS(ID, "TIME_INDEX","TIME") VALUES(seq_time_slot_id.nextVal, 2, '10:00')
INSERT INTO TIME_SLOTS(ID, "TIME_INDEX","TIME") VALUES(seq_time_slot_id.nextVal, 3, '10:30')
INSERT INTO TIME_SLOTS(ID, "TIME_INDEX","TIME") VALUES(seq_time_slot_id.nextVal, 4, '11:00')
INSERT INTO TIME_SLOTS(ID, "TIME_INDEX","TIME") VALUES(seq_time_slot_id.nextVal, 5, '11:30')
INSERT INTO TIME_SLOTS(ID, "TIME_INDEX","TIME") VALUES(seq_time_slot_id.nextVal, 6, '12:00')
INSERT INTO TIME_SLOTS(ID, "TIME_INDEX","TIME") VALUES(seq_time_slot_id.nextVal, 7, '12:30')
INSERT INTO TIME_SLOTS(ID, "TIME_INDEX","TIME") VALUES(seq_time_slot_id.nextVal, 8, '13:00')
INSERT INTO TIME_SLOTS(ID, "TIME_INDEX","TIME") VALUES(seq_time_slot_id.nextVal, 9, '13:30')
INSERT INTO TIME_SLOTS(ID, "TIME_INDEX","TIME") VALUES(seq_time_slot_id.nextVal, 10,'14:00')
INSERT INTO TIME_SLOTS(ID, "TIME_INDEX","TIME") VALUES(seq_time_slot_id.nextVal, 11,'14:30')
INSERT INTO TIME_SLOTS(ID, "TIME_INDEX","TIME") VALUES(seq_time_slot_id.nextVal, 12,'15:00')
INSERT INTO TIME_SLOTS(ID, "TIME_INDEX","TIME") VALUES(seq_time_slot_id.nextVal, 13,'15:30')
INSERT INTO TIME_SLOTS(ID, "TIME_INDEX","TIME") VALUES(seq_time_slot_id.nextVal, 14,'16:00')
INSERT INTO TIME_SLOTS(ID, "TIME_INDEX","TIME") VALUES(seq_time_slot_id.nextVal, 15,'16:30')

--init stylist table
INSERT INTO STYLISTS(ID, NAME, STATE) VALUES(SEQ_STYLIST_ID.nextVal, 'Antonio', 'READY_TO_STYLE')
INSERT INTO STYLISTS(ID, NAME, STATE) VALUES(SEQ_STYLIST_ID.nextVal, 'Diana', 'READY_TO_STYLE')

--init leave table
INSERT INTO LEAVES(ID, BEGIN, END, STYLIST_ID, LEAVE_TYPE) VALUES(SEQ_LEAVE_ID.NEXTVAL, CURRENT_DATE(), CURRENT_DATE()+3, SEQ_STYLIST_ID.CURRVAL, 'ON_HOLIDAY')

--init customer table
INSERT INTO CUSTOMERS(ID, NAME) VALUES(SEQ_CUSTOMER_ID.nextVal, 'John')
INSERT INTO CUSTOMERS(ID, NAME) VALUES(SEQ_CUSTOMER_ID.nextVal, 'Tom')

--init reservation table
INSERT INTO RESERVATIONS(ID, TIME_INDEX, STYLIST_ID, CUSTOMER_ID, DATE) VALUES(SEQ_RESERVATION_ID.NEXTVAL, 0, 1, 1, CURRENT_DATE())
--INSERT INTO RESERVATIONS(ID, TIME_INDEX, STYLIST_ID, CUSTOMER_ID, DATE) VALUES(SEQ_RESERVATION_ID.NEXTVAL, 15, 1, 2, '2018-08-09')
