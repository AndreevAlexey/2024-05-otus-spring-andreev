package ru.otus.hw.rest;


import lombok.RequiredArgsConstructor;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

    @GetMapping("/api/book")
    public List<BookDto> getBooks() {
        return bookService.findAll()
                .stream()
                .map(BookDto::toDto)
                .toList();
    }

    @GetMapping("/api/book/{id}")
    public BookDto getBooks(@PathVariable("id") long id) {
        Book book =  bookService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        return BookDto.toDto(book);
    }

    @PostMapping("/api/book")
    public BookDto addBook(@RequestBody Book book) {
        return BookDto.toDto(bookService.insert(book));
    }

    @PutMapping("/api/book")
    public BookDto updateBook(@RequestBody Book book) {
        return BookDto.toDto(bookService.update(book));
    }

    @DeleteMapping("/api/book/{id}")
    public void deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
    }
}
