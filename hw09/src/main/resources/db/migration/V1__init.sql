create table public.authors (
                                id bigserial,
                                full_name varchar(255),
                                primary key (id)
);

create table public.genres (
                               id bigserial,
                               name varchar(255),
                               primary key (id)
);

create table public.books (
                              id bigserial,
                              title varchar(255),
                              author_id bigint references authors (id) on delete cascade,
                              genre_id bigint references genres(id) on delete cascade,
                              primary key (id)
);