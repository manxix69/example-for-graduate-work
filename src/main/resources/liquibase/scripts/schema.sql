-- liquibase formatted sql

-- changeset sergei:1
CREATE TABLE users(
     id          INTEGER      primary key
    ,username    TEXT
    ,password    TEXT
    ,first_name  TEXT
    ,last_name   TEXT
    ,phone       TEXT
    ,image       TEXT
    ,role        TEXT
    ,file_path   TEXT
);
-- changeset sergei:2
CREATE TABLE photos(
     id          INTEGER      primary key
    ,file_path   TEXT
    ,file_size   BIGINT
    ,media_type  TEXT
    ,data        BYTEA
);
-- changeset sergei:3
CREATE TABLE ads(
     id           INTEGER      primary key
    ,title        TEXT
    ,price        INTEGER
    ,description  TEXT
    ,photo        INTEGER      REFERENCES photos
    ,author       INTEGER      REFERENCES users
    ,file_path    TEXT
);
-- changeset sergei:4
CREATE TABLE comments(
     id          INTEGER       primary key
    ,text        TEXT
    ,created_at  BIGINT
    ,author      INTEGER      REFERENCES users
    ,ad          INTEGER      REFERENCES ads
);



