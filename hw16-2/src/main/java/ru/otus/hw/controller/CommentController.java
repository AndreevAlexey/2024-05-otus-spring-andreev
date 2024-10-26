package ru.otus.hw.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class CommentController {

    @GetMapping("/comment")
    public String comments() {
        return "comment/comments";
    }

    @GetMapping("/comment/book/{id}")
    public String commentsByBookId(@PathVariable("id") long bookId) {
        return "comment/comments-book";
    }

    @GetMapping("/comment/edit/{id}")
    public String edit(@PathVariable("id") long id) {
        return "comment/comment-edit";
    }

    @GetMapping("/comment/add")
    public String add() {
        return "comment/comment-add";
    }

}
