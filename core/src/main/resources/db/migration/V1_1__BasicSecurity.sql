create table organisation
(
    id                 serial primary key,
    name               varchar(255)                         not null,
    inactive           boolean   default false              not null,
    uuid               uuid      default uuid_generate_v4() not null unique,
    created_date       timestamp default (now()):: timestamp without time zone not null,
    last_modified_date timestamp default (now()):: timestamp without time zone not null
);

create table users
(
    id                 serial primary key,
    email              varchar(255)                         not null unique,
    name               varchar(255)                         not null,
    password           varchar(255)                         not null,
    user_type          varchar(100)                         not null,
    phone              varchar(30)                          null,
    inactive           boolean   default false              not null,
    uuid               uuid      default uuid_generate_v4() not null unique,
    created_date       timestamp default (now()):: timestamp without time zone not null,
    last_modified_date timestamp default (now()):: timestamp without time zone not null,
    organisation_id    int                                  not null references organisation (id)
);
