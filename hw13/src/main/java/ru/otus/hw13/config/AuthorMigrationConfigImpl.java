package ru.otus.hw13.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw13.converter.AuthorConverter;
import ru.otus.hw13.models.Author;
import ru.otus.hw13.models.mongo.AuthorMongo;
import ru.otus.hw13.repositories.AuthorRepository;
import ru.otus.hw13.repositories.mongo.AuthorMongoRepository;

@Configuration
@RequiredArgsConstructor
public class AuthorMigrationConfigImpl implements RepositoryMigrationConfig {


    private final AuthorRepository authorRepository;

    private final AuthorMongoRepository authorMongoRepository;

    private final AuthorConverter authorConverter;

    private final AppConfig appConfig;

    @Bean
    public RepositoryItemReader<Author> authorReader() {
        return reader("authorReader", authorRepository);
    }

    @Bean
    public ItemProcessor<Author, AuthorMongo> authorProcessor() {
        return authorConverter::convertToMongo;
    }

    @Bean
    public RepositoryItemWriter<AuthorMongo> authorMongoWriter() {
        return mongoWriter(authorMongoRepository);
    }

    @Override
    public int getPageSize() {
        return appConfig.getPageSize();
    }
}
