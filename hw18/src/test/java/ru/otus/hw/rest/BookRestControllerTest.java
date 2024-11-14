package ru.otus.hw.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.BookRepositoryCustom;

import java.util.List;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookRepositoryCustom bookRepositoryCustom;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookRepository bookRepository;

    private List<Author> authors;

    private List<Genre> genres;

    private List<Book> books;

    @BeforeEach
    void setUp() {
        authors = getDbAuthors();
        genres = getDbGenres();
        books = getDbBooks();
    }

    @Test
    void shouldReturnCorrectBookDtoList() {
        // given
        Mockito
                .when(bookRepositoryCustom.findAll())
                .thenReturn(Flux.just(books.get(0), books.get(1), books.get(2)));
        // then
        webTestClient.get()
                .uri("/api/book")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[1].id").isEqualTo("2")
                .jsonPath("$.[1].title").isEqualTo("BookTitle_2");
    }

    @Test
    void shouldReturnCorrectBookDtoByBookId() {
        // given
        long bookId = 1L;

        Mockito
                .when(bookRepositoryCustom.findById(bookId))
                .thenReturn(Mono.just(books.get(0)));

        // then
        webTestClient.get()
                .uri("/api/book/{id}", bookId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.title").isEqualTo("BookTitle_1");
    }

    @Test
    void bookAddTestShouldReturnCorrectBookDto() throws JsonProcessingException {
        // given
        Book newBook = new Book(777, "new", authors.get(1), genres.get(2));

        Mockito
                .when(bookRepositoryCustom.insertBook(newBook))
                .thenReturn(Mono.just(newBook));

        // then
        webTestClient.post()
                .uri("/api/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(newBook))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("777")
                .jsonPath("$.title").isEqualTo("new");
    }

    @Test
    void bookEditTestShouldReturnCorrectBookDto() throws Exception {
        // given
        Book book = books.get(0);
        Book expected = books.get(1);

        Mockito
                .when(bookRepositoryCustom.updateBook(book))
                .thenReturn(Mono.just(expected));

        // then
        webTestClient.put()
                .uri("/api/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(book))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(expected.getId())
                .jsonPath("$.title").isEqualTo(expected.getTitle());
    }

    @Test
    void bookDeleteTestShouldReturnOkStatus() {
        // given
        long bookId = 3L;
        Book book = books.get(2);

        Mockito
                .when(bookRepository.findById(bookId))
                .thenReturn(Mono.just(book));

        Mockito.when(bookRepository.delete(book)).thenReturn(Mono.empty());

        // then
        webTestClient.delete()
                .uri("/api/book/{id}", bookId)
                .exchange()
                .expectStatus().isOk();
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
