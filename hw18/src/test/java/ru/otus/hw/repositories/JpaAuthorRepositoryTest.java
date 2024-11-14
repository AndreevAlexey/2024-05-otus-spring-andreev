package ru.otus.hw.repositories;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;
import ru.otus.hw.models.Author;


@SpringBootTest
public class JpaAuthorRepositoryTest {

    public static final long NOT_EXISTING_AUTHOR_ID = -1;

    @Autowired
    AuthorRepository authorRepository;

    @DisplayName("должен возвращать список всех авторов")
    @Test
    void shouldReturnAllAuthorList() {
        // given
        var expectedAuthorsCount = 3;
        // then
        StepVerifier.create(authorRepository.findAll())
                .expectNextCount(expectedAuthorsCount)
                .verifyComplete();
    }

    @DisplayName("должен возвращать автора по ид")
    @Test
    void shouldReturnCorrectAuthorById() {
        // given
        var expectedAuthorId = 1L;
        var expectedAuthor = new Author(1, "Author_1");
        // when
        StepVerifier
                .create(authorRepository.findById(expectedAuthorId))
                .expectNext(expectedAuthor)
                .verifyComplete();
    }

    @DisplayName("должен возвращать пустое значение по несуществующему ид")
    @Test
    void shouldReturnEmptyAuthorById() {
        // given
        var expectedAuthorsCount = 0;
        // then
        StepVerifier
                .create(authorRepository.findById(NOT_EXISTING_AUTHOR_ID))
                .expectNextCount(expectedAuthorsCount)
                .verifyComplete();
    }

}
