package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;


import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }


    @Transactional(readOnly = true)
    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findAllByBookId(long bookId) {
        bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        return commentRepository.findByBookId(bookId);
    }

    @Transactional
    @Override
    public Comment insert(Comment comment) {
        return save(comment);
    }

    @Transactional
    @Override
    public Comment update(Comment comment) {
        findById(comment.getId())
             .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(comment.getId())));

        return save(comment);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Comment save(Comment comment) {
        var bookId = comment.getBook().getId();
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        comment.setBook(book);

        return commentRepository.save(comment);
    }
}
