package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;


@RepositoryRestResource(path = "comment")
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph("book_graph")
    @Override
    Optional<Comment> findById(Long aLong);


    @EntityGraph("book_graph")
    @Override
    List<Comment> findAll();


    @Query("select c from Comment c where c.book.id = :bookId")
    List<Comment> findByBookId(@Param("bookId") long bookId);

}
