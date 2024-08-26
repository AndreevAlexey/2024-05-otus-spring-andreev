package ru.otus.hw.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/books")
    public String books(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "book/books";
    }

    @GetMapping("book/add")
    public String add(Model model) {
        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);

        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);

        Book book = new Book();
        model.addAttribute("book", book);

        return "book/book-add";
    }

    @GetMapping("/book/edit/{id}")
    public String edit(@PathVariable("id") long id, Model model) {
        Book book = bookService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        model.addAttribute("book", book);

        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);

        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);

        return "book/book-edit";
    }

    @GetMapping("/book/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }

    @PostMapping("/book/update/{id}")
    public String update(Book book) {
        bookService.update(book);
        return "redirect:/books";
    }

    @PostMapping("/book/insert")
    public String insert(Book book) {
        bookService.insert(book);
        return "redirect:/books";
    }

}
