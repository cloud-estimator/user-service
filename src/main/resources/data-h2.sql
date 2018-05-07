insert into authority (name) values ('ROLE_USER');
insert into authority (name) values ('ROLE_ADMIN');
insert into authority (name) values ('ROLE_ANONYMOUS');

insert into estimator_user (id, activated, activation_key, name, email, image_url, lang_key, login, password_hash, created_date, created_by) values ('8a8081966337e912016337eb4a770003', true, 'BC-1D-F0-A1-6B-DA', 'Dharminder Dharna', 'ddharna@icloud.com', 'http://dummyimage.com/214x250.jpg/ff4444/ffffff', 'PH', 'ddharna', '{bcrypt}$2a$10$CO7jj2DvnwmRoyNQm9pC/umRjQn9htRrHF33HsUV5H.U4qRjSlLBy', '2017-08-06T01:11:55Z', 'ddharna');
insert into estimator_user (id, activated, activation_key, name, email, image_url, lang_key, login, password_hash, created_date, created_by) values ('7a8081966337e912016337eb4a770003', true, 'BC-1D-F0-A1-6B-DA', 'Vic Dharna', 'vdharna@icloud.com', 'http://dummyimage.com/214x250.jpg/ff4444/ffffff', 'PH', 'vdharna', '{bcrypt}$2a$10$CO7jj2DvnwmRoyNQm9pC/umRjQn9htRrHF33HsUV5H.U4qRjSlLBy', '2017-08-06T01:11:55Z', 'ddharna');

insert into account (id, created_by, created_date) values ('8a8081966337e912016337e93d850001', 'system', '2017-08-06T01:11:55Z');

insert into organization (account_id, created_by, created_date, name) values ('8a8081966337e912016337e93d850001', 'system', '2017-08-06T01:11:55Z', 'Golden Gate Consulting');

insert into user_account (account_id, authority_name, user_id) values ('8a8081966337e912016337e93d850001', 'ROLE_ADMIN', '8a8081966337e912016337eb4a770003');
insert into user_account (account_id, authority_name, user_id) values ('8a8081966337e912016337e93d850001', 'ROLE_USER', '7a8081966337e912016337eb4a770003');
