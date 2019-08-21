package org.dupply.cli;

import org.dupply.core.DupplyCleanuper;
import org.dupply.core.DupplyScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class Launcher {

    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    public static void execute(String command, String cfgLocation, String cfgEncoding) throws Exception {
        logger.info("Executing command[{}], cfgLocation[{}], cfgEncoding[{}]", command, cfgLocation, cfgEncoding);

        Properties properties = new Properties();
        try (Reader reader = new InputStreamReader(new FileInputStream(cfgLocation), cfgEncoding)) {
            properties.load(reader);
        }

        logger.debug("Properties loaded are [{}]", properties);

        if ("scan".equalsIgnoreCase(command)) {
            new DupplyScanner(properties).run();
        } else if ("scan-cleanup".equalsIgnoreCase(command)) {
            new DupplyScanner(properties).run();
            new DupplyCleanuper(properties).run();
        } else if ("cleanup".equalsIgnoreCase(command)) {
            new DupplyCleanuper(properties).run();
        } else {
            throw new IllegalArgumentException("Unsupported command ["+command+"]");
        }

    }

    public static void main(String[] args) throws Exception {
        String command = "scan";
        String cfgLocation = "dupply.properties";
        String cfgEncoding = "utf-8";
        if (args.length > 1) {
            command = args[0];
            cfgLocation = args[1];
            if (args.length > 2) {
                cfgEncoding = args[2];
            }
        }

        execute(command, cfgLocation, cfgEncoding);
    }

}
