package ru.otus.hw13.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw13.models.Author;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Override
    List<Author> findAll();

}
