package ru.otus.hw.repositories;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaCommentRepository.class)
public class JpaCommentRepositoryTest {

    public static final long NOT_EXISTING_AUTHOR_ID = -1;

    @Autowired
    JpaCommentRepository commentRepository;

    @Autowired
    TestEntityManager em;

    List<Comment> dbComments;

    @BeforeEach
    void setUp() {
        dbComments = getDbComments();
    }


    @DisplayName("должен возвращать комментарий по ид")
    @ParameterizedTest
    @MethodSource("getDbCommentsId")
    void shouldReturnCorrectCommentById(long id) {
        // given
        var expectedComment = em.find(Comment.class, id);
        // when
        var actualComment = commentRepository.findById(id);
        // then
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен возвращать список всех комментариев по ид книги")
    @Test
    void shouldReturnAllCommentsByBookId() {
        // given
        var bookId = 2L;
        var expectedComments = dbComments.stream()
                .filter(comment -> comment.getBook().getId() == bookId)
                .toList();
        // when
        var actualComments = commentRepository.findAllByBookId(bookId);
        // then
        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        // given
        var book = em.find(Book.class, 1L);
        var expectedComment = new Comment(0, book, "Save comment test");
        // when
        var returnedComment = commentRepository.save(expectedComment);
        // then
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(em.find(Comment.class, returnedComment.getId()))
                .isNotNull()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        // given
        long commentId = 1L;
        var book = em.find(Book.class, 1L);
        var expectedComment = new Comment(commentId, book, "Update comment test");

        assertThat(em.find(Comment.class, expectedComment.getId()))
                .isNotNull()
                .isNotEqualTo(expectedComment);
        // when
        var returnedComment = commentRepository.save(expectedComment);
        // then
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(em.find(Comment.class, returnedComment.getId()))
                .isNotNull()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteByIdComment() {
        // given
        long commentId = 1L;
        assertThat(em.find(Comment.class, commentId)).isNotNull();
        // when
        commentRepository.deleteById(commentId);
        em.clear();
        // then
        assertThat(em.find(Comment.class, commentId)).isNull();
    }

    public static List<Long> getDbCommentsId() {
        return
                LongStream.range(1, 7)
                        .boxed()
                        .toList();
    }

    private List<Comment> getDbComments() {
        return IntStream.range(1, 7)
                .boxed()
                .map(id -> em.find(Comment.class, id))
                .toList();
    }
}
