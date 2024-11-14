package ru.otus.hw.rest;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.BookRepositoryCustom;

@RestController
@RequiredArgsConstructor
public class BookRestController {

    private final BookRepository bookRepository;

    private final BookRepositoryCustom bookRepositoryCustom;


    @GetMapping("/api/book")
    public Flux<BookDto> getBooks() {
        return bookRepositoryCustom.findAll()
                .map(BookDto::toDto);
    }

    @GetMapping("/api/book/{id}")
    public Mono<ResponseEntity<BookDto>> getBookById(@PathVariable("id") long id) {
        return bookRepositoryCustom.findById(id)
                .map(BookDto::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping("/api/book")
    public Mono<BookDto> addBook(@RequestBody Book book) {
        return bookRepositoryCustom.insertBook(book).map(BookDto::toDto);
    }

    @PutMapping("/api/book")
    public Mono<BookDto> updateBook(@RequestBody Book book) {
        return bookRepositoryCustom.updateBook(book).map(BookDto::toDto);
    }

    @DeleteMapping("/api/book/{id}")
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable long id) {
        return
                bookRepository.findById(id)
                        .flatMap(book -> bookRepository.delete(book)
                                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
                        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
