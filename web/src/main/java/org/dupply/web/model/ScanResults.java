package org.dupply.web.model;

import org.dupply.core.FileDesc;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ScanResults {

    private boolean running;
    private long processed;
    private Map<FileDesc, List<File>> result;

    public ScanResults() {}

    public ScanResults(boolean running, long processed, Map<FileDesc, List<File>> result) {
        this.running = running;
        this.processed = processed;
        this.result = result;
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

    @Override
    public String toString() {
        return "ScanResults{" +
                "running=" + running +
                ", processed=" + processed +
                ", result=" + result +
                '}';
    }
}
