package ru.otus.hw.services.book;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Retry(name = "library")
    @CircuitBreaker(name = "library")
    @Transactional(readOnly = true)
    @Override
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    @Retry(name = "library")
    @CircuitBreaker(name = "library", fallbackMethod = "fallbackBooks")
    @Transactional(readOnly = true)
    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Retry(name = "library")
    @CircuitBreaker(name = "library")
    @Transactional
    @Override
    public Book insert(Book book) {
        return save(book);
    }

    @Retry(name = "library")
    @CircuitBreaker(name = "library")
    @Transactional
    @Override
    public Book update(Book book) {
        return save(book);
    }

    @Retry(name = "library")
    @CircuitBreaker(name = "library")
    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(Book book) {
        var authorId = book.getAuthor().getId();
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        book.setAuthor(author);

        var genreId = book.getGenre().getId();
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(genreId)));
        book.setGenre(genre);

        return bookRepository.save(book);
    }

    private List<Book> fallbackBooks(Exception e) {
        log.warn(e.getMessage());
        return List.of(new Book(0, "waiting..", null, null));
    }
}