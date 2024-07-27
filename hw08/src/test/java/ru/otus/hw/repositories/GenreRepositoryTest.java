package ru.otus.hw.repositories;

import de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class GenreRepositoryTest {

    public static final String NOT_EXISTING_GENRE_ID = "-1";

    @Autowired
    GenreRepository genreRepository;

    private List<Genre> genres;

    @BeforeEach
    void setUp() {
        genres = getDbGenres();
    }

    @DisplayName("должен возвращать жанр по ид")
    @ParameterizedTest
    @MethodSource("getDbGenresId")
    void shouldReturnCorrectGenreById(String id) {
        // given
        var expectedGenre = genres.stream()
                .filter(genre -> genre.getId().equals(id))
                .findFirst()
                .orElse(null);
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
        List<Genre> expectedGenres = genres;
        // when
        var actualGenres = genreRepository.findAll();
        // then
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }

    private static List<String> getDbGenresId() {
        return List.of("1", "2", "3");
    }

    private List<Genre> getDbGenres() {
        return List.of(
                new Genre("1", "Genre_1"),
                new Genre("2", "Genre_2"),
                new Genre("3", "Genre_3")
        );
    }

}
