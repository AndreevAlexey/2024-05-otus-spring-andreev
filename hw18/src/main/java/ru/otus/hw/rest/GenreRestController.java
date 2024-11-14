package ru.otus.hw.rest;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;

@RestController
@RequiredArgsConstructor
public class GenreRestController {

    private final GenreRepository genreRepository;


    @GetMapping("/api/genre")
    public Flux<GenreDto> getGenres() {
        return genreRepository.findAll()
                .map(GenreDto::toDto);
    }
}
