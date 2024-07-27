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
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }


    @Transactional(readOnly = true)
    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findAllByBookId(String bookId) {
        bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        return commentRepository.findCommentsByBookId(bookId);
    }

    @Transactional
    @Override
    public Comment insert(String bookId, String text) {
        return save(null, bookId, text);
    }

    @Transactional
    @Override
    public Comment update(String id, String bookId, String text) {
        return save(id, bookId, text);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    private Comment save(String id, String bookId, String text) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        var comment = new Comment(id, book, text);
        return commentRepository.save(comment);
    }
}
