package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class JpaGenreRepositoryTest {

    public static final long NOT_EXISTING_GENRE_ID = -1;

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    TestEntityManager em;


    @DisplayName("должен возвращать жанр по ид")
    @ParameterizedTest
    @MethodSource("getDbGenresId")
    void shouldReturnCorrectGenreById(long id) {
        // given
        var expectedGenre = em.find(Genre.class, id);
        // when
        var actualGenre = genreRepository.findById(id);
        // then
        assertThat(actualGenre).isPresent()
                .get()
                .isEqualTo(expectedGenre);
    }

    @DisplayName("должен возвращать пустое значение по несуществующему ид")
    @Test
    void shouldReturnEmptyGenreById() {
        // when
        var actualGenre = genreRepository.findById(NOT_EXISTING_GENRE_ID);
        // then
        assertThat(actualGenre).isEmpty();
    }

    @DisplayName("должен возвращать список всех жанров")
    @Test
    void shouldReturnAllGenreList() {
        // given
        List<Genre> expectedGenres = getDbGenres();
        // when
        var actualGenres = genreRepository.findAll();
        // then
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }

    private static List<Long> getDbGenresId() {
        return LongStream.range(1, 4)
                .boxed()
                .toList();
    }

    private List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> em.find(Genre.class, id))
                .toList();
    }
}
