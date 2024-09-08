create table public.app_user (
                                id bigserial,
                                username varchar(255),
                                password varchar(255),
                                primary key (id)
);

create table public.role (
                             id bigserial,
                             code varchar(255),
                             name varchar(255),
                             primary key (id)
);

create table public.user_role (
                                 id bigserial,
                                 user_id bigint references app_user(id) on delete cascade,
                                 role_id bigint references role(id) on delete cascade,
                                 primary key (id)
);


insert into public.app_user(username, password)
values ('user', 'user'),
       ('admin', 'admin'),
       ('guest', '');


insert into public.role(code, name)
values ('USER', 'Role User'),
       ('ADMIN', 'Role Admin');


insert into public.user_role(user_id, role_id)
select u.id, r.id
from public.app_user u, public.role r
where u.username = 'user'
and r.code = 'USER';


insert into public.user_role(user_id, role_id)
select u.id, r.id
from public.app_user u, public.role r
where u.username = 'admin'
  and r.code = 'ADMIN';