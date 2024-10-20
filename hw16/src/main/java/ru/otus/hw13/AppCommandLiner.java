package ru.otus.hw13;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class AppCommandLiner implements CommandLineRunner {

    private final JobLauncher jobLauncher;

    private final Job job;

    @Override
    public void run(String... args) throws Exception {
        jobLauncher.run(job, new JobParameters());
    }
}
