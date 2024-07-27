package ru.otus.hw.repositories;

import de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Репозиторий на основе JPA для работы с книгами ")
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private List<Author> authors;

    private List<Genre> genres;

    private List<Book> books;


    @BeforeEach
    void setUp() {
        authors = getDbAuthors();
        genres = getDbGenres();
        books = getDbBooks();
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getDbBooksId")
    void shouldReturnCorrectBookById(String id) {
        // given
        var expectedBook = books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElse(null);
        // when
        books.forEach(System.out::println);
        System.out.println("expectedBook = " + expectedBook);
        var actualBook = bookRepository.findById(id);
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        // given
        var expectedBooks = books;
        // when
        var actualBooks = bookRepository.findAll();
        // then
        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
    }


    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        // given
        var author = authors.get(0);
        var genre = genres.get(0);
        var expectedBook = new Book(null, "BookTitle_10500", author, genre);
        // when
        var returnedBook = bookRepository.save(expectedBook);
        // then
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() != null && !book.getId().isEmpty())
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(bookRepository.findById(returnedBook.getId()))
                .isNotNull()
                .get()
                .isEqualTo(returnedBook);
    }


    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        // given
        String bookId = "1";
        var author = authors.get(1);
        var genre = genres.get(1);
        var expectedBook = new Book(bookId, "BookTitle_10500", author, genre);

        assertThat(bookRepository.findById(expectedBook.getId()))
                .isNotNull()
                .isNotEqualTo(expectedBook);
        // when
        var returnedBook = bookRepository.save(expectedBook);
        // then
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() != null && !book.getId().isEmpty())
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(bookRepository.findById(returnedBook.getId()))
                .isNotNull()
                .get()
                .isEqualTo(returnedBook);
    }


    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        // given
        String bookId = "1";
        assertThat(bookRepository.findById(bookId)).isNotNull();
        // when
        bookRepository.deleteById(bookId);
        // then
        assertThat(bookRepository.findById(bookId)).isEmpty();
    }

    private static List<String> getDbBooksId() {
        return List.of("1", "2", "3");
    }

    private List<Book> getDbBooks() {
        return List.of(
                new Book("1", "BookTitle_1", authors.get(0), genres.get(0)),
                new Book("2", "BookTitle_2", authors.get(1), genres.get(1)),
                new Book("3", "BookTitle_3", authors.get(2), genres.get(2))
        );
    }

    private List<Author> getDbAuthors() {
        return List.of(
                new Author("1", "Author_1"),
                new Author("2", "Author_2"),
                new Author("3", "Author_3"));
    }

    private List<Genre> getDbGenres() {
        return List.of(
                new Genre("1", "Genre_1"),
                new Genre("2", "Genre_2"),
                new Genre("3", "Genre_3")
        );
    }
}