insert into customer values (1, "vlada", "$2a$10$2xkpMfhfcTOy3G1zVEzPoOt0riZ9NK4UitmD6iKveHrmMcIPvjUpS", "Vladimir Cvetanovic");
insert into customer values (2, "vukasin", "$2a$10$UWAujpNefPsU/eFanwGire/u4hpQJ0Djw0BZYtMdjcZ37ivQI4vRe", "Vukasin Jovic");

insert into bank (id, name) values (1, "OTP bank");
insert into bank (id, name) values (2, "Intesa");
insert into bank (id, name) values (3, "Addiko bank");

insert into bank_account (id, pan, cardholder_name, expiration_date, service_code, balance, reserved, bank_id, customer_id)
values (1, "1234123412341234", "Vukasin Jovic", '2020-05-20 00:00:00', "111", 10000, 0, 1, 2);
insert into bank_account (id, pan, cardholder_name, expiration_date, service_code, balance, reserved, bank_id, customer_id)
values (2, "1111111111111111", "Vladimir Cvetanovic", '2020-03-20 00:00:00', "111", 10000, 0, 1, 1);