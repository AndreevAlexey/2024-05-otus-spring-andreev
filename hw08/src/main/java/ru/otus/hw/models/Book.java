package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;



@Document(collection = "books")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Book {

    @Id
    private String id;

    @Field(name = "title")
    private String title;

    private Author author;

    private Genre genre;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Book book = (Book) o;
        return Objects.equals(id, book.id)
                && Objects.equals(title, book.title)
                && Objects.equals(author.getId(), book.author.getId())
                && Objects.equals(genre.getId(), book.genre.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author.getId(), genre.getId());
    }
}
