package ru.otus.hw13.config;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw13.converter.CommentConverter;
import ru.otus.hw13.models.Comment;
import ru.otus.hw13.models.mongo.CommentMongo;
import ru.otus.hw13.repositories.CommentRepository;
import ru.otus.hw13.repositories.mongo.CommentMongoRepository;

@Configuration
@RequiredArgsConstructor
public class CommentMigrationConfigImpl implements RepositoryMigrationConfig {

    private final CommentRepository commentRepository;

    private final CommentMongoRepository commentMongoRepository;

    private final CommentConverter commentConverter;

    private final AppConfig appConfig;

    @Bean
    public RepositoryItemReader<Comment> commentReader() {
        return reader("commentReader", commentRepository);
    }

    @Bean
    public ItemProcessor<Comment, CommentMongo> commentProcessor() {
        return commentConverter::convertToMongo;
    }

    @Bean
    public RepositoryItemWriter<CommentMongo> commentMongoWriter() {
        return mongoWriter(commentMongoRepository);
    }

    @Override
    public int getPageSize() {
        return appConfig.getPageSize();
    }
}
