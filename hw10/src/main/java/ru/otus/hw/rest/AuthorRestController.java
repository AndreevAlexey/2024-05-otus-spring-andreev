package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorServiceImpl;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class AuthorRestController {

    private final AuthorServiceImpl authorService;

    @GetMapping("/api/author")
    public List<AuthorDto> getAuthors() {
        return authorService.findAll()
                .stream()
                .map(AuthorDto::toDto)
                .toList();
    }
}
