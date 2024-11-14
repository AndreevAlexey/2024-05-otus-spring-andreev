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
import reactor.test.StepVerifier;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;

@Testcontainers
@DataR2dbcTest
@Import(CommentsRepositoryCustom.class)
public class JpaCommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentsRepositoryCustom commentsRepositoryCustom;

    List<Book> books = getDbBooks();

    List<Comment> comments = getComments();


    @Container
    static PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:13")
            .withDatabaseName("library-test")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", JpaCommentRepositoryTest::r2dbcUrl);
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

    @DisplayName("должен возвращать комментарий по ид")
    @Test
    void shouldReturnCorrectCommentById() {
        // given
        var commentId = 1L;
        var expectedComment = comments.get(0);
        // then
        StepVerifier
                .create(commentsRepositoryCustom.findById(commentId))
                .expectNext(expectedComment)
                .verifyComplete();
    }

    @DisplayName("должен загружать список всех комментариев")
    @Test
    void shouldReturnCorrectCommentList() {
        // given
        var expectedCommentsCount = comments.size();
        // then
        StepVerifier.create(commentsRepositoryCustom.findAll())
                .expectNextCount(expectedCommentsCount)
                .verifyComplete();
    }

    @DisplayName("должен возвращать список всех комментариев по ид книги")
    @Test
    void shouldReturnAllCommentsByBookId() {
        // given
        var bookId = 2L;
        var expectedCommentsCount = 2;
        // then
        StepVerifier.create(commentsRepositoryCustom.findByBookId(bookId))
                .expectNextCount(expectedCommentsCount)
                .verifyComplete();
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        // given
        var expectedComment = new Comment(777, books.get(2), "Save comment test");
        // then
        StepVerifier.create(commentsRepositoryCustom.insert(expectedComment))
                .verifyComplete();
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        // given
        long commentId = 1L;
        var expectedComment = new Comment(commentId, books.get(0), "Update comment test");
        // then
        StepVerifier.create(commentsRepositoryCustom.update(expectedComment))
                .verifyComplete();
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteByIdComment() {
        StepVerifier.create(commentRepository.delete(comments.get(5)))
                .verifyComplete();
    }

    private List<Comment> getComments() {
        return List.of(
                new Comment(1, books.get(0), "Comment_1_1"),
                new Comment(2, books.get(1), "Comment_2_1"),
                new Comment(3, books.get(1), "Comment_2_2"),
                new Comment(4, books.get(2), "Comment_3_1"),
                new Comment(5, books.get(2), "Comment_3_2"),
                new Comment(6, books.get(2), "Comment_3_3")
        );
    }

    private List<Book> getDbBooks() {
        Author author = new Author();
        Genre genre = new Genre();
        return List.of(
                new Book(1, "BookTitle_1", author, genre),
                new Book(2, "BookTitle_2", author, genre),
                new Book(3, "BookTitle_3", author, genre)
        );
    }
}
