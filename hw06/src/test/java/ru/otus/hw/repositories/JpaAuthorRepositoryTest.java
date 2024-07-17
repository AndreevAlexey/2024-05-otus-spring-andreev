package ru.otus.hw.repositories;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaAuthorRepository.class})
public class JpaAuthorRepositoryTest {

    public static final long NOT_EXISTING_AUTHOR_ID = -1;

    @Autowired
    JpaAuthorRepository authorRepository;

    @Autowired
    TestEntityManager em;


    @DisplayName("должен возвращать автора по ид")
    @ParameterizedTest
    @MethodSource("getDbAuthorsId")
    void shouldReturnCorrectAuthorById(long id) {
        // given
        var expectedAuthor = em.find(Author.class, id);
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
        var expectedAuthors = getDbAuthors();
        // when
        var actualAuthor = authorRepository.findAll();
        actualAuthor.forEach(System.out::println);
        // then
        assertThat(actualAuthor).containsAll(expectedAuthors);
    }

    public static List<Long> getDbAuthorsId() {
        return
                LongStream.range(1, 4)
                        .boxed()
                        .toList();
    }

    private List<Author> getDbAuthors() {
        return IntStream.range(1, 4)
                .boxed()
                .map(id -> em.find(Author.class, id))
                .toList();
    }
}
