package ru.otus.hw.repositories;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;



@Repository
public interface CommentRepository extends ReactiveCrudRepository<Comment, Long> {
}
