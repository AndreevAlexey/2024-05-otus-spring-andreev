package ru.otus.hw.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GenreController {

    @GetMapping("/genre")
    public String genres() {
        return "genre/genres";
    }
}
