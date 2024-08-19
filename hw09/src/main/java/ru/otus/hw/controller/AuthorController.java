package ru.otus.hw.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.repositories.AuthorRepository;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorRepository authorRepository;

    @GetMapping("authors")
    public String authors(Model model) {
        model.addAttribute("authors", authorRepository.findAll());
        return "author/authors";
    }
}
