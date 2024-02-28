ALTER TABLE user_auth_code_entity
DROP
COLUMN creation_date_time;

ALTER TABLE user_auth_code_entity
DROP
COLUMN creation_time;

ALTER TABLE user_auth_code_entity
DROP
COLUMN creation_date;

ALTER TABLE user_auth_code_entity
    ADD creation_date TIMESTAMP WITHOUT TIME ZONE;