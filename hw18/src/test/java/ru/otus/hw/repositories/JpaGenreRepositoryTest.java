package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;
import ru.otus.hw.models.Genre;

@SpringBootTest
public class JpaGenreRepositoryTest {

    public static final long NOT_EXISTING_GENRE_ID = -1;

    @Autowired
    GenreRepository genreRepository;


    @DisplayName("должен возвращать жанр по ид")
    @Test
    void shouldReturnCorrectGenreById() {
        // given
        var expectedGenreId = 1L;
        var expectedGenre = new Genre(1, "Genre_1");
        // when
        StepVerifier
                .create(genreRepository.findById(expectedGenreId))
                .expectNext(expectedGenre)
                .verifyComplete();
    }

    @DisplayName("должен возвращать пустое значение по несуществующему ид")
    @Test
    void shouldReturnEmptyGenreById() {
        // given
        var expectedGenreCount = 0;
        // then
        StepVerifier
                .create(genreRepository.findById(NOT_EXISTING_GENRE_ID))
                .expectNextCount(expectedGenreCount)
                .verifyComplete();
    }

    @DisplayName("должен возвращать список всех жанров")
    @Test
    void shouldReturnAllGenreList() {
        // given
        var expectedGenreCount = 3;
        // then
        StepVerifier
                .create(genreRepository.findAll())
                .expectNextCount(expectedGenreCount)
                .verifyComplete();
    }

}
