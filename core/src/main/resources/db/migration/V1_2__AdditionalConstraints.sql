ALTER TABLE users
    ADD CONSTRAINT email_or_mobile_present CHECK (
                (CASE WHEN email IS NULL THEN 0 ELSE 1 END) +
                (CASE WHEN mobile IS NULL THEN 0 ELSE 1 END) > 0);

insert into users (email, name, password, created_by_id, last_modified_by_id)
    values ('server@example.com', 'Server Application', '$2a$10$pI2eYogWvj15IJ9.QIZaj.gKmd8aswUqUbp5jcymcICtBFs52aebO', 1, 1);
