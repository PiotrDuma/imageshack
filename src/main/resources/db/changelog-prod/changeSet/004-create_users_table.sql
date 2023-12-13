--liquibase formatted sql
--changeset PiotrDuma:004-create_users_table

CREATE TABLE users(
  user_id BIGINT NOT NULL AUTO_INCREMENT,
  username varchar(255) UNIQUE NOT NULL,
  email varchar(255) UNIQUE NOT NULL,
  password varchar(255) NOT NULL,
  PRIMARY KEY (user_id)
);