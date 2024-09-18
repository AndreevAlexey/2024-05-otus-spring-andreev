package ru.otus.hw13.config;

import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.Collections;

public interface RepositoryMigrationConfig {

    int getPageSize();

    default <T> RepositoryItemReader<T> reader(String name, JpaRepository<T, Long> repository) {
        return
                new RepositoryItemReaderBuilder<T>()
                        .name(name)
                        .repository(repository)
                        .methodName("findAll")
                        .arguments(new ArrayList<>())
                        .pageSize(getPageSize())
                        .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                        .build();
    }

    default <T> RepositoryItemWriter<T> mongoWriter(MongoRepository<T, String> repository) {
        return new RepositoryItemWriterBuilder<T>()
                .repository(repository)
                .methodName("save")
                .build();
    }
}
