package com.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatchProfileApplication implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final Job job;

    public BatchProfileApplication(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    public static void main(String[] args) {
        SpringApplication.run(BatchProfileApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        jobLauncher.run(job, new JobParameters());
    }
}
