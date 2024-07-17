package ru.otus.hw.services;


import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaCommentRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Проверка сервиса комментариев на доступность связанных полей")
@Transactional(propagation = Propagation.NEVER)
@DataJpaTest
@Import({JpaCommentRepository.class, JpaBookRepository.class, CommentServiceImpl.class})
public class CommentServiceTest {

    @Autowired
    CommentServiceImpl commentService;

    @Autowired
    EntityManager em;

    @Test
    void shouldReturnCorrectCommentById() {
        // given
        long id = 1L;
        var expectedComment = em.find(Comment.class, id);
        // when
        var actualComment = commentService.findById(id);
        // then
        System.out.println("expectedComment = "+expectedComment.getId());
        System.out.println("actualComment = "+actualComment.get().getId());
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment)
                .matches(comment -> comment.getBook().getTitle() != null);
    }

    @DisplayName("должен возвращать список всех комментариев по ид книги")
    @Test
    void shouldReturnAllCommentsByBookId() {
        // given
        var bookId = 2L;
        var expectedComments = getDbComments().stream()
                .filter(comment -> comment.getBook().getId() == bookId)
                .toList();
        // when
        var actualComments = commentService.findAllByBookId(bookId);
        // then
        assertThat(actualComments).isNotEmpty()
                .containsExactlyElementsOf(expectedComments)
                .allMatch(comment -> comment.getBook().getTitle() != null);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        // given
        var bookId = 1L;
        var text = "Save comment test";
        var book = em.find(Book.class, bookId);
        var expectedComment = new Comment(0, book, text);
        // when
        var returnedComment = commentService.insert(bookId, text);
        // then
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .matches(comment -> comment.getBook().getTitle() != null)
                .matches(comment -> comment.getBook().getAuthor().getFullName() != null)
                .matches(comment -> comment.getText().equals(expectedComment.getText()))
                .matches(comment -> comment.getBook().getId() == expectedComment.getBook().getId());

        assertThat(em.find(Comment.class, returnedComment.getId()))
                .isNotNull()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        // given
        long commentId = 1L;
        long bookId = 1L;
        var text = "Update comment test";
        var book = em.find(Book.class, bookId);
        var expectedComment = new Comment(commentId, book, text);

        assertThat(em.find(Comment.class, expectedComment.getId()))
                .isNotNull()
                .isNotEqualTo(expectedComment);
        // when
        var returnedComment = commentService.update(commentId, bookId, text);
        // then
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .matches(comment -> comment.getBook().getTitle() != null)
                .isEqualTo(expectedComment);

        assertThat(em.find(Comment.class, returnedComment.getId()))
                .isNotNull()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteByIdByIdComment() {
        // given
        long commentId = 7L;
        assertThat(em.find(Comment.class, commentId)).isNotNull();
        // when
        commentService.deleteById(commentId);
        // then
        assertThat(em.find(Comment.class, commentId)).isNull();
    }

    private List<Comment> getDbComments() {
        return IntStream.range(1, 8)
                .boxed()
                .map(id -> em.find(Comment.class, id))
                .filter(Objects::nonNull)
                .toList();
    }
}
