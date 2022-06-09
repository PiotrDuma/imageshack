--liquibase formatted sql
--changeset PiotrDuma:006-create_users_roles_table

CREATE TABLE users_roles(
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(user_id),
  FOREIGN KEY (role_id) REFERENCES roles(role_id)
);