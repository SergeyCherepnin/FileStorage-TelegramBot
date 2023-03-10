create table app_document (
    id int8 generated by default as identity,
    doc_name varchar(255),
    file_size int8,
    mime_type varchar(255),
    telegram_file_id varchar(255),
    binary_content_id int8,
    primary key (id)
);

create table app_photo (
    id int8 generated by default as identity,
    file_size int4,
    telegram_file_id varchar(255),
    binary_content_id int8,
    primary key (id)
);

create table app_user (
    id int8 generated by default as identity,
    email varchar(255),
    first_login_date timestamp,
    first_name varchar(255),
    is_active boolean,
    last_name varchar(255),
    state varchar(255),
    telegram_user_id int8,
    user_name varchar(255),
    primary key (id)
);

create table binary_content (
    id int8 generated by default as identity,
    file_as_array_of_bytes bytea,
    primary key (id)
);

create table raw_data (
    id int8 generated by default as identity,
    event jsonb,
    primary key (id)
);

alter table if exists app_document
    add constraint FKfcm2si6jix496diei6g94rrxm
    foreign key (binary_content_id)
    references binary_content;

alter table if exists app_photo
    add constraint FKsrw4o8i2rpx21b5wsu5yts4uu
    foreign key (binary_content_id)
    references binary_content;
