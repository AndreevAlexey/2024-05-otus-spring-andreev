package ru.otus.hw.services.genre;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Retry(name = "library")
    @CircuitBreaker(name = "library", fallbackMethod = "fallbackGenres")
    @Transactional(readOnly = true)
    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Retry(name = "library")
    @CircuitBreaker(name = "library")
    @Transactional(readOnly = true)
    @Override
    public Optional<Genre> findById(long id) {
        return genreRepository.findById(id);
    }

    private List<Genre> fallbackGenres(Exception e) {
        log.warn(e.getMessage());
        return List.of(new Genre(0, "waiting.."));
    }
}
