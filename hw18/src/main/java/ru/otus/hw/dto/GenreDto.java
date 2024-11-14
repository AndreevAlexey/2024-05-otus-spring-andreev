package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.hibernate.proxy.HibernateProxy;
import ru.otus.hw.models.Genre;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {

    private long id;

    private String name;

/*    public static GenreDto toDto(Genre genre) {
        return (genre instanceof HibernateProxy)
                ? new GenreDto()
                : new GenreDto(genre.getId(), genre.getName());
    }*/

    public static GenreDto toDto(Genre genre) {
        return
                new GenreDto(genre.getId(), genre.getName());
    }

    public Genre toEntity() {
        return new Genre(id, name);
    }
}
