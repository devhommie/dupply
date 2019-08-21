package org.dupply.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.*;
import java.util.stream.Collectors;

@Deprecated
public class DupplyScanner {

    private static final Logger logger = LoggerFactory.getLogger(DupplyScanner.class);

    private String reportLocation = "report.csv";
    private String reportEncoding = "utf-8";

    private String scanRoot = "/";
    private List<String> fileExtensions = Arrays.asList("*");
    private boolean useMd5Hashing = true;
    private int notifyOn = 150;

    public DupplyScanner(Properties props) {
        if (props.containsKey("report.location")) {
            this.reportLocation = props.getProperty("report.location");
        }

        if (props.containsKey("scan.root")) {
            this.scanRoot = props.getProperty("scan.root");
        }

        if (props.containsKey("report.encoding")) {
            this.reportEncoding = props.getProperty("report.encoding");
        }

        if (props.containsKey("md5.hashing")) {
            this.useMd5Hashing = Boolean.valueOf(props.getProperty("md5.hashing"));
        }

        if (props.containsKey("notify.counter")) {
            this.notifyOn = Integer.parseInt(props.getProperty("notify.counter"));
        }

        if (props.containsKey("file.extensions")) {
            String[] extensions = props.getProperty("file.extensions").split(",");
            this.fileExtensions = new ArrayList<>();
            for (String ext : extensions) {
                if (ext!=null && ext.trim().length() > 0) {
                    this.fileExtensions.add(ext.trim().toLowerCase());
                }
            }
        }

        logger.debug("Created scanner instance: reportLocation[{}], scanRoot[{}], reportEncoding[{}], " +
                "fileExtensions[{}], useMd5Hashing[{}], notifyOn[{}]", reportLocation, scanRoot,
                reportEncoding, fileExtensions, useMd5Hashing, notifyOn);
    }

    public void run() {
        long cnt = 0;
        Map<FileDesc, List<File>> processed = new HashMap<>();
        File reportFile = new File(reportLocation);

        File root = new File(scanRoot);
        if (root.exists() && root.isDirectory()) {
            cnt += scanDir(root, processed, cnt);
        }
        List<Map.Entry<FileDesc, List<File>>> duplicates = processed.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .collect(Collectors.toList());
        int count = duplicates.size();
        logger.info("Finished, scanned [{}] files, found[{}] duplicates", cnt, count);
        flushReport(reportFile, duplicates);
    }

    private void flushReport(File reportFile, List<Map.Entry<FileDesc, List<File>>> duplicated) {
        reportFile.delete();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(reportFile), reportEncoding))) {
            int num = 1;
            for (Map.Entry<FileDesc, List<File>> entry : duplicated) {
                writer.write(createLine(num, entry.getKey(), entry.getValue()));
                writer.newLine();
                num++;
            }
        } catch (Exception e) {
            logger.error("Cannot create report file ["+reportFile+"]", e);
        }
    }

    private String createLine(int num, FileDesc key, List<File> value) {
        StringBuilder sb = new StringBuilder();
        sb.append(num);
        sb.append(", ");
        sb.append(key.getName());
        sb.append(", ");
        sb.append(value.size());
        for (File file : value) {
            sb.append(", ");
            sb.append(file.getAbsolutePath());
        }
        return sb.toString();
    }

    long scanDir(File dir, Map<FileDesc, List<File>> processed, long alreadyProcessed) {
        long res = 0;
        if (dir.listFiles() != null) {
            for (File inner : dir.listFiles()) {
                if (inner.isDirectory()) {
                    res += scanDir(inner, processed, alreadyProcessed+res);
                } else if (isTargetFile(inner)) {
                    FileDesc fileDesc = FileDesc.byNameAndLengthAndDigest(inner.getName(), inner.length(), getDigest(inner));
                    if (processed.containsKey(fileDesc)) {
                        processed.get(fileDesc).add(inner);
                    } else {
                        List<File> files = new LinkedList<>();
                        files.add(inner);
                        processed.put(fileDesc, files);
                    }
                    res++;
                    long overall = res + alreadyProcessed;
                    if (overall%notifyOn == 0) {
                        logger.debug("Processed [{}] files, scan directory[{}]...", overall, dir);
                    }
                }
            }
        }
        return res;
    }

    private String getDigest(File inner) {
        try {
            if (!useMd5Hashing) {
                return "";
            }
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[2056];
            try (DigestInputStream dis = new DigestInputStream(new FileInputStream(inner), md)) {
                int numRead;

                do {
                    numRead = dis.read(buffer);
                    if (numRead > 0) {
                        md.update(buffer, 0, numRead);
                    }
                } while (numRead != -1);
            }
            byte[] digest = md.digest();
            return new String(digest);

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isTargetFile(File inner) {
        if (inner.isFile()) {
            if (fileExtensions.contains("*")) {
                return true;
            }

            String filename = inner.getName().toLowerCase();
            for (String extension : fileExtensions) {
                if (filename.endsWith(extension)) {
                    return true;
                }
            }
        }
        return false;
    }

}
