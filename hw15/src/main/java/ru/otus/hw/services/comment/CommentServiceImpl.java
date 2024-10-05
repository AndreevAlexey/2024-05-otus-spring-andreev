package ru.otus.hw.services.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.services.security.AclServiceWrapperService;


import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final AclServiceWrapperService aclService;

    @Transactional(readOnly = true)
    @Override
    @PostAuthorize("hasPermission(returnObject.orElse(null), 'READ') || hasRole('ADMIN')")
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }


    @Transactional(readOnly = true)
    @Override
    @PostFilter("hasPermission(filterObject, 'READ') || hasRole('ADMIN')")
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }


    @Transactional(readOnly = true)
    @Override
    @PostFilter("hasPermission(filterObject, 'READ') || hasRole('ADMIN')")
    public List<Comment> findAllByBookId(long bookId) {
        bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        return commentRepository.findByBookId(bookId);
    }


    @Transactional
    @Override
    @PreAuthorize("hasPermission(#comment, 'CREATE') || hasRole('ADMIN')")
    public Comment insert(Comment comment) {
        Comment savedComment = save(comment);
        aclService.createFullPermission(savedComment);
        return savedComment;
    }


    @Transactional
    @Override
    @PreAuthorize("hasPermission(#comment, 'WRITE') || hasRole('ADMIN')")
    public Comment update(Comment comment) {
        findById(comment.getId())
             .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(comment.getId())));

        return save(comment);
    }


    @Transactional
    @Override
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.models.Comment', 'DELETE') || hasRole('ADMIN')")
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
