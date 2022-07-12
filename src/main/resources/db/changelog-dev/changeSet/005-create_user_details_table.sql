--liquibase formatted sql
--changeset PiotrDuma:005-create_user_details_table

CREATE TABLE user_details(
  details_id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  account_non_expired bit,
  account_non_locked bit,
  credentials_non_expired bit,
  enabled bit,
  PRIMARY KEY (details_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id)
);