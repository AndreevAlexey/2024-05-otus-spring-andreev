package ru.otus.hw.repositories;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.hw.models.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataMongoTest
@EnableConfigurationProperties
public class AuthorRepositoryTest {

    public static final String NOT_EXISTING_AUTHOR_ID = "-1";

    @Autowired
    AuthorRepository authorRepository;

    private List<Author> authors;

    @BeforeEach
    public void setUp() {
        authors = getDbAuthors();
    }

    @DisplayName("должен возвращать автора по ид")
    @ParameterizedTest
    @MethodSource("getDbAuthorsId")
    void shouldReturnCorrectAuthorById(String id) {
        // given
        var expectedAuthor = authors.stream()
                .filter(author -> author.getId().equals(id))
                .findFirst()
                .orElse(null);
        // when
        var actualAuthor = authorRepository.findById(id);
        // then
        assertThat(actualAuthor).isPresent()
                .get()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("должен возвращать пустое значение по несуществующему ид")
    @Test
    void shouldReturnEmptyAuthorById() {
        // when
        var actualAuthor = authorRepository.findById(NOT_EXISTING_AUTHOR_ID);
        // then
        assertThat(actualAuthor).isEmpty();
    }

    @DisplayName("должен возвращать список всех авторов")
    @Test
    void shouldReturnAllAuthorList() {
        // given
        var expectedAuthors = authors;
        // when
        var actualAuthor = authorRepository.findAll();
        // then
        assertThat(actualAuthor).containsAll(expectedAuthors);
    }

    public static List<String> getDbAuthorsId() {
        return List.of("1", "2", "3");
    }

    private List<Author> getDbAuthors() {
        return List.of(
                new Author("1", "Author_1"),
                new Author("2", "Author_2"),
                new Author("3", "Author_3"));
    }

}
