package ru.otus.hw.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorServiceImpl;
import ru.otus.hw.services.BookServiceImpl;
import ru.otus.hw.services.GenreServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

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
    void shouldReturnCorrectBookList() throws Exception {
        // given
        List<Book> expected = books;
        given(bookService.findAll()).willReturn(books);
        // then
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/books"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", expected));
    }

    @Test
    void bookAddTestShouldReturnCorrectViewAndModel() throws Exception {
        // given
        Book expected = new Book();
        // then
        mvc.perform(get("/book/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/book-add"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attribute("authors", authors))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("genres", genres))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("book", expected));
    }

    @Test
    void bookEditTestShouldReturnCorrectViewAndModel() throws Exception {
        // given
        Book expected = books.get(1);
        given(bookService.findById(2L)).willReturn(Optional.ofNullable(expected));
        // then
        mvc.perform(get("/book/edit/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(view().name("book/book-edit"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attribute("authors", authors))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("genres", genres))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("book", expected));
    }

    @Test
    void bookDeleteTestShouldRedirectToBooks() throws Exception {
        // given
        doNothing().when(bookService).deleteById(3L);
        // then
        mvc.perform(get("/book/delete/{id}", 3))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/books"));
    }

    /*@Test
    void bookUpdateTestShouldRedirectToBooks() throws Exception {
        // given
        Book expected = books.get(1);
//        given(bookService.update(2, "BookTitle_2", 2, 2)).willReturn(expected);
        given(bookService.update(anyLong(), anyString(), any(), any())).willReturn(expected);
        // then title=wwwwwwww&author=2&genre=2
        mvc.perform(post("/book/update")
                        .param("id", "2")
                                .content("title=BookTitle_2&author=2&genre=2")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(expected))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/books"));
    }

    @Test
    void bookInsertTestShouldRedirectToBooks() throws Exception {
        // given
        Book expected = new Book(4, "BookTitle_4", authors.get(0), genres.get(0));
        given(bookService.insert("BookTitle_4", 1, 1)).willReturn(expected);
        // then
        mvc.perform(post("/book/insert")
                        .param("id", "4")
                        .param("title", "BookTitle_4")
                        .param("authorId", "1")
                        .param("genreId", "1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(expected))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/books"));
    }*/

    @Test
    void bookUpdateTestShouldRedirectToBooks() throws Exception {
        // given
        Book expected = books.get(1);
        given(bookService.update(any())).willReturn(expected);
        mvc.perform(post("/book/update/{id}", 2)
                                .content("title=BookTitle_2&author=2&genre=2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/books"));
    }

    @Test
    void bookInsertTestShouldRedirectToBooks() throws Exception {
        // given
        Book expected = new Book(0, "BookTitle_4", authors.get(0), genres.get(0));
        given(bookService.insert(any())).willReturn(expected);
        // then
        mvc.perform(post("/book/insert")
                        .content("title=BookTitle_4&author=1&genre=1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/books"));
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
