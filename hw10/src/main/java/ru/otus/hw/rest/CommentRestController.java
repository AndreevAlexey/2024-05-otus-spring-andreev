package ru.otus.hw.rest;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentService commentService;

    @GetMapping("/api/comment")
    public List<CommentDto> getComments() {
        return commentService.findAll()
                .stream()
                .map(CommentDto::toDto)
                .toList();
    }

    @GetMapping("/api/comment/{id}")
    public CommentDto getCommentById(@PathVariable long id) {
        Comment comment = commentService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        return CommentDto.toDto(comment);
    }

    @GetMapping("/api/comment/book/{id}")
    public List<CommentDto> getCommentsByBook(@PathVariable("id") Long bookId) {
        return commentService.findAllByBookId(bookId)
                .stream()
                .map(CommentDto::toDto)
                .toList();
    }

    @PostMapping("/api/comment")
    public CommentDto createComment(@RequestBody Comment comment) {
        return CommentDto.toDto(commentService.insert(comment));
    }

    @PutMapping("/api/comment")
    public CommentDto updateComment(@RequestBody Comment comment) {
        return CommentDto.toDto(commentService.update(comment));
    }

    @DeleteMapping("/api/comment/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteById(id);
    }
}
