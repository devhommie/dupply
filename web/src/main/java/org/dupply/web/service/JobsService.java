package org.dupply.web.service;

import org.dupply.web.model.JobData;
import java.util.List;

public interface JobsService {

    String startJob(JobData jobData);

    JobData getJob(String id);

    List<JobData> list();

    void deleteJob(String id);

    void deleteFile(String jobId, String file);

}
