insert into customer values (1, "vlada", "$2a$10$2xkpMfhfcTOy3G1zVEzPoOt0riZ9NK4UitmD6iKveHrmMcIPvjUpS", "Vladimir Cvetanovic");
insert into customer values (2, "marko", "$2a$10$3eunmta1fd4kGI8Wip0rAOiae/MwuPNFvgT8a7IonguUspZKVALjS", "Marko Stevanov");

/* CASOPISI - PRODAVCI */
insert into customer values (3, "Naucni kutak","$2a$10$iHLUEnk5gYMa50EclC.cEu0UTBIM2wyNAloS/59yC.MN/cAe88NeS", "Milica Makaric");
insert into customer values (4, "Savremena psihologija", "$2a$10$UWAujpNefPsU/eFanwGire/u4hpQJ0Djw0BZYtMdjcZ37ivQI4vRe", "Vukasin Jovic");
insert into customer values (5, "Nauka danas", "$2a$10$TWPesMnqNm66Z9vNd/b5UudKvezOzPiuYGjI36MvonEzfvUaN8FRq", "Andrijana Jeremic");

insert into bank (id, name) values (1, "OTP bank");
insert into bank (id, name) values (2, "Intesa");
insert into bank (id, name) values (3, "Addiko bank");

insert into bank_account (id, pan, cardholder_name, expiration_date, service_code, balance, reserved, bank_id, customer_id)
values (1, "1111111111111111", "Vladimir Cvetanovic", '2020-03-20 00:00:00', "111", 10000, 0, 1, 1);
insert into bank_account (id, pan, cardholder_name, expiration_date, service_code, balance, reserved, bank_id, customer_id)
values (2, "3333332222222222", "Marko Stevanov", '2020-04-20 00:00:00', "222", 10000, 0, 2, 2);

/* CASOPISI - RACUNI */
insert into bank_account (id, pan, cardholder_name, expiration_date, service_code, balance, reserved, bank_id, customer_id)
values (3, "1111113333333333", "Naucni kutak", '2020-05-20 00:00:00', "333", 10000, 0, 2, 3);
insert into bank_account (id, pan, cardholder_name, expiration_date, service_code, balance, reserved, bank_id, customer_id)
values (4, "2222224444444444", "Savremena psihologija", '2020-05-20 00:00:00', "444", 10000, 0, 1, 4);
insert into bank_account (id, pan, cardholder_name, expiration_date, service_code, balance, reserved, bank_id, customer_id)
values (5, "2222225555555555", "Nauka danas", '2020-05-20 00:00:00', "555", 10000, 0, 3, 5);