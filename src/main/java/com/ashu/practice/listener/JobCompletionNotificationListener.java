package com.ashu.practice.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobCompletionNotificationListener implements JobExecutionListener {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("JOB NOT STARTED! Cleanup history table");

        String query = "truncate table city_history;";
        jdbcTemplate.execute(query);
        log.info("Successfully truncated city history table.");

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("JOB FINISHED! Time to verify the results");

            String query = "SELECT count(id) FROM city_history";
            Long rowCount = jdbcTemplate.queryForObject(query, Long.class);
            log.info("Found {} records in city history table.", rowCount);
        }
    }
}
