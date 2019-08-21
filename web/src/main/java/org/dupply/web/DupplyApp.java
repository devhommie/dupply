package org.dupply.web;

import org.dupply.web.service.JobsService;
import org.dupply.web.service.impl.MemoryJobsService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DupplyApp {

    @Bean
    public JobsService jobsService() {
        return new MemoryJobsService(1);
    }

    public static void main(String[] args) {
        SpringApplication.run(DupplyApp.class, args);
    }

}
