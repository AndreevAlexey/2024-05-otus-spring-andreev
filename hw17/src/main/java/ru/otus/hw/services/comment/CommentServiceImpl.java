package ru.otus.hw.services.comment;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Retry(name = "library")
    @CircuitBreaker(name = "library")
    @Transactional(readOnly = true)
    @Override
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    @Retry(name = "library")
    @CircuitBreaker(name = "library", fallbackMethod = "fallbackComments")
    @Transactional(readOnly = true)
    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Retry(name = "library")
    @CircuitBreaker(name = "library", fallbackMethod = "fallbackComments")
    @Transactional(readOnly = true)
    @Override
    public List<Comment> findAllByBookId(long bookId) {
        bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        return commentRepository.findByBookId(bookId);
    }

    @Retry(name = "library")
    @CircuitBreaker(name = "library")
    @Transactional
    @Override
    public Comment insert(Comment comment) {
        return save(comment);
    }

    @Retry(name = "library")
    @CircuitBreaker(name = "library")
    @Transactional
    @Override
    public Comment update(Comment comment) {
        findById(comment.getId())
             .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(comment.getId())));

        return save(comment);
    }

    @Retry(name = "library")
    @CircuitBreaker(name = "library")
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

    private List<Comment> fallbackComments(Exception e) {
        log.warn(e.getMessage());
        return List.of(new Comment(0, null, "waiting.."));
    }
}
