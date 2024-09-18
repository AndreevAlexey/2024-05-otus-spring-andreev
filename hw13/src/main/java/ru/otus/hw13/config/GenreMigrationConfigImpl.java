package ru.otus.hw13.config;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw13.converter.GenreConverter;
import ru.otus.hw13.models.Genre;
import ru.otus.hw13.models.mongo.GenreMongo;
import ru.otus.hw13.repositories.GenreRepository;
import ru.otus.hw13.repositories.mongo.GenreMongoRepository;

@Configuration
@RequiredArgsConstructor
public class GenreMigrationConfigImpl implements RepositoryMigrationConfig {

    private final GenreRepository genreRepository;

    private final GenreMongoRepository genreMongoRepository;

    private final GenreConverter genreConverter;

    private final AppConfig appConfig;

    @Bean
    public RepositoryItemReader<Genre> genreReader() {
        return reader("genreReader", genreRepository);
    }

    @Bean
    public ItemProcessor<Genre, GenreMongo> genreProcessor() {
        return genreConverter::convertToMongo;
    }

    @Bean
    public RepositoryItemWriter<GenreMongo> genreMongoWriter() {
        return mongoWriter(genreMongoRepository);
    }

    @Override
    public int getPageSize() {
        return appConfig.getPageSize();
    }
}
