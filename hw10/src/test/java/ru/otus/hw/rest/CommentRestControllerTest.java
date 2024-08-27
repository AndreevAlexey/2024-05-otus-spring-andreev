package ru.otus.hw.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookServiceImpl;
import ru.otus.hw.services.CommentServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentRestController.class)
public class CommentRestControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    CommentServiceImpl commentService;

    @MockBean
    BookServiceImpl bookService;

    List<Comment> comments;

    List<Book> books;


    @BeforeEach
    void setUp() {
        books = getDbBooks();
        comments = getComments();
        given(bookService.findAll()).willReturn(books);
    }

    @Test
    void shouldReturnCorrectCommentDtoList() throws Exception {
        // given
        List<CommentDto> expected =
                comments
                        .stream()
                        .map(CommentDto::toDto)
                        .toList();
        given(commentService.findAll()).willReturn(comments);
        // then
        mvc.perform(get("/api/comment"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    void shouldReturnCorrectCommentDtoById() throws Exception {
        // given
        long commentId = 1L;
        Comment comment = comments.get(0);
        CommentDto expected = CommentDto.toDto(comment);
        given(commentService.findById(commentId)).willReturn(Optional.ofNullable(comment));
        // then
        mvc.perform(get("/api/comment/{id}", commentId))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    void shouldReturnCorrectCommentDtoListByBookId() throws Exception {
        // given
        long bookId = 3L;
        List<Comment> bookComments = comments
                .stream()
                .filter(comment -> comment.getBook().getId() == bookId)
                .toList();
        List<CommentDto> expected =
                bookComments
                        .stream()
                        .map(CommentDto::toDto)
                        .toList();
        given(commentService.findAllByBookId(bookId)).willReturn(bookComments);
        // then
        mvc.perform(get("/api/comment/book/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    void commentEditTestShouldReturnCorrectCommentDto() throws Exception {
        // given
        long commentId = 2L;
        Comment comment = comments.get(1);
        CommentDto expected = CommentDto.toDto(comment);
        given(commentService.update(comment)).willReturn(comment);
        // then
        mvc.perform(put("/api/comment")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    void commentAddTestShouldReturnCorrectCommentDto() throws Exception {
        // given
        Comment comment = new Comment(0, books.get(0), "new");
        CommentDto expected = CommentDto.toDto(comment);
        given(commentService.insert(comment)).willReturn(comment);
        // then
        mvc.perform(post("/api/comment")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    void commentDeleteTestShouldReturnOkStatus() throws Exception {
        // given
        long commentId = 3L;
        doNothing().when(commentService).deleteById(commentId);
        // then
        mvc.perform(delete("/api/comment/{id}", commentId))
                .andExpect(status().isOk());
    }

    private List<Comment> getComments() {
        return List.of(
                new Comment(1, books.get(0), "Comment_1_1"),
                new Comment(2, books.get(1), "Comment_2_1"),
                new Comment(3, books.get(1), "Comment_2_2"),
                new Comment(4, books.get(2), "Comment_3_1"),
                new Comment(5, books.get(2), "Comment_3_2"),
                new Comment(6, books.get(2), "Comment_3_3")
        );
    }

    private List<Book> getDbBooks() {
        Author author = new Author();
        Genre genre = new Genre();
        return List.of(
                new Book(1, "BookTitle_1", author, genre),
                new Book(2, "BookTitle_2", author, genre),
                new Book(3, "BookTitle_3", author, genre)
        );
    }

}
