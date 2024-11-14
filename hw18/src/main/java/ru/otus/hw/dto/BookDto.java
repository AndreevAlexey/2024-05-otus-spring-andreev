package ru.otus.hw.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.hibernate.proxy.HibernateProxy;
import ru.otus.hw.models.Book;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    private long id;

    private String title;

    private AuthorDto author;

    private GenreDto genre;

/*    public static BookDto toDto(Book book) {
        return (book instanceof HibernateProxy)
                ? new BookDto()
                : new BookDto(book.getId()
                            , book.getTitle()
                            , AuthorDto.toDto(book.getAuthor())
                            , GenreDto.toDto(book.getGenre()));
    }*/

    public static BookDto toDto(Book book) {
        return
                new BookDto(book.getId()
                , book.getTitle()
                , AuthorDto.toDto(book.getAuthor())
                , GenreDto.toDto(book.getGenre()));
    }

    public Book toEntity() {
        return new Book(id, title, author.toEntity(), genre.toEntity());
    }
}
