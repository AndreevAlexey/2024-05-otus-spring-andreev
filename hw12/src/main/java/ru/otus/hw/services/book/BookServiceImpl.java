package ru.otus.hw.services.book;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.security.AclServiceWrapperService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final AclServiceWrapperService aclService;

    @Transactional(readOnly = true)
    @Override
    @PostAuthorize("hasPermission(returnObject.orElse(null), 'READ') || hasRole('ADMIN')")
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }


    @Transactional(readOnly = true)
    @Override
    @PostFilter("hasPermission(filterObject, 'READ') || hasRole('ADMIN')")
    public List<Book> findAll() {
        return bookRepository.findAll();
    }


    @Transactional
    @Override
    @PreAuthorize("hasPermission(#book, 'CREATE') || hasRole('ADMIN')")
    public Book insert(Book book) {
        Book newBook = save(book);
        aclService.createFullPermission(newBook);
        return newBook;
    }


    @Transactional
    @Override
    @PreAuthorize("hasPermission(#book, 'WRITE') || hasRole('ADMIN')")
    public Book update(Book book) {
        return save(book);
    }


    @Transactional
    @Override
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.models.Book', 'DELETE') || hasRole('ADMIN')")
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

}