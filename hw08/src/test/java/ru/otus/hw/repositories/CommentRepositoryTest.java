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
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    List<Comment> comments;

    List<Book> books;

    @BeforeEach
    void setUp() {
        books = getBooks();
        comments = getComments();
    }

    @DisplayName("должен возвращать комментарий по ид")
    @ParameterizedTest
    @MethodSource("getDbCommentsId")
    void shouldReturnCorrectCommentById(String id) {
        // given
        var expectedComment = comments.stream()
                .filter(comment -> comment.getId().equals(id))
                .findFirst()
                .orElse(null);
        // when
        var actualComment = commentRepository.findById(id);
        // then
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать список всех комментариев")
    @Test
    void shouldReturnCorrectCommentList() {
        // given
        var expectedComments = comments;
        // when
        var actualComments = commentRepository.findAll();
        // then
        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
    }

    @DisplayName("должен возвращать список всех комментариев по ид книги")
    @Test
    void shouldReturnAllCommentsByBookId() {
        // given
        var bookId = "2";
        var expectedComments = comments.stream()
                .filter(comment -> comment.getBook().getId().equals(bookId))
                .toList();
        // when
        var actualComments = commentRepository.findCommentsByBookId(bookId);
        // then
        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        // given
        var book = books.get(0);
        var expectedComment = new Comment(null, book, "Save comment test");
        // when
        var returnedComment = commentRepository.save(expectedComment);
        // then
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() != null && !comment.getId().isEmpty())
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(commentRepository.findById(returnedComment.getId()))
                .isNotNull()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        // given
        var commentId = "1";
        var book = books.get(0);
        var expectedComment = new Comment(commentId, book, "Update comment test");

        assertThat(commentRepository.findById(expectedComment.getId()))
                .isNotNull()
                .get()
                .isNotEqualTo(expectedComment);
        // when
        var returnedComment = commentRepository.save(expectedComment);
        // then
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() != null && !comment.getId().isEmpty())
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(commentRepository.findById(returnedComment.getId()))
                .isNotNull()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteByIdComment() {
        // given
        var commentId = "1";
        assertThat(commentRepository.findById(commentId)).isNotEmpty();
        // when
        commentRepository.deleteById(commentId);
        // then
        assertThat(commentRepository.findById(commentId)).isEmpty();
    }

    public static List<String> getDbCommentsId() {
        return
                Stream.of("1", "2", "3", "4", "5", "6")
                        .toList();
    }

    private List<Comment> getComments() {
        return List.of(
                new Comment("1", books.get(0), "Comment_1_1"),
                new Comment("2", books.get(1), "Comment_2_1"),
                new Comment("3", books.get(1), "Comment_2_2"),
                new Comment("4", books.get(2), "Comment_3_1"),
                new Comment("5", books.get(2), "Comment_3_2"),
                new Comment("6", books.get(2), "Comment_3_3")
        );
    }

    private List<Book> getBooks() {
        return
            Stream.of("1", "2", "3")
                    .map(id -> {
                        Book book = new Book();
                        book.setId(id);
                        return book;
                    })
                    .toList();
    }
}
