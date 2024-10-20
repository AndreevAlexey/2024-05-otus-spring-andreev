package ru.otus.hw13.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw13.config.AppConfig;
import ru.otus.hw13.models.Author;
import ru.otus.hw13.models.mongo.AuthorMongo;


@Service
@RequiredArgsConstructor
public class AuthorConverter {

    private final AppConfig appConfig;

    public AuthorMongo convertToMongo(Author author) {
        String mongoId = appConfig.getPrefixMongoId() + author.getId();
        return new
                AuthorMongo(mongoId, author.getFullName());
    }
}
