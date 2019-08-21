package org.dupply.web.controller;

import org.dupply.web.model.JobData;
import org.dupply.web.model.JobDataJson;
import org.dupply.web.service.JobsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class JobsController {

    private static final Logger logger = LoggerFactory.getLogger(JobsController.class);

    private JobsService jobsService;

    public JobsController(JobsService jobsService) {
        this.jobsService = jobsService;
    }

    @GetMapping("/jobs")
    public String listJobs(Model model, HttpServletResponse response) {
        model.addAttribute("jobs", asJobDataJsons(jobsService.list()));
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        return "jobs";
    }

    @RequestMapping("/jobs/{jobId}")
    public String showJob(@PathVariable("jobId") String jobId, Model model, HttpServletResponse response) {
        model.addAttribute("job", JobDataJson.instance(jobsService.getJob(jobId)));
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        return "job";
    }

    @PostMapping("/jobs/{jobId}")
    public String deleteFile(@RequestParam("location") String fileLocation, @PathVariable("jobId") String jobId, Model model, HttpServletResponse response) {
        logger.info("Try to delete file [{}] of jobId[{}]", fileLocation, jobId);
        jobsService.deleteFile(jobId, fileLocation);
        return showJob(jobId, model, response);

    }

    List<JobDataJson> asJobDataJsons(List<JobData> jobs) {
        if (jobs == null) {
            return null;
        }
        return jobs.stream().map(JobDataJson::instance).collect(Collectors.toList());
    }

}
