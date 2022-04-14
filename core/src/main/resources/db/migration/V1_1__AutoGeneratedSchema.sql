create table appointment (id  serial not null, inactive boolean default false not null, uuid uuid default uuid_generate_v4() not null, created_date timestamp default (now()):: timestamp without time zone not null, last_modified_date timestamp default (now()):: timestamp without time zone not null, is_current boolean, queue_number int4 not null, created_by_id int4 not null, last_modified_by_id int4 not null, organisation_id integer not null, appointment_provider_id integer not null, client_id integer not null, consultation_id integer null, consultation_room_id integer not null, primary key (id));
create table appointment_user (id  serial not null, inactive boolean default false not null, uuid uuid default uuid_generate_v4() not null, created_date timestamp default (now()):: timestamp without time zone not null, last_modified_date timestamp default (now()):: timestamp without time zone not null, created_by_id int4 not null, last_modified_by_id int4 not null, organisation_id integer not null, appointment_id integer not null, user_id integer not null, primary key (id));
create table client (id  serial not null, inactive boolean default false not null, uuid uuid default uuid_generate_v4() not null, created_date timestamp default (now()):: timestamp without time zone not null, last_modified_date timestamp default (now()):: timestamp without time zone not null, date_of_birth date not null, gender varchar(100) not null, name varchar(255), registration_number varchar(100), created_by_id int4 not null, last_modified_by_id int4 not null, organisation_id integer not null, primary key (id));
create table consultant_room_user (id  serial not null, inactive boolean default false not null, uuid uuid default uuid_generate_v4() not null, created_date timestamp default (now()):: timestamp without time zone not null, last_modified_date timestamp default (now()):: timestamp without time zone not null, created_by_id int4 not null, last_modified_by_id int4 not null, organisation_id integer not null, consultation_room_id integer not null, user_id integer not null, primary key (id));
create table consultation (id  serial not null, inactive boolean default false not null, uuid uuid default uuid_generate_v4() not null, created_date timestamp default (now()):: timestamp without time zone not null, last_modified_date timestamp default (now()):: timestamp without time zone not null, created_by_id int4 not null, last_modified_by_id int4 not null, organisation_id integer not null, appointment_id integer null, primary key (id));
create table consultation_room (id  serial not null, inactive boolean default false not null, uuid uuid default uuid_generate_v4() not null, created_date timestamp default (now()):: timestamp without time zone not null, last_modified_date timestamp default (now()):: timestamp without time zone not null, end_time time, scheduled_end_time time not null, scheduled_on date not null, scheduled_start_time time not null, start_time time, title varchar(255) not null, total_slots int4, created_by_id int4 not null, last_modified_by_id int4 not null, organisation_id integer not null, consultation_room_schedule_id integer not null, primary key (id));
create table consultation_room_schedule (id  serial not null, inactive boolean default false not null, uuid uuid default uuid_generate_v4() not null, created_date timestamp default (now()):: timestamp without time zone not null, last_modified_date timestamp default (now()):: timestamp without time zone not null, end_time time not null, recurrence_rule varchar(255) not null, start_date date not null, start_time time not null, title varchar(255) not null, total_slots int4 check (total_slots > 0) not null, created_by_id int4 not null, last_modified_by_id int4 not null, organisation_id integer not null, primary key (id));
create table consultation_room_schedule_user (id  serial not null, inactive boolean default false not null, uuid uuid default uuid_generate_v4() not null, created_date timestamp default (now()):: timestamp without time zone not null, last_modified_date timestamp default (now()):: timestamp without time zone not null, created_by_id int4 not null, last_modified_by_id int4 not null, organisation_id integer not null, consultation_room_schedule_id integer not null, user_id integer not null, primary key (id));
create table consultation_session_record (id  serial not null, inactive boolean default false not null, uuid uuid default uuid_generate_v4() not null, created_date timestamp default (now()):: timestamp without time zone not null, last_modified_date timestamp default (now()):: timestamp without time zone not null, complaints varchar(10000) not null, key_inference varchar(10000) not null, observations varchar(10000) not null, recommendations varchar(10000) not null, created_by_id int4 not null, last_modified_by_id int4 not null, organisation_id integer not null, client_id integer not null, primary key (id));
create table organisation (id  serial not null, inactive boolean default false not null, uuid uuid default uuid_generate_v4() not null, created_date timestamp default (now()):: timestamp without time zone not null, last_modified_date timestamp default (now()):: timestamp without time zone not null, name varchar(255) not null, created_by_id int4 not null, last_modified_by_id int4 not null, primary key (id));
create table organisation_user (id  serial not null, inactive boolean default false not null, uuid uuid default uuid_generate_v4() not null, created_date timestamp default (now()):: timestamp without time zone not null, last_modified_date timestamp default (now()):: timestamp without time zone not null, provider_type varchar(255) not null, user_type varchar(255) not null, created_by_id int4 not null, last_modified_by_id int4 not null, organisation_id integer not null, user_id integer not null, primary key (id));
create table password_reset_token (id  serial not null, inactive boolean default false not null, uuid uuid default uuid_generate_v4() not null, created_date timestamp default (now()):: timestamp without time zone not null, last_modified_date timestamp default (now()):: timestamp without time zone not null, expiry_date timestamp not null, token varchar(255) not null, created_by_id int4 not null, last_modified_by_id int4 not null, user_id int4 not null, primary key (id));
create table privilege (id  serial not null, inactive boolean default false not null, uuid uuid default uuid_generate_v4() not null, created_date timestamp default (now()):: timestamp without time zone not null, last_modified_date timestamp default (now()):: timestamp without time zone not null, name varchar(255) not null, created_by_id int4 not null, last_modified_by_id int4 not null, primary key (id));
create table role (id  serial not null, inactive boolean default false not null, uuid uuid default uuid_generate_v4() not null, created_date timestamp default (now()):: timestamp without time zone not null, last_modified_date timestamp default (now()):: timestamp without time zone not null, name varchar(255) not null, created_by_id int4 not null, last_modified_by_id int4 not null, primary key (id));
create table role_privilege (role_id int4 not null, privilege_id int4 not null, primary key (role_id, privilege_id));
create table tele_conference (id  serial not null, inactive boolean default false not null, uuid uuid default uuid_generate_v4() not null, created_date timestamp default (now()):: timestamp without time zone not null, last_modified_date timestamp default (now()):: timestamp without time zone not null, jitsi_id varchar(255), created_by_id int4 not null, last_modified_by_id int4 not null, organisation_id integer not null, consultation_room_id integer not null, primary key (id));
create table users (id  serial not null, inactive boolean default false not null, uuid uuid default uuid_generate_v4() not null, created_date timestamp default (now()):: timestamp without time zone not null, email varchar(255), last_modified_date timestamp default (now()):: timestamp without time zone not null, mobile varchar(255), name varchar(255) not null, password varchar(255) not null, created_by_id int4 not null, last_modified_by_id int4 not null, primary key (id));
alter table if exists appointment add constraint UKgldxvfhif8y51210rt4je8ucl unique (consultation_room_id, client_id);
alter table if exists appointment add constraint UK_ax5xe41hcl38kiq3tr9oapt5y unique (uuid);
alter table if exists appointment_user add constraint UK_72s0sgnltk1eitp8bw61f5w8t unique (uuid);
alter table if exists client add constraint UK_ffov2uk8f9qvop5b4nvt9hgvw unique (uuid);
alter table if exists consultant_room_user add constraint UK_dwlq6qt8m7ebih9f5gev268bi unique (uuid);
alter table if exists consultation add constraint UK_g5j68qd6m5yvuqqeuoq7mknxv unique (uuid);
alter table if exists consultation_room add constraint UK_s1ghvv4d4p4h80jbm4q8qa1kc unique (uuid);
alter table if exists consultation_room_schedule add constraint UK_am9e1jc133umpuu0mjrx2p4ht unique (uuid);
alter table if exists consultation_room_schedule_user add constraint UK_85pd36hc5vj81l3g8cboc25ke unique (uuid);
alter table if exists consultation_session_record add constraint UK_p4k8glsc30w3orw1kxibhabe4 unique (uuid);
alter table if exists organisation add constraint UK_a0spar1od6i2qp7n3b3l0ug5y unique (uuid);
alter table if exists organisation_user add constraint UK_e20k00juxw43g38hd3b7x1tbj unique (uuid);
alter table if exists password_reset_token add constraint UK_b83nimwg75cdko9pp1lkdp892 unique (uuid);
alter table if exists privilege add constraint UK_hkkbq0189qlql9ss5srnoltsa unique (uuid);
alter table if exists role add constraint UK_k5dwya5n8n7y3m2opvmm7qjcc unique (uuid);
alter table if exists tele_conference add constraint UK_jotwuv73im2nqmqtqcnpf9j4r unique (uuid);
alter table if exists users add constraint UK_6km2m9i3vjuy36rnvkgj1l61s unique (uuid);
alter table if exists appointment add constraint FKcfabgaya6yleguuurel3lqxf foreign key (created_by_id) references users;
alter table if exists appointment add constraint FKbmnk20j2682f27kh2c1s5ca4b foreign key (last_modified_by_id) references users;
alter table if exists appointment add constraint FKrfpdralj9wgi9p8am8tyy6sjq foreign key (organisation_id) references organisation;
alter table if exists appointment add constraint FKhe0u7elcsutmy3oxgr3n2sjap foreign key (appointment_provider_id) references users;
alter table if exists appointment add constraint FK3gbqcfd3mnwwcit63lybpqcf8 foreign key (client_id) references client;
alter table if exists appointment add constraint FK5eeq95jip601cui2ep32stak8 foreign key (consultation_id) references consultation;
alter table if exists appointment add constraint FK827kv3xgewvtwgd68skmdn1ft foreign key (consultation_room_id) references consultation_room;
alter table if exists appointment_user add constraint FKibt2nd43xtr4kvomt0x6gq8h8 foreign key (created_by_id) references users;
alter table if exists appointment_user add constraint FK8wlqoub3lnefhkiittnvhvkb foreign key (last_modified_by_id) references users;
alter table if exists appointment_user add constraint FK7u9victrn7w5c0u6fjh2yotny foreign key (organisation_id) references organisation;
alter table if exists appointment_user add constraint FK7odjffcpfrviiy9o8qc87t23m foreign key (appointment_id) references appointment;
alter table if exists appointment_user add constraint FK8rerio2sj9ftvnxchmvesq7k6 foreign key (user_id) references users;
alter table if exists client add constraint FK52lw11fkj3pdhjiad26ubmvvo foreign key (created_by_id) references users;
alter table if exists client add constraint FKfwlj6q95qwx9h5wlqhpjl2pop foreign key (last_modified_by_id) references users;
alter table if exists client add constraint FK1i6inmxcixwq8mgfaxgwekb3o foreign key (organisation_id) references organisation;
alter table if exists consultant_room_user add constraint FKe16n88bk1qoib9x3xo11kj4gh foreign key (created_by_id) references users;
alter table if exists consultant_room_user add constraint FKl980di69eh4wuq4ym5ajqkvjf foreign key (last_modified_by_id) references users;
alter table if exists consultant_room_user add constraint FKt3j67y94utyln6ny1g66rnbuq foreign key (organisation_id) references organisation;
alter table if exists consultant_room_user add constraint FK137xov2h8qa2meo6dltnxffei foreign key (consultation_room_id) references consultation_room;
alter table if exists consultant_room_user add constraint FK4f3cb2gr35vuttutuqflpk92s foreign key (user_id) references users;
alter table if exists consultation add constraint FKnmpufjalrm2p1e3eb9bwvgjl3 foreign key (created_by_id) references users;
alter table if exists consultation add constraint FKjyenlx06qx9presfug0sqf384 foreign key (last_modified_by_id) references users;
alter table if exists consultation add constraint FKn2tel9xq78m3im4rk8q1ctmu6 foreign key (organisation_id) references organisation;
alter table if exists consultation add constraint FKoegqlitug8x2a09slhgtl0ea5 foreign key (appointment_id) references appointment;
alter table if exists consultation_room add constraint FKq9objux4petvdak69e9xs1oc2 foreign key (created_by_id) references users;
alter table if exists consultation_room add constraint FK1t142cgvanubchjkfu3d65sfv foreign key (last_modified_by_id) references users;
alter table if exists consultation_room add constraint FKjgd2c02r4yt05fleyks5xnarl foreign key (organisation_id) references organisation;
alter table if exists consultation_room add constraint FKbj047k3hvvph780mm330pftvj foreign key (consultation_room_schedule_id) references consultation_room_schedule;
alter table if exists consultation_room_schedule add constraint FKckdi3a3if6bs1fwlshr0riyi1 foreign key (created_by_id) references users;
alter table if exists consultation_room_schedule add constraint FK4b8vbd0lftwmi5gqip5144we9 foreign key (last_modified_by_id) references users;
alter table if exists consultation_room_schedule add constraint FK2e81g5rxre4sc0g1f2riexf4b foreign key (organisation_id) references organisation;
alter table if exists consultation_room_schedule_user add constraint FK7qx0h41vd7puvfjfjek084o8d foreign key (created_by_id) references users;
alter table if exists consultation_room_schedule_user add constraint FK947wtpl5kiwtlhknoe2t1d7wh foreign key (last_modified_by_id) references users;
alter table if exists consultation_room_schedule_user add constraint FKr7cej9rvwl1cxoed66vxj3rga foreign key (organisation_id) references organisation;
alter table if exists consultation_room_schedule_user add constraint FK71gak6cbb40gyylmchvyhpbvr foreign key (consultation_room_schedule_id) references consultation_room_schedule;
alter table if exists consultation_room_schedule_user add constraint FK5fde9ngcjkyhwtjexq80pivjk foreign key (user_id) references users;
alter table if exists consultation_session_record add constraint FKnlovh9f0ua82x0bmexcdwc7qi foreign key (created_by_id) references users;
alter table if exists consultation_session_record add constraint FKlnopw1ijsmgfcxdssbdryy4eg foreign key (last_modified_by_id) references users;
alter table if exists consultation_session_record add constraint FKdpd21xoryns1gp2keq1va8sn2 foreign key (organisation_id) references organisation;
alter table if exists consultation_session_record add constraint FKr53twvc2exo5cn8vwy1x0awg2 foreign key (client_id) references client;
alter table if exists organisation add constraint FKlqxckt96e7yltj1qic8lug6kr foreign key (created_by_id) references users;
alter table if exists organisation add constraint FK95vdsp86x8v3c8ufsgewe82qk foreign key (last_modified_by_id) references users;
alter table if exists organisation_user add constraint FK7eej5l58hijgj0cqv80o03via foreign key (created_by_id) references users;
alter table if exists organisation_user add constraint FKlvtfilq12cx012j82db8nm0r0 foreign key (last_modified_by_id) references users;
alter table if exists organisation_user add constraint FK12a6wyngshovcjge1cvn4d6jn foreign key (organisation_id) references organisation;
alter table if exists organisation_user add constraint FK7cwghb79m4el4t15bvk77k9j0 foreign key (user_id) references users;
alter table if exists password_reset_token add constraint FKl05pe0bq3u2m9dxpyidy4gu0r foreign key (created_by_id) references users;
alter table if exists password_reset_token add constraint FKi44hal8i8afrava3ur4rlcrhv foreign key (last_modified_by_id) references users;
alter table if exists password_reset_token add constraint FK83nsrttkwkb6ym0anu051mtxn foreign key (user_id) references users;
alter table if exists privilege add constraint FK5wowtotsljmlkxmcqbj36sllq foreign key (created_by_id) references users;
alter table if exists privilege add constraint FKixmcc8rsvwydl41qsgktj3ahd foreign key (last_modified_by_id) references users;
alter table if exists role add constraint FK34h8fv9s0c1hg7mkvkjel0gb5 foreign key (created_by_id) references users;
alter table if exists role add constraint FKaavq9o4xwsk4is4qulvif4mpg foreign key (last_modified_by_id) references users;
alter table if exists role_privilege add constraint FKdkwbrwb5r8h74m1v7dqmhp99c foreign key (privilege_id) references privilege;
alter table if exists role_privilege add constraint FKsykrtrdngu5iexmbti7lu9xa foreign key (role_id) references role;
alter table if exists tele_conference add constraint FKqkx1s4un347poba3kshy8djk6 foreign key (created_by_id) references users;
alter table if exists tele_conference add constraint FKjw0id28knyksthnu9h9i9hqnt foreign key (last_modified_by_id) references users;
alter table if exists tele_conference add constraint FK6qhqf0917f4lt2hqlr5xrvoax foreign key (organisation_id) references organisation;
alter table if exists tele_conference add constraint FKu6welns3mi57a2vme3cnsddf foreign key (consultation_room_id) references consultation_room;
alter table if exists users add constraint FK8nakkftyppd62ke6tv7oo5a92 foreign key (created_by_id) references users;
alter table if exists users add constraint FK4yi7iyejvdeanf2jmu8i6dkc8 foreign key (last_modified_by_id) references users;
