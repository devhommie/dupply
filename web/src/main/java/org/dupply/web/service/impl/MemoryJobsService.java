package org.dupply.web.service.impl;

import org.dupply.core.DScanner;
import org.dupply.core.FileDesc;
import org.dupply.web.model.JobData;
import org.dupply.web.service.JobsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class MemoryJobsService implements JobsService {

    private static final Logger logger = LoggerFactory.getLogger(MemoryJobsService.class);

    private Map<String, JobData> store = new ConcurrentHashMap<>();
    private ExecutorService executorService;

    public MemoryJobsService(int threads) {
        this.executorService = Executors.newFixedThreadPool(threads, new ThreadFactory() {
            int counter = 1;
            @Override
            public Thread newThread(Runnable r) {
                Thread result = new Thread(r);
                result.setDaemon(true);
                result.setName("worker-" + counter++);
                return result;
            }
        });
    }

    @Override
    public String startJob(JobData jobData) {
        String guid = UUID.randomUUID().toString();
        jobData.setId(guid);
        jobData.setCreated(new Date());
        store.put(guid, jobData);
        logger.info("Try to start job [{}]", jobData);
        executorService.submit(() -> {
            jobData.setRunning(true);
            DScanner.find(jobData.getScanRoot(), jobData.getScanType(),
                    file -> isTargetFile(file, jobData.getFileExtensions()),
                    processedFiles -> jobData.setProcessed(processedFiles),
                    fileDescListMap -> {
                        jobData.setRunning(false);
                        jobData.setResult(fileDescListMap);
                        jobData.setFinished(new Date());
                    });
        });

        return guid;
    }

    @Override
    public JobData getJob(String id) {
        return store.get(id);
    }

    @Override
    public List<JobData> list() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteJob(String id) {
        store.remove(id);
    }

    @Override
    public void deleteFile(String jobId, String file) {
        JobData job = store.get(jobId);
        if (job != null && job.getResult() != null) {
            for (Map.Entry<FileDesc, List<File>> entry : job.getResult().entrySet()) {
                if (entry.getValue() != null) {
                    Iterator<File> fileIterator = entry.getValue().iterator();
                    while (fileIterator.hasNext()) {
                        File aFile = fileIterator.next();
                        if (aFile.toString().equals(file) && aFile.delete()) {
                            fileIterator.remove();
                            if (entry.getValue().size() == 1) {
                                job.getResult().remove(entry.getKey());
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    boolean isTargetFile(File file, List<String> fileExtensions) {
        if (file.isFile()) {
            if (fileExtensions == null || fileExtensions.isEmpty() || fileExtensions.contains("*")) {
                return true;
            }

            String filename = file.getName().toLowerCase();
            for (String extension : fileExtensions) {
                if (filename.endsWith(extension)) {
                    return true;
                }
            }
        }
        return false;
    }
}
