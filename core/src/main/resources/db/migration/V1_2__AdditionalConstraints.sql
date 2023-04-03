ALTER TABLE users
    ADD CONSTRAINT email_or_mobile_present CHECK (
                (CASE WHEN email IS NULL THEN 0 ELSE 1 END) +
                (CASE WHEN mobile IS NULL THEN 0 ELSE 1 END) > 0);

insert into users (email, name, password, created_by_id, last_modified_by_id)
    values ('server@example.com', 'Server Application', '$2a$10$pI2eYogWvj15IJ9.QIZaj.gKmd8aswUqUbp5jcymcICtBFs52aebO', 1, 1);


insert into users (email, name, password, created_by_id, last_modified_by_id)
values ('Super_Admin@example.com', 'Super Admin', 'Password not setup', 1, 1);

-- The dummy organisation is created so that spring security SQLs are easy to use
insert into organisation (name, created_by_id, last_modified_by_id) values ('TeleSathi', 2, 2);
insert into organisation_user (organisation_id, user_id, user_type, provider_type, created_by_id, last_modified_by_id)
values ((select id from organisation where name = 'TeleSathi'), (select id from users where email = 'Super_Admin@example.com'), 'Admin', 'None', 2, 2);
