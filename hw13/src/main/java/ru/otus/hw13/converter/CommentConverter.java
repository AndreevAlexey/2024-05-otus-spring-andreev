package ru.otus.hw13.converter;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw13.config.AppConfig;
import ru.otus.hw13.models.Comment;
import ru.otus.hw13.models.mongo.BookMongo;
import ru.otus.hw13.models.mongo.CommentMongo;

@Service
@RequiredArgsConstructor
public class CommentConverter {

    private final AppConfig appConfig;

    private final BookConverter bookConverter;

    public CommentMongo convertToMongo(Comment comment) {
        String mongoId = appConfig.getPrefixMongoId() + comment.getId();
        BookMongo bookMongo = bookConverter.convertToMongo(comment.getBook());
        return
                new CommentMongo(mongoId, bookMongo, comment.getText());
    }
}
