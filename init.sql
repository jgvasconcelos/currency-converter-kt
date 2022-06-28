-- SCHEMA AND TABLE CREATION SCRIPT

create schema users;

create schema conversions;

create table users.user_info
(
    id            bigserial   not null
        constraint user_info_pk
            primary key,
    username  varchar(50) not null
        constraint user_info_username_unique unique,
    password         char(64)    not null,
    register_date  date   not null,
    active         boolean     not null
);

alter table users.user_info
    owner to jose;

create unique index user_id_uindex
    on users.user_info (id);


create table conversions.currency
(
    id         serial      not null
        constraint currency_pk
            primary key,
    description  varchar(50) not null,
    short_name char(3)     not null
        constraint currency_short_name_unique unique
);

alter table conversions.currency
    owner to jose;

create unique index currency_id_uindex
    on conversions.currency (id);

create table conversions.transaction
(
    id                  bigserial not null
        constraint transaction_pk
            primary key,
    original_currency   bigint    not null
        constraint transaction_currency_id_fk
            references conversions.currency,
    original_value      numeric   not null,
    target_currency     bigint    not null
        constraint transaction_currency_id_fk_2
            references conversions.currency,
    conversion_rate     numeric   not null,
    transaction_datetime timestamp not null,
    user_id             integer   not null
        constraint transaction_user_info_id_fk
            references users.user_info
);

alter table conversions.transaction
    owner to jose;

create unique index transaction_id_uindex
    on conversions.transaction (id);

-- CORE DATA INSERT SCRIPT

insert into users.user_info
(username, password, register_date, active)
values ('admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', now(), true);

INSERT INTO conversions.currency (description, short_name) VALUES ('US Dollar', 'USD');
INSERT INTO conversions.currency (description, short_name) VALUES ('Brazilian Real', 'BRL');
INSERT INTO conversions.currency (description, short_name) VALUES ('European Euro', 'EUR');
INSERT INTO conversions.currency (description, short_name) VALUES ('Japanese Yen', 'JPY');