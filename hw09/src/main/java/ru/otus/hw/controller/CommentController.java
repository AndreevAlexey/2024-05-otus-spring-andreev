package ru.otus.hw.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private final BookService bookService;

    @GetMapping("/comments")
    public String comments(Model model) {
        List<Comment> comments = commentService.findAll();
        model.addAttribute("comments", comments);
        return "comment/comments";
    }

    @GetMapping("/comments/book/{id}")
    public String commentsByBookId(@PathVariable("id") long bookId, Model model) {
        List<Comment> comments = commentService.findAllByBookId(bookId);
        model.addAttribute("comments", comments);
        return "book/book-comments";
    }

    @GetMapping("/comment/edit/{id}")
    public String edit(@PathVariable("id") long id, Model model) {
        Comment comment = commentService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        model.addAttribute("comment", comment);

        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);

        return "comment/comment-edit";
    }

    @GetMapping("/comment/add")
    public String add(Model model) {
        Comment comment = new Comment();
        model.addAttribute("comment", comment);

        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);

        return "comment/comment-add";
    }

    @PostMapping("/comment/update/{id}")
    public String update(Comment comment) {
        commentService.update(comment);
        return "redirect:/comments";
    }

    @PostMapping("/comment/insert")
    public String insert(Comment comment) {
        commentService.insert(comment);
        return "redirect:/comments";
    }

    @GetMapping("/comment/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        commentService.deleteById(id);
        return "redirect:/comments";
    }
}
