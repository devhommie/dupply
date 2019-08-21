package org.dupply.web.controller;

import org.dupply.web.model.JobDataJson;
import org.dupply.web.service.JobsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AjaxController {

    private JobsService jobsService;

    public AjaxController (JobsService jobsService) {
        this.jobsService = jobsService;
    }

    @GetMapping("/ajax/jobs/{jobId}")
    public JobDataJson getJob(@PathVariable("jobId") String jobId) {
        return JobDataJson.instance(jobsService.getJob(jobId));
    }
}
