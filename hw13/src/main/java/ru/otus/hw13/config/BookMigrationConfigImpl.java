package ru.otus.hw13.config;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw13.converter.BookConverter;
import ru.otus.hw13.models.Book;
import ru.otus.hw13.models.mongo.BookMongo;
import ru.otus.hw13.repositories.BookRepository;
import ru.otus.hw13.repositories.mongo.BookMongoRepository;

@Configuration
@RequiredArgsConstructor
public class BookMigrationConfigImpl implements RepositoryMigrationConfig {

    private final BookRepository bookRepository;

    private final BookMongoRepository bookMongoRepository;

    private final BookConverter bookConverter;

    private final AppConfig appConfig;

    @Bean
    public RepositoryItemReader<Book> bookReader() {
        return reader("bookReader", bookRepository);
    }

    @Bean
    public ItemProcessor<Book, BookMongo> bookProcessor() {
        return bookConverter::convertToMongo;
    }

    @Bean
    public RepositoryItemWriter<BookMongo> bookMongoWriter() {
        return mongoWriter(bookMongoRepository);
    }

    @Override
    public int getPageSize() {
        return appConfig.getPageSize();
    }
}
