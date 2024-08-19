package ru.otus.hw.services;

import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<Book> findById(long id);

    List<Book> findAll();

    Book insert(Book book);

    Book update(Book book);

    void deleteById(long id);

}