drop table user if exists;

create table user (id varchar(255) not null, activated boolean not null, activation_key varchar(20), created_by varchar(50) not null, created_date timestamp not null, email varchar(100), first_name varchar(50), image_url varchar(256), lang_key varchar(6), last_modified_by varchar(50), last_modified_date timestamp, last_name varchar(50), login varchar(50) not null, password_hash varchar(60) not null, reset_date timestamp, reset_key varchar(20), primary key (id));
alter table user add constraint UK_ob8kqyqqgmefl0aco34akdtpe unique (email);
alter table user add constraint UK_ew1hvam8uwaknuaellwhqchhb unique (login);