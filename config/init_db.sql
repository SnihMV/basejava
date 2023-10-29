create table resume
(
    uuid      char(36) primary key,
    full_name text not null
);

create table contact
(
    id          serial,
    type        text     not null,
    value       text     not null,
    resume_uuid char(36) not null references resume (uuid) on delete cascade
);

create unique index contact_uuid_type_index on contact (resume_uuid, type);

