--liquibase formatted sql
--changeset PiotrDuma:002-create_operation_table

CREATE TABLE operations(
   operation_id BIGINT NOT NULL AUTO_INCREMENT,
   operation_type varchar(255) NOT NULL UNIQUE,
   PRIMARY KEY (operation_id)
);