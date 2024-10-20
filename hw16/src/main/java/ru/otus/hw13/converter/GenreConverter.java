package ru.otus.hw13.converter;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw13.config.AppConfig;
import ru.otus.hw13.models.Genre;
import ru.otus.hw13.models.mongo.GenreMongo;

@Service
@RequiredArgsConstructor
public class GenreConverter {

    private final AppConfig appConfig;

    public GenreMongo convertToMongo(Genre genre) {
        String mongoId = appConfig.getPrefixMongoId() + genre.getId();
        return new GenreMongo(mongoId, genre.getName());
    }
}
