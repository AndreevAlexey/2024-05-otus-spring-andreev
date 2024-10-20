package ru.otus.hw13.models.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;



@Document(collection = "books")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookMongo {

    @Id
    private String id;

    @Field(name = "title")
    private String title;

    private AuthorMongo author;

    private GenreMongo genre;

}
