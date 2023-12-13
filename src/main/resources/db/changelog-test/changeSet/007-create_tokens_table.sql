--liquibase formatted sql
--changeset PiotrDuma:007-create_tokens_table.sql

CREATE TABLE tokens(
  token_id BIGINT NOT NULL AUTO_INCREMENT,
  email varchar(255) NOT NULL,
  token varchar(32) NOT NULL,
  token_type varchar(255) NOT NULL,
  created DATETIME NOT NULL,
  expired DATETIME NOT NULL,
  PRIMARY KEY (token_id)
);