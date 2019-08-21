package org.dupply.web.model;

import org.dupply.core.FileDesc;
import org.dupply.core.ScanType;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JobData {

    private String id;
    private boolean running;
    private long processed;
    private Map<FileDesc, List<File>> result;
    private Date created;
    private Date finished;
    private ScanType scanType;
    private File scanRoot;
    private List<String> fileExtensions;

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

    public Map<FileDesc, List<File>> getResult() {
        return result;
    }

    public void setResult(Map<FileDesc, List<File>> result) {
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

    public ScanType getScanType() {
        return scanType;
    }

    public void setScanType(ScanType scanType) {
        this.scanType = scanType;
    }

    public File getScanRoot() {
        return scanRoot;
    }

    public void setScanRoot(File scanRoot) {
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
        return "JobData{" +
                "id='" + id + '\'' +
                ", running=" + running +
                ", processed=" + processed +
                ", result=" + result +
                ", created=" + created +
                ", finished=" + finished +
                ", scanType=" + scanType +
                ", scanRoot=" + scanRoot +
                ", fileExtensions=" + fileExtensions +
                '}';
    }
}
