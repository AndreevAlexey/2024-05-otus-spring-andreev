package ru.otus.hw.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookServiceImpl;
import ru.otus.hw.services.CommentServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    CommentServiceImpl commentService;

    @MockBean
    BookServiceImpl bookService;

    List<Comment> comments;

    List<Book> books;

    private List<Author> authors;

    private List<Genre> genres;


    @BeforeEach
    void setUp() {
        authors = getDbAuthors();
        genres = getDbGenres();
        books = getDbBooks();
        comments = getComments();
        given(bookService.findAll()).willReturn(books);
    }

    @Test
    void shouldReturnCorrectCommentList() throws Exception {
        // given
        List<Comment> expected = comments;
        given(commentService.findAll()).willReturn(comments);
        // then
        mvc.perform(get("/comments"))
                .andExpect(status().isOk())
                .andExpect(view().name("comment/comments"))
                .andExpect(model().attributeExists("comments"))
                .andExpect(model().attribute("comments", expected));
    }

    @Test
    void shouldReturnCorrectCommentListByBook() throws Exception {
        // given
        long bookId = 3L;
        List<Comment> expected = comments
                                    .stream()
                                    .filter(comment -> comment.getBook().getId() == bookId)
                                    .toList();
        given(commentService.findAllByBookId(bookId)).willReturn(expected);
        // then
        mvc.perform(get("/comments/book/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(view().name("book/book-comments"))
                .andExpect(model().attributeExists("comments"))
                .andExpect(model().attribute("comments", expected));
    }

    @Test
    void commentEditTestShouldReturnCorrectViewAndModel() throws Exception {
        // given
        Comment expected = comments.get(1);
        given(commentService.findById(2L)).willReturn(Optional.ofNullable(expected));
        // then
        mvc.perform(get("/comment/edit/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(view().name("comment/comment-edit"))
                .andExpect(model().attributeExists("comment"))
                .andExpect(model().attribute("comment", expected))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", books));
    }

    @Test
    void commentAddTestShouldReturnCorrectViewAndModel() throws Exception {
        // given
        Comment expected = new Comment();
        // then
        mvc.perform(get("/comment/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("comment/comment-add"))
                .andExpect(model().attributeExists("comment"))
                .andExpect(model().attribute("comment", expected))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", books));
    }

    @Test
    void commentDeleteTestShouldRedirectToComments() throws Exception {
        // given
        doNothing().when(commentService).deleteById(3L);
        // then
        mvc.perform(get("/comment/delete/{id}", 3))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/comments"));
    }

    @Test
    void commentUpdateTestShouldRedirectToComments() throws Exception {
        // given
        Comment expected = comments.get(1);
        given(commentService.update(any())).willReturn(expected);
        mvc.perform(post("/comment/update/{id}", 2)
                        .content("book=2&text=test_update")                )
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/comments"));
    }

    @Test
    void commentInsertTestShouldRedirectToComments() throws Exception {
        // given
        Comment expected = new Comment(0, books.get(0), "test_insert");
        given(commentService.update(any())).willReturn(expected);
        mvc.perform(post("/comment/insert")
                        .content("book=2&text=test_update"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/comments"));
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
        return List.of(
                new Book(1, "BookTitle_1", authors.get(0), genres.get(0)),
                new Book(2, "BookTitle_2", authors.get(1), genres.get(1)),
                new Book(3, "BookTitle_3", authors.get(2), genres.get(2))
        );
    }

    private List<Author> getDbAuthors() {
        return List.of(
                new Author(1, "Author_1"),
                new Author(2, "Author_2"),
                new Author(3, "Author_3"));
    }

    private List<Genre> getDbGenres() {
        return List.of(
                new Genre(1, "Genre_1"),
                new Genre(2, "Genre_2"),
                new Genre(3, "Genre_3")
        );
    }
}
