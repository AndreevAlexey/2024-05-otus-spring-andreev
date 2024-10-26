package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.book.BookServiceImpl;
import ru.otus.hw.services.genre.GenreServiceImpl;
import ru.otus.hw.services.author.AuthorServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(BookRestController.class)
@Import(SecurityConfig.class)
@WithMockUser(
        username = "user",
        authorities = {"ROLE_USER"}
)
public class BookRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookServiceImpl bookService;

    @MockBean
    private AuthorServiceImpl authorService;

    @MockBean
    private GenreServiceImpl genreService;

    private List<Author> authors;

    private List<Genre> genres;

    private List<Book> books;


    @BeforeEach
    void setUp() {
        authors = getDbAuthors();
        genres = getDbGenres();
        books = getDbBooks();
        given(authorService.findAll()).willReturn(authors);
        given(genreService.findAll()).willReturn(genres);
    }

    @Test
    void shouldReturnCorrectBookDtoList() throws Exception {
        // given
        List<BookDto> expected = books
                .stream()
                .map(BookDto::toDto)
                .toList();
        given(bookService.findAll()).willReturn(books);
        // then
        mvc.perform(get("/api/book"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    void shouldReturnCorrectBookDtoByBookId() throws Exception {
        // given
        long bookId = 1L;
        Book book = books.get(0);
        BookDto expected = BookDto.toDto(book);
        given(bookService.findById(bookId)).willReturn(Optional.ofNullable(book));
        // then
        mvc.perform(get("/api/book/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    void bookAddTestShouldReturnCorrectBookDtoOnAdmin() throws Exception {
        // given
        Book newBook = new Book(0, "new", authors.get(1), genres.get(2));
        BookDto expected = BookDto.toDto(newBook);
        given(bookService.insert(newBook)).willReturn(newBook);
        // then
        mvc.perform(post("/api/book")
                        .content(mapper.writeValueAsString(newBook))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    void bookAddTestShouldReturn403OnUser() throws Exception {
        // given
        Book newBook = new Book(0, "new", authors.get(1), genres.get(2));
        given(bookService.insert(newBook)).willReturn(newBook);
        // then
        mvc.perform(post("/api/book")
                        .content(mapper.writeValueAsString(newBook))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    void bookEditTestShouldReturnCorrectBookDtoOnAdmin() throws Exception {
        // given
        long bookId = 1L;
        Book book = books.get(0);
        BookDto expected = BookDto.toDto(book);
        given(bookService.update(book)).willReturn(book);
        // then
        mvc.perform(put("/api/book", bookId)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    void bookEditTestShouldReturn403OnUser() throws Exception {
        // given
        long bookId = 1L;
        Book book = books.get(0);
        BookDto expected = BookDto.toDto(book);
        given(bookService.update(book)).willReturn(book);
        // then
        mvc.perform(put("/api/book", bookId)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().is4xxClientError());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    void bookDeleteTestShouldReturnOkStatusOnAdmin() throws Exception {
        // given
        long bookId = 3L;
        doNothing().when(bookService).deleteById(bookId);
        // then
        mvc.perform(delete("/api/book/{id}", bookId))
                .andExpect(status().isOk());
    }

    @Test
    void bookDeleteTestShouldReturn403StatusOnUser() throws Exception {
        // given
        long bookId = 3L;
        doNothing().when(bookService).deleteById(bookId);
        // then
        mvc.perform(delete("/api/book/{id}", bookId))
                .andExpect(status().is4xxClientError());
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
