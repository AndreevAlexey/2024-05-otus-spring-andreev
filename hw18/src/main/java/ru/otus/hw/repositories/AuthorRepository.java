package ru.otus.hw.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Author;


@Repository
public interface AuthorRepository extends ReactiveCrudRepository<Author, Long> {

    @Override
    Flux<Author> findAll();
}
