package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;


@RepositoryRestResource(path = "book")
public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph("author_genre_graph")
    @Override
    List<Book> findAll();

    @EntityGraph("author_genre_graph")
    @Override
    Optional<Book> findById(Long aLong);
}
