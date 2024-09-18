package ru.otus.hw13.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw13.models.Genre;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Override
    List<Genre> findAll();

}
