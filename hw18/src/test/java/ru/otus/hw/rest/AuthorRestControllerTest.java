package ru.otus.hw.rest;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthorRepository authorRepository;

    private final List<Author> authors = getDbAuthors();

    @Test
    void getAllAuthorsTestShouldReturnCorrectAuthorDtoList() {
        // given
        Mockito
                .when(authorRepository.findAll())
                .thenReturn(Flux.just(authors.get(0), authors.get(1), authors.get(2)));
        // then
        webTestClient.get()
                .uri("/api/author")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[1].id").isEqualTo("2")
                .jsonPath("$.[1].fullName").isEqualTo("Author_2");
    }

    private List<Author> getDbAuthors() {
        return List.of(
                new Author(1, "Author_1"),
                new Author(2, "Author_2"),
                new Author(3, "Author_3"));
    }
}
