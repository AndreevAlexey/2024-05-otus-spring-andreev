package ru.otus.hw.services.comment;

import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<Comment> findById(long id);

    List<Comment> findAll();

    List<Comment> findAllByBookId(long bookId);

    Comment insert(Comment comment);

    Comment update(Comment comment);

    void deleteById(long id);
}
