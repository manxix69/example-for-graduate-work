-- liquibase formatted sql

-- changeset sergei:1
CREATE TABLE users(
     id          SERIAL     primary key
    ,username    TEXT
    ,password    TEXT
    ,email       TEXT
    ,first_name  TEXT
    ,last_name   TEXT
    ,phone       TEXT
    ,image       TEXT
    ,role        TEXT
);
