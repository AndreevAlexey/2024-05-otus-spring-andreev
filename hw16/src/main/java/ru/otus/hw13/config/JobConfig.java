package ru.otus.hw13.config;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class JobConfig {

    public static final String JOB_NAME = "migrationLibraryJob";

    private final AppConfig appConfig;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    private final AuthorMigrationConfigImpl authorMigrationConfig;

    private final GenreMigrationConfigImpl genreMigrationConfig;

    private final BookMigrationConfigImpl bookMigrationConfig;

    private final CommentMigrationConfigImpl commentMigrationConfig;

    @Bean
    public Step authorMigration() {
        return
                step("authorMigration"
                        , authorMigrationConfig.authorReader()
                        , authorMigrationConfig.authorProcessor()
                        , authorMigrationConfig.authorMongoWriter());
    }

    @Bean
    public Step genreMigration() {
        return
                step("genreMigration"
                        , genreMigrationConfig.genreReader()
                        , genreMigrationConfig.genreProcessor()
                        , genreMigrationConfig.genreMongoWriter());
    }

    @Bean
    public Step bookMigration() {
        return
                step("bookMigration"
                        , bookMigrationConfig.bookReader()
                        , bookMigrationConfig.bookProcessor()
                        , bookMigrationConfig.bookMongoWriter());
    }

    @Bean
    public Step commentMigration() {
        return
                step("commentMigration"
                        , commentMigrationConfig.commentReader()
                        , commentMigrationConfig.commentProcessor()
                        , commentMigrationConfig.commentMongoWriter());
    }

    @Bean
    public Job transferAuthorJob() {
        return
                new JobBuilder(JOB_NAME, jobRepository)
                        .incrementer(new RunIdIncrementer())
                        .start(authorMigration())
                        .next(genreMigration())
                        .next(bookMigration())
                        .next(commentMigration())
                        .build();
    }

    public <T,E> Step step(String name,
                           RepositoryItemReader<T> reader,
                           ItemProcessor<T, E> processor,
                           RepositoryItemWriter<E> writer) {
        return
                new StepBuilder(name, jobRepository)
                        .<T, E>chunk(appConfig.getChunkSize(), transactionManager)
                        .reader(reader)
                        .processor(processor)
                        .writer(writer)
                        .listener(new StepExecutionListener() {
                            @Override
                            public void beforeStep(@NonNull StepExecution stepExecution) {
                                log.info("Start step: {}", stepExecution.getStepName());
                            }

                            @Override
                            public ExitStatus afterStep(@NonNull StepExecution stepExecution) {
                                log.info("End step: {}", stepExecution.getStepName());
                                return ExitStatus.COMPLETED;
                            }
                        })
                        .build();
    }

}