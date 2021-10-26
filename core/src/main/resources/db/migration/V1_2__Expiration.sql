create table password_reset_token
(
    id                 serial primary key,
    token              varchar(255)                                            not null,
    expiry_date        timestamp without time zone                             not null,
    user_id            int                                                     not null references users (id),
    inactive           boolean   default false                                 not null,
    uuid               uuid      default uuid_generate_v4()                    not null unique,
    created_date       timestamp default (now()):: timestamp without time zone not null,
    last_modified_date timestamp default (now()):: timestamp without time zone not null
);
