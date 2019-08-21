package org.dupply.web.controller;

import org.dupply.core.ScanType;
import org.dupply.web.model.JobData;
import org.dupply.web.service.JobsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Controller
public class SearchController {

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    private JobsService jobsService;

    public SearchController(JobsService jobsService) {
        this.jobsService = jobsService;
    }

    @PostMapping("/search")
    public String showPage(@RequestParam("scan_directory") String scanDir,
                           @RequestParam("scan_type") String scanType,
                           @RequestParam(value = "file_extensions", required = false) String fileExtensionsStr,
                           Model model) {

        logger.debug("Try to scan dir [{}] by type [{}]", scanDir, scanType);

        List<String> fileExtensions = fileExtensionsStr != null ? Arrays.asList(fileExtensionsStr.split("\\s*\\,\\s*")) : null;

        model.addAttribute("scanDir", scanDir);
        model.addAttribute("scanType", scanType);
        model.addAttribute("fileExtensions", fileExtensions);

        if (!StringUtils.hasLength(scanDir)) {
            model.addAttribute("error", "Необходимо указать директорию для поиска дублей");
            return "search";
        }

        JobData jobData = new JobData();
        jobData.setScanRoot(new File(scanDir));
        jobData.setScanType(ScanType.fromString(scanType));
        jobData.setFileExtensions(fileExtensions);
        String jobId = jobsService.startJob(jobData);
        return "redirect:/jobs/" + jobId;
    }

}
