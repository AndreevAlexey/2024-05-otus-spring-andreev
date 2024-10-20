package ru.otus.hw13.converter;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw13.config.AppConfig;
import ru.otus.hw13.models.Book;
import ru.otus.hw13.models.mongo.AuthorMongo;
import ru.otus.hw13.models.mongo.BookMongo;
import ru.otus.hw13.models.mongo.GenreMongo;

@Service
@RequiredArgsConstructor
public class BookConverter {

    private final AppConfig appConfig;

    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public BookMongo convertToMongo(Book book) {
        String mongoId = appConfig.getPrefixMongoId() + book.getId();
        AuthorMongo authorMongo = authorConverter.convertToMongo(book.getAuthor());
        GenreMongo genreMongo = genreConverter.convertToMongo(book.getGenre());
        return
                new BookMongo(mongoId, book.getTitle(), authorMongo, genreMongo);
    }
}
