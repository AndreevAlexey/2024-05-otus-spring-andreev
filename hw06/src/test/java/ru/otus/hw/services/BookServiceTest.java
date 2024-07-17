package ru.otus.hw.services;


import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaGenreRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Проверка сервиса книг на доступность связанных полей")
@Transactional(propagation = Propagation.NEVER)
@DataJpaTest
@Import({JpaAuthorRepository.class, JpaGenreRepository.class, JpaBookRepository.class, BookServiceImpl.class})
public class BookServiceTest {

    public static final long NOT_EXISTING_AUTHOR_ID = -1;

    @Autowired
    BookServiceImpl bookService;

    @Autowired
    EntityManager em;

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        // given
        var id = 1L;
        var expectedBook = em.find(Book.class, id);
        // when
        var actualBook = bookService.findById(id);
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook)
                .matches(book -> book.getAuthor().getFullName() != null);
    }


    @DisplayName("должен возвращать пустое значение по несуществующему ид")
    @Test
    void shouldReturnEmptyAuthorBookById() {
        // when
        var actualBook = bookService.findById(NOT_EXISTING_AUTHOR_ID);
        // then
        assertThat(actualBook).isEmpty();
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        // given
        var expectedBooks = getDbBooks();
        // when
        var actualBooks = bookService.findAll();
        // then
        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        // given
        var title = "BookTitle_10500";
        var authorId = 1L;
        var genreId = 1L;
        // when
        var returnedBook = bookService.insert(title, authorId, genreId);
        // then
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .matches(book -> book.getAuthor().getId() == authorId)
                .matches(book -> book.getGenre().getId() == genreId)
                .matches(book -> book.getTitle().equals(title))
                .matches(book -> book.getAuthor().getFullName() != null);

        assertThat(em.find(Book.class, returnedBook.getId()))
                .isNotNull()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        // given
        long bookId = 1L;
        var title = "BookTitle_10500";
        var authorId = 2L;
        var genreId = 2L;
        var author = em.find(Author.class, authorId);
        var genre = em.find(Genre.class, genreId);
        var expectedBook = new Book(bookId, title, author, genre);

        assertThat(em.find(Book.class, expectedBook.getId()))
                .isNotNull()
                .isNotEqualTo(expectedBook);
        // when
        var returnedBook = bookService.update(bookId, title, authorId, genreId);
        // then
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .matches(book -> book.getAuthor().getFullName() != null)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(em.find(Book.class, returnedBook.getId()))
                .isNotNull()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        // given
        long bookId = 4L;
        assertThat(em.find(Book.class, bookId)).isNotNull();
        // when
        bookService.deleteById(bookId);
        // then
        assertThat(em.find(Book.class, bookId)).isNull();
    }

    private List<Book> getDbBooks() {
        return IntStream.range(1, 5).boxed()
                .map(id -> em.find(Book.class, id))
                .filter(Objects::nonNull)
                .toList();
    }
}
