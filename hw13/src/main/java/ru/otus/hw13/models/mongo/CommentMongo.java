package ru.otus.hw13.models.mongo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;



@Document(collection = "comments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentMongo {

    @Id
    private String id;

    @DBRef
    private BookMongo book;

    @Field(name = "text")
    private String text;

}
