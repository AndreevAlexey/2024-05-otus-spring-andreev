package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({JdbcGenreRepository.class})
public class JdbcGenreRepositoryTest {

    @Autowired
    JdbcGenreRepository genreRepository;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = getDbGenres();
    }

    @DisplayName("должен возвращать жанр по ид")
    @ParameterizedTest
    @MethodSource("getDbGenres")
    void shouldReturnCorrectGenreById(Genre expectedGenre) {
        // when
        var actualGenre = genreRepository.findById(expectedGenre.getId());
        // then
        assertThat(actualGenre).isPresent()
                .get()
                .isEqualTo(expectedGenre);
    }

    @DisplayName("должен возвращать пустое значение по несуществующему ид")
    @Test
    void shouldReturnEmptyGenreById() {
        // given
        long id = dbGenres.size() + 1;
        // when
        var actualGenre = genreRepository.findById(id);
        // then
        assertThat(actualGenre).isEmpty();
    }

    @DisplayName("должен возвращать список всех жанров")
    @Test
    void shouldReturnAllGenreList() {
        // given
        var expectedGenres = dbGenres;
        // when
        var actualGenres = genreRepository.findAll();
        // then
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }


    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }
}
