package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с книгами ")
@DataJpaTest
@Import({JpaBookRepository.class})
class JpaBookRepositoryTest {



    @Autowired
    private JpaBookRepository bookRepository;

    @Autowired
    private TestEntityManager em;


    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getDbBooksId")
    void shouldReturnCorrectBookById(long id) {
        // given
        var expectedBook = em.find(Book.class, id);
        // when
        var actualBook = bookRepository.findById(id);
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }


    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        // given
        var expectedBooks = getDbBooks();
        // when
        var actualBooks = bookRepository.findAll();
        // then
        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        // given
        var author = em.find(Author.class, 1L);
        var genre = em.find(Genre.class, 1L);
        var expectedBook = new Book(0, "BookTitle_10500", author, genre);
        // when
        var returnedBook = bookRepository.save(expectedBook);
        // then
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(em.find(Book.class, returnedBook.getId()))
                .isNotNull()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        // given
        long bookId = 1L;
        var author = em.find(Author.class, 2L);
        var genre = em.find(Genre.class, 2L);
        var expectedBook = new Book(bookId, "BookTitle_10500", author, genre);

        assertThat(em.find(Book.class, expectedBook.getId()))
                .isNotNull()
                .isNotEqualTo(expectedBook);
        // when
        var returnedBook = bookRepository.save(expectedBook);
        // then
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(em.find(Book.class, returnedBook.getId()))
                .isNotNull()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        // given
        long bookId = 1L;
        assertThat(em.find(Book.class, bookId)).isNotNull();
        // when
        bookRepository.deleteById(bookId);
        em.clear();
        // then
        assertThat(em.find(Book.class, bookId)).isNull();
    }

    private static List<Long> getDbBooksId() {
        return LongStream.range(1, 4)
                .boxed()
                .toList();
    }

    private List<Book> getDbBooks() {
        return IntStream.range(1, 4).boxed()
                .map(id -> em.find(Book.class, id))
                .toList();
    }
}