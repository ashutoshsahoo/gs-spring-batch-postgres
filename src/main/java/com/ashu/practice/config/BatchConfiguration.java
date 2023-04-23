package com.ashu.practice.config;

import com.ashu.practice.domain.City;
import com.ashu.practice.listener.JobCompletionNotificationListener;
import com.ashu.practice.mapper.CityRowMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration {

    @Autowired
    private DataSource dataSource;


    @Bean
    public JdbcCursorItemReader<City> reader() {
        return new JdbcCursorItemReaderBuilder<City>()
                .dataSource(dataSource)
                .name("cityReader")
                .sql("select * from city")
                .rowMapper(new CityRowMapper())
                .build();
    }


    @Bean
    public JdbcBatchItemWriter<City> writer() {
        return new JdbcBatchItemWriterBuilder<City>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO city_history (id, name, countrycode, district,population) " +
                        "VALUES (:id, :name, :countrycode, :district, :population)")
                .dataSource(dataSource)
                .build();
    }


    @Bean
    public Job importUserJob(JobRepository jobRepository, JobCompletionNotificationListener listener, Step step1) {
        return new JobBuilder("importCityJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager, JdbcBatchItemWriter<City> writer) {
        return new StepBuilder("step1", jobRepository)
                .<City, City>chunk(10, transactionManager)
                .reader(reader())
                .writer(writer)
                .build();
    }

}