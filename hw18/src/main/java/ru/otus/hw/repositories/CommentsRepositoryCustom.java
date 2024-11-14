package ru.otus.hw.repositories;


import io.r2dbc.spi.Readable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

@Repository
@RequiredArgsConstructor
public class CommentsRepositoryCustom {

    private static final String SQL_ALL = """
            select c1_0.id as id,c1_0.text as comment_text,
            b1_0.id as book_id,b1_0.author_id as author_id,b1_0.genre_id as genre_id,b1_0.title as title  
            from public.comments c1_0 
            left join public.books b1_0 on b1_0.id=c1_0.book_id                     
            """;

    private static final String SQL_BY_ID = """
            select c1_0.id as id,c1_0.text as comment_text,
            b1_0.id as book_id,b1_0.author_id as author_id,b1_0.genre_id as genre_id,b1_0.title as title  
            from public.comments c1_0 
            left join public.books b1_0 on b1_0.id=c1_0.book_id 
            where c1_0.id = :id
            """;

    private static final String SQL_BY_BOOK_ID = """
            select c1_0.id as id,c1_0.text as comment_text,
            b1_0.id as book_id,b1_0.author_id as author_id,b1_0.genre_id as genre_id,b1_0.title as title  
            from public.comments c1_0 
            left join public.books b1_0 on b1_0.id=c1_0.book_id 
            where b1_0.id = :book_id
            """;

    private static final String SQL_INSERT = """
            insert into public.comments (text, book_id) 
            values(:text, :book);
            """;

    private static final String SQL_UPDATE = """
            update public.comments set (text, book_id)=(:text, :book) 
            where id=:id
            """;

    private final R2dbcEntityTemplate template;

    public Flux<Comment> findAll() {
        return
                template.getDatabaseClient().inConnectionMany(connection ->
                        Flux.from(connection.createStatement(SQL_ALL)
                                        .execute())
                                .flatMap(result -> result.map(this::mapper)));
    }

    public Mono<Comment> findById(Long id) {
        return
                Mono.from(template.getDatabaseClient()
                        .sql(SQL_BY_ID)
                        .bind("id", id)
                        .map(this::mapper)
                        .first());
    }

    public Flux<Comment> findByBookId(Long id) {
        return
            Flux.from(template.getDatabaseClient()
                    .sql(SQL_BY_BOOK_ID)
                    .bind("book_id", id)
                    .flatMap(result -> result.map(this::mapper)));
    }

    public Mono<Comment> insert(Comment comment) {
        return
                Mono.from(template.getDatabaseClient()
                        .sql(SQL_INSERT)
                        .bind("text", comment.getText())
                        .bind("book", comment.getBook().getId())
                        .map(this::mapper)
                        .first());
    }

    public Mono<Comment> update(Comment comment) {
        return
                Mono.from(template.getDatabaseClient()
                        .sql(SQL_UPDATE)
                        .bind("text", comment.getText())
                        .bind("book", comment.getBook().getId())
                        .bind("id", comment.getId())
                        .map(this::mapper)
                        .first());
    }

    private Comment mapper(Readable record) {
        Author author = new Author(record.get("author_id", Long.class), null);
        Genre genre = new Genre(record.get("genre_id", Long.class), null);
        Book book = new Book(record.get("book_id", Long.class), record.get("title", String.class), author, genre);
        return
                new Comment(record.get("id", Long.class), book, record.get("comment_text", String.class));
    }
}
