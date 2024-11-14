package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;


@Testcontainers
@DataR2dbcTest
@Import(BookRepositoryCustom.class)
class JpaBookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookRepositoryCustom bookRepositoryCustom;

    private List<Author> authors = getDbAuthors();

    private List<Genre> genres = getDbGenres();

    private List<Book> books = getDbBooks();

    @Container
    static PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:13")
            .withDatabaseName("library-test")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", JpaBookRepositoryTest::r2dbcUrl);
        registry.add("spring.r2dbc.username", POSTGRES::getUsername);
        registry.add("spring.r2dbc.password", POSTGRES::getPassword);

        registry.add("spring.flyway.url", POSTGRES::getJdbcUrl);
        registry.add("spring.flyway.username", POSTGRES::getUsername);
        registry.add("spring.flyway.password", POSTGRES::getPassword);
    }

    private static String r2dbcUrl() {
        return
                String.format("r2dbc:postgresql://%s:%s/%s",
                        POSTGRES.getHost(),
                        POSTGRES.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                        POSTGRES.getDatabaseName());
    }

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        // given
        var expectedBookId = 1L;
        var expectedBook = new Book(1, "BookTitle_1",
                new Author(1, null), new Genre(1, null));
        // when
        StepVerifier
                .create(bookRepositoryCustom.findById(expectedBookId))
                .expectNext(expectedBook)
                .verifyComplete();
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        // given
        var expectedBooksCount = books.size();
        // then
        StepVerifier.create(bookRepositoryCustom.findAll())
                .expectNextCount(expectedBooksCount)
                .verifyComplete();
    }


    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        // given
        var author = new Author(1, "Author_1");
        var genre = new Genre(1, "Genre_1");
        var expectedBook = new Book(777, "BookTitle_NEW", author, genre);
        Mono<Book> bookNew = bookRepositoryCustom.insertBook(expectedBook);
        // then
        StepVerifier.create(bookNew)
                .verifyComplete();
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        // given
        long bookId = 3L;
        var expectedBook = new Book(bookId, "BookTitle_10500", authors.get(2), genres.get(2));
        // then
        StepVerifier.create(bookRepositoryCustom.updateBook(expectedBook))
                .verifyComplete();
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        StepVerifier.create(bookRepository.delete(books.get(2)))
                .verifyComplete();
    }

    private List<Book> getDbBooks() {
        return List.of(
                new Book(1, "BookTitle_1", authors.get(0), genres.get(0)),
                new Book(2, "BookTitle_2", authors.get(1), genres.get(1)),
                new Book(3, "BookTitle_3", authors.get(2), genres.get(2))
        );
    }

    private List<Author> getDbAuthors() {
        return List.of(
                new Author(1, "Author_1"),
                new Author(2, "Author_2"),
                new Author(3, "Author_3"));
    }

    private List<Genre> getDbGenres() {
        return List.of(
                new Genre(1, "Genre_1"),
                new Genre(2, "Genre_2"),
                new Genre(3, "Genre_3")
        );
    }
}