package org.dupply.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.dupply.core.FileDesc;
import org.dupply.web.util.FormatUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobDataJson {

    protected String id;
    protected boolean running;
    protected long processed;
    protected List<FileDuplicate> result;
    protected Date created;
    protected Date finished;
    protected String scanType;
    protected String scanTypeName;
    protected String scanRoot;
    protected List<String> fileExtensions;

    public static JobDataJson instance(JobData jobData) {
        if (jobData == null) {
            return null;
        }
        JobDataJson result = new JobDataJson();
        result.setId(jobData.getId());
        result.setRunning(jobData.isRunning());
        result.setProcessed(jobData.getProcessed());
        result.setCreated(jobData.getCreated());
        result.setFinished(jobData.getFinished());
        result.setScanType(jobData.getScanType() != null ? jobData.getScanType().name() : null);
        result.setScanTypeName(jobData.getScanType() != null ? jobData.getScanType().getTypeName() : null);
        result.setScanRoot(jobData.getScanRoot() != null ? jobData.getScanRoot().toString() : null);
        result.setFileExtensions(jobData.getFileExtensions());

        if (jobData.getResult() != null && !jobData.getResult().isEmpty()) {
            List<FileDuplicate> resultJson = new ArrayList<>();
            result.setResult(resultJson);
            for (Map.Entry<FileDesc, List<File>> entry : jobData.getResult().entrySet()) {
                FileDuplicate fileDuplicate = new FileDuplicate();
                fileDuplicate.setName(entry.getKey().getName());
                fileDuplicate.setSize(entry.getKey().getLength());
                fileDuplicate.setSizeName(FormatUtils.humanReadableByteCount(entry.getKey().getLength(), false));
                fileDuplicate.setLocations(entry.getValue().stream().map(File::toString).collect(Collectors.toList()));
                resultJson.add(fileDuplicate);
            }
        }

        return result;

    }

    public JobDataJson() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public long getProcessed() {
        return processed;
    }

    public void setProcessed(long processed) {
        this.processed = processed;
    }

    public List<FileDuplicate> getResult() {
        return result;
    }

    public void setResult(List<FileDuplicate> result) {
        this.result = result;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getFinished() {
        return finished;
    }

    public void setFinished(Date finished) {
        this.finished = finished;
    }

    public String getScanType() {
        return scanType;
    }

    public void setScanType(String scanType) {
        this.scanType = scanType;
    }

    public String getScanTypeName() {
        return scanTypeName;
    }

    public void setScanTypeName(String scanTypeName) {
        this.scanTypeName = scanTypeName;
    }

    public String getScanRoot() {
        return scanRoot;
    }

    public void setScanRoot(String scanRoot) {
        this.scanRoot = scanRoot;
    }

    public List<String> getFileExtensions() {
        return fileExtensions;
    }

    public void setFileExtensions(List<String> fileExtensions) {
        this.fileExtensions = fileExtensions;
    }

    @Override
    public String toString() {
        return "JobDataJson{" +
                "id='" + id + '\'' +
                ", running=" + running +
                ", processed=" + processed +
                ", result=" + result +
                ", created=" + created +
                ", finished=" + finished +
                ", scanType='" + scanType + '\'' +
                ", scanTypeName='" + scanTypeName + '\'' +
                ", scanRoot='" + scanRoot + '\'' +
                ", fileExtensions=" + fileExtensions +
                '}';
    }
}
