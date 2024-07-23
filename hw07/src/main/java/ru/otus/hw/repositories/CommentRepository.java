package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph("book_graph")
    @Override
    Optional<Comment> findById(Long aLong);


    @EntityGraph("book_graph")
    @Override
    List<Comment> findAll();


    List<Comment> findByBookId(long bookId);

}
