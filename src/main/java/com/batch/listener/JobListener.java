package com.batch.listener;

import com.batch.model.Persona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobListener implements JobExecutionListener {

    private static final Logger LOG = LoggerFactory.getLogger(JobListener.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            LOG.info("FINALIZO EL JOB");

            jdbcTemplate.query("SELECT nombre, apellido, telefono FROM persona;",
                            (rs, rowNum) -> new Persona(rs.getString(1), rs.getString(2), rs.getString(3)))
                    .forEach(persona -> LOG.info("Registro < " + persona + " >"));
        }
    }
}
