package ru.otus.hw.repositories;


import io.r2dbc.spi.Readable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

@Repository
@RequiredArgsConstructor
public class BookRepositoryCustom {

    private static final String SQL_ALL = """
            select b1_0.id as id,b1_0.title as title,
               a1_0.id as author_id,a1_0.full_name as author_name,
               g1_0.id as genre_id,g1_0.name genre_name 
            from public.books b1_0 
            left join public.authors a1_0 on a1_0.id=b1_0.author_id 
            left join public.genres g1_0 on g1_0.id=b1_0.genre_id                     
            """;

    private static final String SQL_BY_ID = """
            select b1_0.id as id,b1_0.title as title,
               a1_0.id as author_id,a1_0.full_name as author_name,
               g1_0.id as genre_id,g1_0.name genre_name 
            from public.books b1_0 
            left join public.authors a1_0 on a1_0.id=b1_0.author_id 
            left join public.genres g1_0 on g1_0.id=b1_0.genre_id
            where b1_0.id = :id
            """;

    private static final String SQL_INSERT = """
            insert into public.books (title, author_id, genre_id) 
            values(:title, :author, :genre);
            """;

    private static final String SQL_UPDATE = """
            update public.books set (title, author_id, genre_id)=(:title, :author, :genre) 
            where id = :id
            """;

    private final R2dbcEntityTemplate template;

    public Flux<Book> findAll() {
        return
                template.getDatabaseClient().inConnectionMany(connection ->
                        Flux.from(connection.createStatement(SQL_ALL)
                                .execute())
                                .flatMap(result -> result.map(this::mapper)));
    }

    public Mono<Book> findById(Long id) {
        return
                Mono.from(template.getDatabaseClient()
                        .sql(SQL_BY_ID)
                        .bind("id", id)
                        .map(this::mapper)
                        .first());
    }

    public Mono<Book> insertBook(Book book) {
        return
                Mono.from(template.getDatabaseClient()
                        .sql(SQL_INSERT)
                        .bind("title", book.getTitle())
                        .bind("author", book.getAuthor().getId())
                        .bind("genre", book.getGenre().getId())
                        .map(this::mapper)
                        .first());
    }

    public Mono<Book> updateBook(Book book) {
        return
                Mono.from(template.getDatabaseClient()
                        .sql(SQL_UPDATE)
                        .bind("title", book.getTitle())
                        .bind("author", book.getAuthor().getId())
                        .bind("genre", book.getGenre().getId())
                        .bind("id", book.getId())
                        .map(this::mapper)
                        .first());
    }


    private Book mapper(Readable record) {
        Author author = new Author(record.get("author_id", Long.class), record.get("author_name", String.class));
        Genre genre = new Genre(record.get("genre_id", Long.class), record.get("genre_name", String.class));
        return
                new Book(record.get("id", Long.class), record.get("title", String.class), author, genre);
    }
}
