create table users
(
    id                 serial primary key,
    email              varchar(255)                         not null unique,
    name               varchar(255)                         not null,
    password           varchar(255)                         not null,
    inactive           boolean   default false              not null,
    uuid               uuid      default uuid_generate_v4() not null unique,
    created_date       timestamp default (now()):: timestamp without time zone not null,
    last_modified_date timestamp default (now()):: timestamp without time zone not null
);

create table organisation
(
    id                 serial primary key,
    name               varchar(255)                         not null,
    inactive           boolean   default false              not null,
    uuid               uuid      default uuid_generate_v4() not null unique,
    created_date       timestamp default (now()):: timestamp without time zone not null,
    last_modified_date timestamp default (now()):: timestamp without time zone not null
);

create table organisation_users
(
    id              serial primary key,
    users_id        int not null,
    organisation_id int not null
);
ALTER TABLE ONLY organisation_users
    ADD CONSTRAINT organisation_users_users FOREIGN KEY (users_id) REFERENCES users (id);
ALTER TABLE ONLY organisation_users
    ADD CONSTRAINT organisation_users_organisation FOREIGN KEY (organisation_id) REFERENCES organisation (id);

create table role
(
    id                 serial primary key,
    name               varchar(255)                         not null,
    inactive           boolean   default false              not null,
    uuid               uuid      default uuid_generate_v4() not null unique,
    created_date       timestamp default (now()):: timestamp without time zone not null,
    last_modified_date timestamp default (now()):: timestamp without time zone not null
);

create table privilege
(
    id                 serial primary key,
    name               varchar(255)                         not null,
    inactive           boolean   default false              not null,
    uuid               uuid      default uuid_generate_v4() not null unique,
    created_date       timestamp default (now()):: timestamp without time zone not null,
    last_modified_date timestamp default (now()):: timestamp without time zone not null
);

create table role_privilege
(
    id           serial primary key,
    privilege_id int not null,
    role_id      int not null
);
ALTER TABLE ONLY role_privilege
    ADD CONSTRAINT role_privilege_privilege FOREIGN KEY (privilege_id) REFERENCES privilege (id);
ALTER TABLE ONLY role_privilege
    ADD CONSTRAINT role_privilege_role FOREIGN KEY (role_id) REFERENCES role (id);
