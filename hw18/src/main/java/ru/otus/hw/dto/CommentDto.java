package ru.otus.hw.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.hibernate.proxy.HibernateProxy;
import ru.otus.hw.models.Comment;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private long id;

    private BookDto book;

    private String text;

/*    public static CommentDto toDto(Comment comment) {
        return (comment instanceof HibernateProxy)
                ? new CommentDto()
                : new CommentDto(comment.getId(), BookDto.toDto(comment.getBook()), comment.getText());
    }*/

    public static CommentDto toDto(Comment comment) {
        return
                new CommentDto(comment.getId(), BookDto.toDto(comment.getBook()), comment.getText());
    }

    public Comment toEntity() {
        return new Comment(id, book.toEntity(), text);
    }
}
