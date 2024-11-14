package ru.otus.hw.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.CommentsRepositoryCustom;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CommentsRepositoryCustom commentsRepositoryCustom;

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private ObjectMapper mapper;

    List<Comment> comments;

    List<Book> books;

    @BeforeEach
    void setUp() {
        books = getDbBooks();
        comments = getComments();
    }

    @Test
    void shouldReturnCorrectCommentDtoList() {
        // given
        Mockito
                .when(commentsRepositoryCustom.findAll())
                .thenReturn(Flux.just(comments.get(0), comments.get(1), comments.get(2)));
        // then
        webTestClient.get()
                .uri("/api/comment")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[1].id").isEqualTo("2")
                .jsonPath("$.[1].text").isEqualTo("Comment_2_1");
    }

    @Test
    void shouldReturnCorrectCommentDtoById() {
        // given
        long commentId = 2L;

        Mockito
                .when(commentsRepositoryCustom.findById(commentId))
                .thenReturn(Mono.just(comments.get(1)));

        // then
        webTestClient.get()
                .uri("/api/comment/{id}", commentId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("2")
                .jsonPath("$.text").isEqualTo("Comment_2_1");
    }

    @Test
    void shouldReturnCorrectCommentDtoListByBookId() {
        // given
        long bookId = 2L;

        Mockito
                .when(commentsRepositoryCustom.findByBookId(bookId))
                .thenReturn(Flux.just(comments.get(1), comments.get(2)));

        // then
        webTestClient.get()
                .uri("/api/comment/book/{id}", bookId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo("2")
                .jsonPath("$.[0].text").isEqualTo("Comment_2_1");
    }

    @Test
    void commentAddTestShouldReturnCorrectCommentDto() throws Exception {
        // given
        Comment comment = new Comment(777, books.get(0), "new");

        Mockito
                .when(commentsRepositoryCustom.insert(comment))
                .thenReturn(Mono.just(comment));

        // then
        webTestClient.post()
                .uri("/api/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(comment))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("777")
                .jsonPath("$.text").isEqualTo("new");
    }

    @Test
    void commentEditTestShouldReturnCorrectCommentDto() throws Exception {
        // given
        Comment comment = comments.get(1);
        Comment expected = comments.get(2);

        Mockito
                .when(commentsRepositoryCustom.update(comment))
                .thenReturn(Mono.just(expected));

        // then
        webTestClient.put()
                .uri("/api/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(comment))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(expected.getId())
                .jsonPath("$.text").isEqualTo(expected.getText());
    }

    @Test
    void commentDeleteTestShouldReturnOkStatus() {
        // given
        long commentId = 3L;
        Comment comment = comments.get(2);

        Mockito
                .when(commentRepository.findById(commentId))
                .thenReturn(Mono.just(comment));

        Mockito.when(commentRepository.delete(comment)).thenReturn(Mono.empty());

        // then
        webTestClient.delete()
                .uri("/api/comment/{id}", commentId)
                .exchange()
                .expectStatus().isOk();
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
