package ru.otus.hw.security;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import ru.otus.hw.Application;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.author.AuthorServiceImpl;
import ru.otus.hw.services.book.BookServiceImpl;
import ru.otus.hw.services.comment.CommentServiceImpl;
import ru.otus.hw.services.genre.GenreServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = Application.class)
public class ApplicationSecurityTest {


    @Autowired
    private AuthorServiceImpl authorService;

    @Autowired
    private GenreServiceImpl genreService;

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private CommentServiceImpl commentService;

    private List<Author> authors;

    private List<Genre> genres;

    private List<Book> books;

    private List<Comment> comments;

    @BeforeEach
    void setUp() {
        authors = getDbAuthors();
        genres = getDbGenres();
        books = getDbBooks();
        comments = getDbComments();
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @DisplayName("должен выдавать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorListUser() {
        // given
        var expectedAuthors = authors;
        // when
        var actualAuthors = authorService.findAll();
        // then
        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("должен выдавать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorListAdmin() {
        // given
        var expectedAuthors = authors;
        // when
        var actualAuthors = authorService.findAll();
        // then
        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_NOT_EXIST"}
    )
    @DisplayName("должен выдавать пустой список авторов для несуществующей роли")
    @Test
    void shouldReturnEmptyAuthorList() {
        // when
        var actualAuthors = authorService.findAll();
        // then
        assertThat(actualAuthors).isEmpty();
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @DisplayName("должен выдавать список всех жанров")
    @Test
    void shouldReturnCorrectGenreListUser() {
        // given
        var expectedGenres = genres;
        // when
        var actualGenres = genreService.findAll();
        // then
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("должен выдавать список всех жанров")
    @Test
    void shouldReturnCorrectGenreListAdmin() {
        // given
        var expectedGenre = genres;
        // when
        var actualGenres = genreService.findAll();
        // then
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenre);
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_NOT_EXIST"}
    )
    @DisplayName("должен выдавать пустой список жанров для несуществующей роли")
    @Test
    void shouldReturnEmptyGenreList() {
        // when
        var actualGenres = genreService.findAll();
        // then
        assertThat(actualGenres).isEmpty();
    }


    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @DisplayName("должен выдавать список всех книг")
    @Test
    void shouldReturnCorrectBookListUser() {
        // given
        var expectedBooks = books;
        // when
        var actualBooks = bookService.findAll();
        // then
        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("должен выдавать список всех книг")
    @Test
    void shouldReturnCorrectBookListAdmin() {
        // given
        var expectedBooks = books;
        // when
        var actualBooks = bookService.findAll();
        // then
        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_NOT_EXIST"}
    )
    @DisplayName("должен выдавать пустой список книг для несуществующей роли")
    @Test
    void shouldReturnEmptyBookList() {
        // when
        var actualBooks = bookService.findAll();
        // then
        assertThat(actualBooks).isEmpty();
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("должен сохранять новую книгу")
    @Test
    void addBookShouldReturnCorrectBookOnAdmin() {
        // given
        var newBook = new Book(0, "test", new Author(1L, ""), new Genre(1L, ""));
        // when
        var returnedBook = bookService.insert(newBook);
        // then
        assertThat(returnedBook).isNotNull();
    }

    @DisplayName("должен выбрасывать exception на добавление книги")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void bookAddTestShouldThrowExceptionOnUser() {
        // given
        var newBook = new Book(0, "test", new Author(1L, ""), new Genre(1L, ""));
        // then
        Assertions.assertThrows(AccessDeniedException.class, () -> bookService.insert(newBook));
    }

    @DisplayName("должен обновлять книгу")
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    void bookEditTestShouldReturnCorrectBookOnAdmin() throws Exception {
        // given
        Book expected = books.get(0);
        expected.setTitle("Updated Title");
        // when
        Book actual = bookService.update(expected);
        // then
        assertThat(expected).isEqualTo(actual);
    }

    @DisplayName("должен выбрасывать exception на обновление книги")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void bookEditTestShouldThrowExceptionOnUser() throws Exception {
        // given
        Book book = books.get(0);
        // then
        Assertions.assertThrows(AccessDeniedException.class, () -> bookService.update(book));
    }

    @DisplayName("должен удалять книгу по id ")
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    void shouldDeleteBook() {
        // given
        long bookId = 4L;
        // when
        bookService.deleteById(bookId);
        // then
        assertThat(bookService.findById(bookId).orElse(null)).isNull();
    }

    @DisplayName("должен выбрасывать exception на удаление книги")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void bookDeleteTestShouldThrowExceptionOnUser() throws Exception {
        // given
        long bookId = 4L;
        // then
        Assertions.assertThrows(AccessDeniedException.class, () -> bookService.deleteById(bookId));
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @DisplayName("должен выдавать список всех комментариев")
    @Test
    void shouldReturnCorrectCommentListUser() {
        // given
        var expectedComments = comments;
        // when
        var actualComments = commentService.findAll();
        // then
        assertThat(expectedComments).containsExactlyElementsOf(actualComments);
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("должен выдавать список всех комментариев")
    @Test
    void shouldReturnCorrectCommentListAdmin() {
        // given
        var expectedComments = comments;
        // when
        var actualComments = commentService.findAll();
        // then
        assertThat(expectedComments).containsExactlyElementsOf(actualComments);
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_NOT_EXIST"}
    )
    @DisplayName("должен выдавать пустой список комментариев для несуществующей роли")
    @Test
    void shouldReturnEmptyCommentList() {
        // when
        var actualComments = commentService.findAll();
        // then
        assertThat(actualComments).isEmpty();
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("должен сохранять новый комментарий")
    @Test
    void addCommentShouldReturnCorrectCommentWithOwnerPermissionsOnAdmin() {
        // given
        var newComment = new Comment(0, books.get(0), "Save admin comment test");
        var cntBefore =  commentService.findAll().size();
        // when
        var returnedComment = commentService.insert(newComment);
        // then
        assertThat(returnedComment).isNotNull();
        assertThat(commentService.findAll()).contains(returnedComment);
        assertThat(commentService.update(returnedComment)).isNotNull();
        commentService.deleteById(returnedComment.getId());
        assertThat(commentService.findAll().size()).isEqualTo(cntBefore);
    }

    @DisplayName("должен сохранять новый комментарий")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void addCommentShouldReturnCorrectCommentWithOwnerPermissionsOnUser() {
        // given
        var newComment = new Comment(0, books.get(1), "Save user comment test");
        var cntBefore =  commentService.findAll().size();
        // when
        var returnedComment = commentService.insert(newComment);
        // then
        assertThat(returnedComment).isNotNull();
        assertThat(commentService.findAll()).contains(returnedComment);
        assertThat(commentService.update(returnedComment)).isNotNull();
        commentService.deleteById(returnedComment.getId());
        assertThat(commentService.findAll().size()).isEqualTo(cntBefore);
    }

    @DisplayName("должен обновлять комментарий")
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    void CommentEditTestShouldReturnCorrectBookOnAdmin() {
        // given
        Comment expected = comments.get(0);
        expected.setText("Updated text");
        // when
        Comment actual = commentService.update(expected);
        // then
        assertThat(expected).isEqualTo(actual);
    }

    @DisplayName("должен выбрасывать exception на обновление комменнтария")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void CommentEditTestShouldThrowExceptionOnUser() {
        // given
        Comment expected = comments.get(0);
        // then
        Assertions.assertThrows(AccessDeniedException.class, () -> commentService.update(expected));
    }

    @DisplayName("должен удалять комментарий по id ")
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    void shouldDeleteComment() {
        // given
        long commentId = 7L;
        // when
        commentService.deleteById(commentId);
        // then
        assertThat(bookService.findById(commentId).orElse(null)).isNull();
    }

    @DisplayName("должен выбрасывать exception на удаление комменнтария")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void commentDeleteTestShouldThrowExceptionOnUser() {
        // given
        long commentId = 7L;
        // then
        Assertions.assertThrows(AccessDeniedException.class, () -> commentService.deleteById(commentId));
    }


    private List<Comment> getDbComments() {
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
