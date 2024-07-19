package ru.otus.hw.shell;


import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;


    @ShellMethod(value = "Find all comments", key = "ac")
    public String findAll() {
        return commentService.findAll().stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findById(long id) {
        return commentService.findById(id)
                .map(commentConverter::commentToString)
                .orElse("Comment with id %d not found".formatted(id));
    }

    @ShellMethod(value = "Find book comments by bookId", key = "cbbid")
    public String findAllByBookId(long bookId) {
        return commentService.findAllByBookId(bookId).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Insert comment", key = "cins")
    public String insertComment(long bookId, String text) {
        var comment = commentService.insert(bookId, text);
        return commentConverter.commentToString(comment);
    }

    @ShellMethod(value = "Update comment", key = "cupd")
    public String updateComment(long id, long bookId, String text) {
        var comment = commentService.update(id, bookId, text);
        return commentConverter.commentToString(comment);
    }

    @ShellMethod(value = "Delete comment by id", key = "cdel")
    public void deleteComment(long id) {
        commentService.deleteById(id);
    }
}
