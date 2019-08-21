package org.dupply.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DScanner {

    private static final Logger logger = LoggerFactory.getLogger(DScanner.class);

    public static Map<FileDesc, List<File>> find(File scanRoot, ScanType scanType, Predicate<File> fileFilter,
                                                 Consumer<Long> scanProgressNotifier, Consumer<Map<FileDesc, List<File>>> onFinish) {
        long cnt = 0;
        Map<FileDesc, List<File>> processed = new HashMap<>();

        if (scanRoot.exists() && scanRoot.isDirectory()) {
            cnt += scanDir(scanRoot, scanType, processed, cnt, fileFilter, scanProgressNotifier);
        }

        Iterator<Map.Entry<FileDesc, List<File>>> iterator = processed.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<FileDesc, List<File>> item = iterator.next();
            if (item.getValue() == null || item.getValue().isEmpty() || item.getValue().size() == 1) {
                iterator.remove();
            } else {
                setShortestFileNameToFileDesc(item);
            }
        }
        int found = processed.size();
        logger.info("Finished, scanned [{}] files, found[{}] duplicates", cnt, found);
        onFinish.accept(processed);
        return processed;
    }

    private static void setShortestFileNameToFileDesc(Map.Entry<FileDesc, List<File>> entry) {
        if (entry.getValue() != null && !entry.getValue().isEmpty()) {
            String shortestName = entry.getValue().get(0).getName();
            for (File file : entry.getValue()) {
                if (file.getName().length() < shortestName.length()) {
                    shortestName =  file.getName();
                }
            }
            entry.getKey().setName(shortestName);
        }
    }

    static long scanDir(File dir, ScanType scanType, Map<FileDesc, List<File>> processed, long alreadyProcessed, Predicate<File> fileFilter, Consumer<Long> scanProgressNotifier) {
        long res = 0;
        if (dir.listFiles() != null) {
            for (File inner : dir.listFiles()) {
                if (inner.isDirectory()) {
                    res += scanDir(inner, scanType, processed, alreadyProcessed+res, fileFilter, scanProgressNotifier);
                } else if (fileFilter.test(inner)) {
                    FileDesc fileDesc = byScanType(scanType, inner);
                    if (processed.containsKey(fileDesc)) {
                        processed.get(fileDesc).add(inner);
                    } else {
                        List<File> files = new LinkedList<>();
                        files.add(inner);
                        processed.put(fileDesc, files);
                    }
                    res++;
                    scanProgressNotifier.accept(res + alreadyProcessed);
                }
            }
        }
        return res;
    }

    private static String getDigest(File inner) {
        try {
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

    public static FileDesc byScanType(ScanType scanType, File file) {
        if (ScanType.SIZE_AND_HASH.equals(scanType)) {
            return FileDesc.byLengthAndDigest(file.length(), getDigest(file));

        } else if (ScanType.NAME_AND_SIZE_AND_HASH.equals(scanType)) {
            return FileDesc.byNameAndLengthAndDigest(file.getName(), file.length(), getDigest(file));
        } else {
            return FileDesc.byNameAndLength(file.getName(), file.length());
        }
    }

}
