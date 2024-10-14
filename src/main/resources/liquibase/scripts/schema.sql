-- liquibase formatted sql

-- changeset sergei:1
CREATE TABLE users(
     id          SERIAL     primary key
    ,username    TEXT
    ,password    TEXT
    ,first_name  TEXT
    ,last_name   TEXT
    ,phone       TEXT
    ,image       TEXT
    ,role        TEXT
);
-- changeset sergei:2
CREATE TABLE ad(
     id             BIGSERIAL     primary key
    ,author         BIGSERIAL
    ,pk             BIGSERIAL
    ,price          TEXT
    ,title          TEXT
);