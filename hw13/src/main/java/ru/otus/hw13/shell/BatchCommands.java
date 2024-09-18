package ru.otus.hw13.shell;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;
import java.util.Properties;

import static ru.otus.hw13.config.JobConfig.JOB_NAME;

@ShellComponent
@RequiredArgsConstructor
public class BatchCommands {

    private final Job job;

    private final JobLauncher jobLauncher;

    private final JobOperator jobOperator;

    private final JobExplorer jobExplorer;

    private final JobRepository jobRepository;


    @ShellMethod(value = "start migration with JobLauncher", key = "start-ml")
    public void startMigrationJobWithJobLauncher() throws Exception {
        JobParameters parameters = new JobParametersBuilder()
                .toJobParameters();
        JobExecution execution = jobLauncher.run(job, parameters);
        System.out.println(execution);
    }

    @ShellMethod(value = "start migration with JobOperator", key = "start-mo")
    public void startMigrationJobWithJobOperator() throws Exception {
        Properties properties = new Properties();
        Long executionId = jobOperator.start(JOB_NAME, properties);
        System.out.println(jobOperator.getSummary(executionId));
    }

    @ShellMethod(value = "jobs info", key = "info")
    public void showInfo() {
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getLastJobInstance(JOB_NAME));
    }

    @ShellMethod(value = "delete JobInstanse from JobRepository {cnt}", key = "restart")
    public void restart(int cnt) {
        List<JobInstance> jobInstances = jobRepository.findJobInstancesByName(JOB_NAME, 0, cnt);
        jobInstances.forEach(jobRepository::deleteJobInstance);
    }

}
