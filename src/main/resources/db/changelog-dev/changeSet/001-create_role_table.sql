--liquibase formatted sql
--changeset PiotrDuma:001-create_role_table

CREATE TABLE roles(
   role_id BIGINT AUTO_INCREMENT,
   role_type varchar(255) NOT NULL UNIQUE,
   PRIMARY KEY (role_id)
);