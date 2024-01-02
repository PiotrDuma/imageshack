--liquibase formatted sql
--changeset PiotrDuma:007-create_token_table.sql

CREATE TABLE token(
  token_id BIGINT NOT NULL AUTO_INCREMENT,
  owner_id BIGINT NOT NULL,
  public_id varchar(36) NOT NULL,
  value varchar(36) NOT NULL,
  token_type varchar(255) NOT NULL,
  created DATETIME NOT NULL,
  expires DATETIME NOT NULL,
  PRIMARY KEY (token_id)
);