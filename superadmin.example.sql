-- check /test/open/passwordHash
insert into users (email, name, password, created_by_id, last_modified_by_id)
    values ('Super_Admin@example.com', 'Super Admin', 'HASH_OF_YOUR_SUPER_ADMIN_PASSWORD', 1, 1);

-- The dummy organisation is created so that spring security SQLs are easy to use
insert into organisation (name, created_by_id, last_modified_by_id) values ('OpenTele Consult', 2, 2);
insert into organisation_user (organisation_id, user_id, user_type, provider_type, created_by_id, last_modified_by_id)
    values ((select id from organisation where name = 'OpenTele Consult'), (select id from users where email = 'Super_Admin@example.com'), 'Admin', 'None', 2, 2);
