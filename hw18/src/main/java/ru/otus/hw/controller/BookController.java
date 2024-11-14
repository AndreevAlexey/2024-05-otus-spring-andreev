package ru.otus.hw.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class BookController {

    @GetMapping("/book")
    public String books() {
        return "book/books";
    }

    @GetMapping("book/add")
    public String add() {
        return "book/book-add";
    }

    @GetMapping("/book/edit/{id}")
    public String edit(@PathVariable("id") long id) {
        return "book/book-edit";
    }

}
