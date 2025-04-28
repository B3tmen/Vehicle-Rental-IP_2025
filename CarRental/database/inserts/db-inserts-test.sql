# ---- Manufacturers ----
# ALTER TABLE manufacturer AUTO_INCREMENT 3;
INSERT INTO `manufacturer` (name, state, address, phone_number, fax, email)
VALUES ('BMW', 'Germany', 'Address1', '065/123-456', '123456', 'mtest1@mail.com'); #1
INSERT INTO `manufacturer` (name, state, address, phone_number, fax, email)
VALUES ('Ford', 'USA', 'Address2', '065/234-567', '234567', 'mtest2@mail.com'); #2
INSERT INTO `manufacturer` (name, state, address, phone_number, fax, email)
VALUES ('Tesla', 'USA', 'Address3', '065/234-560', '2345678', 'mtest3@mail.com'); #3
INSERT INTO `manufacturer` (name, state, address, phone_number, fax, email)
VALUES ('Binbin', 'Turkey', 'Address4', '065/456-123', '23456789', 'binbin@mail.com'); #4
# ---- Manufacturers ----




# ---- Images ----
INSERT INTO `image` (name, type) VALUES ('eb1df534-1f9d-4dfa-94ae-febe06539e56.jpg', 'image/jpeg');  #1
INSERT INTO `image` (name, type) VALUES ('dd7bf7d6-452c-4098-8d54-9311ca429413.webp', 'image/webp'); #2
INSERT INTO `image` (name, type) VALUES ('90d307d9-8ff2-498f-bed5-acc0141a8cc8.jpg', 'image/jpeg');  #3
# ---- Images ----


# ---- Users ----
# ALTER TABLE user AUTO_INCREMENT 1
INSERT INTO `user` (username, password_hash, first_name, last_name, type_) #1
VALUES ('username1', '$2a$12$YGE0Xo43vuhGz5vlrfzMiefFYwIb5MYXdD/lUnQXqDuwqZ9DCdKD2', 'John', 'Doe', 'Client'); # pass: password1
INSERT INTO `client` (fk_user_id, personal_card_number, email, phone_number, citizen_type, drivers_licence, fk_avatar_image_id)
VALUES (1, '1230004400056', 'john@mail.com', '066/111-222', 'Local', 12345, 1);

INSERT INTO `user` (username, password_hash, first_name, last_name, type_) #2
VALUES ('username2', '$2a$12$8nXaDTQ4YsF241hwoXdpV.JJCZ6RaaL1ZeLvozukpQQnl5ZBWB9KS', 'Jane', 'Doe', 'Employee'); # pass: password2
INSERT INTO `employee` (fk_user_id, role)
VALUES (2, 'Administrator');

INSERT INTO `user` (username, password_hash, first_name, last_name, type_) #3
VALUES ('username3', '$2a$12$H37LU/h46yhuLRokY6qG7O2fDYSh8C.m4yi2HgzQOb62b9h6fhH1C', 'John', 'Smith', 'Employee');
INSERT INTO `employee` (fk_user_id, role)
VALUES (3, 'Operator');

INSERT INTO `user` (username, password_hash, first_name, last_name, type_) #3
VALUES ('username4', '$2a$12$GpqVrChLMF6QRvN2RujJIOVW874.ES9eRrN9bE35Iy//pL/kPAE8.', 'Michael', 'Scofield', 'Employee');  # easter egg :)
INSERT INTO `employee` (fk_user_id, role)
VALUES (4, 'Manager');
# ---- Users ----


# ---- Rental status ----
INSERT INTO `rental_status` (status) VALUES ('Free');
INSERT INTO `rental_status` (status) VALUES ('Rented');
INSERT INTO `rental_status` (status) VALUES ('Broken');
# ---- Rental status ----


# ---- Vehicles ----
# ALTER TABLE rental_vehicle AUTO_INCREMENT 1;
INSERT INTO `rental_vehicle` (model, price, rental_price, type_, is_active, fk_manufacturer_id, fk_image_id, fk_rental_status_id, fk_promotion_id, fk_announcement_id) #1
VALUES ('Model X', 30000, 50, 'Car', 1, 3, 2, 1, null, null);
INSERT INTO `electric_car` (fk_vehicle_id, car_id, purchase_date, description)
VALUES (1, 'TESLA_M_X', '2025-09-01 00:00:00', 'Deskripcija Tesla Model X');

INSERT INTO `rental_vehicle` (model, price, rental_price, type_, is_active, fk_manufacturer_id, fk_image_id, fk_rental_status_id, fk_promotion_id, fk_announcement_id) #2
VALUES ('El. Bicikl', 3000, 50, 'Bicycle', 1, 2, null, 1, null, null);
INSERT INTO `electric_bicycle` (fk_vehicle_id, bicycle_id, riding_autonomy)
VALUES (2, 'EL_BIKE_1', 50);

INSERT INTO `rental_vehicle` (model, price, rental_price, type_, is_active, fk_manufacturer_id, fk_image_id, fk_rental_status_id, fk_promotion_id, fk_announcement_id) #3
VALUES ('Binbin', 3000, 50, 'Scooter', 1, 4, 3, 1, null, null);
INSERT INTO `electric_scooter` (fk_vehicle_id, scooter_id, max_speed)
VALUES (3, 'BIN_BIN_BL', 15);
# ---- Vehicles ----


# ---- Location + Payment + Rental  + Invoice ----
INSERT INTO `location` (latitude, longitude) VALUES (44.757959, 17.187796);

INSERT INTO `payment` (token, type, expiry_date, card_holder_first_name, card_holder_last_name, card_last_4_digits, fk_client_id)
VALUES ('7ff2005690de9c9c4f9685ab601ef8ec1c44d97bcaa4a3ab53fbfb2e2606f056', 'Visa', '2026-05-01', 'John', 'Doe', '5678', 1);

INSERT INTO `rental` (fk_vehicle_id, fk_client_id, rental_date_time, duration, fk_pickup_location_id, fk_dropoff_location_id, fk_payment_id) 
VALUES (1, 1, '2025-04-16 21:46:22', 1, 1, 1, 1);

INSERT INTO `invoice` (pdf_name, issue_date, grand_total, fk_rental_id)
VALUES ('e393443c-153b-40cc-936a-7d9777e87dfd.pdf', '2025-04-16', 123.00, 1);
# ---- Location + Payment + Rental  + Invoice ----

