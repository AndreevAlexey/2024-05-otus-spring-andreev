package ru.otus.hw.repositories;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({JdbcAuthorRepository.class})
public class JdbcAuthorRepositoryTest {

    @Autowired
    JdbcAuthorRepository authorRepository;

    List<Author> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
    }

    @DisplayName("должен возвращать автора по ид")
    @ParameterizedTest
    @MethodSource("getDbAuthors")
    void shouldReturnCorrectAuthorById(Author expectedAuthor) {
        // when
        var actualAuthor = authorRepository.findById(expectedAuthor.getId());
        // then
        assertThat(actualAuthor).isPresent()
                .get()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("должен возвращать пустое значение по несуществующему ид")
    @Test
    void shouldReturnEmptyAuthorById() {
        // given
        long id = dbAuthors.size() + 1;
        // when
        var actualAuthor = authorRepository.findById(id);
        // then
        assertThat(actualAuthor).isEmpty();
    }

    @DisplayName("должен возвращать список всех авторов")
    @Test
    void shouldReturnAllAuthorList() {
        // given
        var expectedAuthor = dbAuthors;
        // when
        var actualAuthor = authorRepository.findAll();
        // then
        assertThat(actualAuthor).containsExactlyElementsOf(expectedAuthor);
    }

    public static List<Author> getDbAuthors() {
        return
            IntStream.range(1, 4).boxed()
                    .map(id -> new Author(id, "Author_" + id))
                    .toList();
    }
}
