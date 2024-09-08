package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.proxy.HibernateProxy;
import ru.otus.hw.models.Author;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {

    private long id;

    private String fullName;

    public static AuthorDto toDto(Author author) {
        return (author instanceof HibernateProxy)
                ? new AuthorDto()
                : new AuthorDto(author.getId(), author.getFullName());
    }

    public Author toEntity() {
        return new Author(id, fullName);
    }
}
