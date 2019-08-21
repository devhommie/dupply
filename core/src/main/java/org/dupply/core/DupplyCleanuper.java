package org.dupply.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class DupplyCleanuper {

    private static final Logger logger = LoggerFactory.getLogger(DupplyCleanuper.class);

    private String reportLocation = "report.csv";
    private String reportEncoding = "utf-8";
    private String carantineLocation = "carantine";
    private boolean keepDuplicates = false;
    private boolean keepEmptyFolders = false;
    private int notifyOn = 150;

    public DupplyCleanuper(Properties props) {
        if (props.containsKey("report.location")) {
            this.reportLocation = props.getProperty("report.location");
        }

        if (props.containsKey("report.encoding")) {
            this.reportEncoding = props.getProperty("report.encoding");
        }

        if (props.containsKey("carantine.location")) {
            this.carantineLocation = props.getProperty("carantine.location");
        }

        if (props.containsKey("keep.duplicates")) {
            this.keepDuplicates = Boolean.valueOf(props.getProperty("keep.duplicates"));
        }

        if (props.containsKey("keep.empty.folders")) {
            this.keepEmptyFolders = Boolean.valueOf(props.getProperty("keep.empty.folders"));
        }

        if (props.containsKey("notify.counter")) {
            this.notifyOn = Integer.parseInt(props.getProperty("notify.counter"));
        }

        logger.debug("Created cleanuper instance: reportLocation[{}], reportEncoding[{}], " +
                "carantineLocation[{}], keepDuplicates[{}], keepEmptyFolders[{}], notifyOn[{}]", reportLocation,
                reportEncoding, carantineLocation, keepDuplicates, keepEmptyFolders, notifyOn);
    }

    public void run() {
        File carantineDir = new File(carantineLocation);
        carantineDir.mkdirs();

        File reportFile = new File(reportLocation);
        int success = 0;
        int failed = 0;
        int processed = 0;
        if (reportFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(reportFile), reportEncoding))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if (processLine(line)) {
                        success++;
                    } else {
                        failed++;
                    }
                    processed++;
                    if (processed%notifyOn == 0) {
                        logger.debug("Processed [{}] lines...", processed);
                    }

                }
            } catch (Exception e) {
                logger.error("Cannot read report file ["+reportLocation+"]", e);
            }

            logger.info("Processing finished: success[{}], failed[{}]", success, failed);
        }
    }

    boolean processLine(String line) {
        try {
            String[] tokens = line.split("\\s*,\\s*");
            if (tokens.length >= 5) {
                int number = Integer.parseInt(tokens[0]);
                String filename = tokens[1];
                int totalFiles = Integer.parseInt(tokens[2]);
                String firstFile = tokens[3];

                int moved = 0;
                int failed = 0;
                for (int i = 4; i<tokens.length; i++) {
                    if (moveFile(tokens[i])) {
                        moved++;
                    } else {
                        failed++;
                    }
                }
                logger.debug("#[{}], file[{}], totalFiles[{}], moved[{}], failed[{}], leaveUntouched[{}]", number, filename, totalFiles, moved, failed, firstFile);
                return true;
            } else {
                logger.error("Cannot process line ["+line+"], insufficient tokens count["+tokens.length+"]");
                return false;
            }
        } catch (Throwable e) {
            logger.error("Cannot process line ["+line+"]", e);
        }
        return false;
    }

    boolean moveFile(String token) {
        try {
            File file = new File(token);
            if (file.exists()) {
                File carantineFile = getUnexistingCarantineFile(file.getName());
                File carantineSourceFile = new File(carantineFile.getParentFile(), carantineFile.getName()+".txt");

                if (keepDuplicates) {
                    Files.copy(file.toPath(), carantineFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    Files.move(file.toPath(), carantineFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    if (!keepEmptyFolders) {
                        File folder = file.getParentFile();
                        if (folder.listFiles() != null && folder.listFiles().length == 0) {
                            logger.info("Try to delete empty folder [{}]", folder);
                            folder.delete();
                        }
                    }
                }

                try (Writer writer = new FileWriter(carantineSourceFile)) {
                    writer.write(file.getAbsolutePath());
                }
                return true;
            } else {
                logger.error("File ["+token+"] not found");
                return false;
            }

        } catch (Throwable e) {
            logger.error("Cannot move file ["+token+"]", e);
        }
        return false;
    }

    File getUnexistingCarantineFile(String name) {
        File result = new File(carantineLocation, name);
        int counter = 1;
        while (result.exists()) {
            result = new File(carantineLocation, injectSuffix(name, "("+counter+")"));
            counter++;
        }
        return result;
    }

    String injectSuffix(String fileName, String suffix) {
        if (fileName == null) {
            return null;
        }
        int dotPos = fileName.lastIndexOf(".");
        if (dotPos > 0) {
            return fileName.substring(0, dotPos) + suffix + fileName.substring(dotPos);
        }
        return fileName + suffix;
    }

}
