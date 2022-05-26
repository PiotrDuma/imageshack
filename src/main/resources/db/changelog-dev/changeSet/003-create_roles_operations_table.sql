--liquibase formatted sql
--changeset PiotrDuma:003-create_roles_operations_table

CREATE TABLE roles_operations(
   role_id int NOT NULL,
   operation_id int NOT NULL,
   FOREIGN KEY (role_id) REFERENCES roles(role_id),
   FOREIGN KEY (operation_id) REFERENCES operations(operation_id)
);