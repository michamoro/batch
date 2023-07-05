package com.batch.config.batch;

import com.batch.dto.CarDto;
import com.batch.listener.CarJobExecutionListener;
import com.batch.model.CarEntity;
import com.batch.processor.CarItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;

    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager, DataSource dataSource) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.dataSource = dataSource;
    }

    @Bean
    public ItemReader<CarDto> reader() {
        return new FlatFileItemReaderBuilder<CarDto>()
                .name("carItemReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .linesToSkip(1)
                .delimited()
                .names("registration", "colour", "model", "fuelType")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<CarDto>() {{
                    setTargetType(CarDto.class);
                }}).build();
    }

    @Bean
    public CarItemProcessor processor() {
        return new CarItemProcessor();
    }

    @Bean
    public ItemWriter<CarEntity> writer() {
        return new JdbcBatchItemWriterBuilder<CarEntity>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO cars (id, registration, colour, model, fuelType ) VALUES (:id, :registration, " +
                        ":colour, :model, :fuelType)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Step step1(ItemReader<CarDto> reader, ItemWriter<CarEntity> writer, ItemProcessor<CarDto, CarEntity> processor) {
        return new StepBuilder("step1", jobRepository)
                .<CarDto, CarEntity>chunk(2, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job createEmployeeJob(CarJobExecutionListener listener, Step step1) {
        return new JobBuilder("createEmployeeJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }
}
