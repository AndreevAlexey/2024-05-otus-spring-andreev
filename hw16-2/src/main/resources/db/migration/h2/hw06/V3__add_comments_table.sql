
create table public.comments (
                                 id bigserial,
                                 text varchar(500),
                                 book_id bigint references books(id) on delete cascade,
                                 primary key (id)
);


insert into comments(book_id, text)
values (1, 'Comment_1_1'),
       (2, 'Comment_2_1'),(2, 'Comment_2_2'),
       (3, 'Comment_3_1'),(3, 'Comment_3_2'),(3, 'Comment_3_3');