package com.recode.framework.automation.ui.utils;
import org.apache.logging.log4j.LogManager;

import java.io.File;

public class Logger {
    private static final String logFileDir = Utils.getAutomationProperties().getProperty("LogFileDir");
    public static final org.apache.logging.log4j.Logger instance = LogManager.getLogger(Logger.class);

    public static String logLocation(String browserName){
        String logPath = System.getProperty("user.dir") + logFileDir + File.separator + browserName;
        File logFolder = new File(logPath);
        if (!logFolder.exists()) {
            logFolder.mkdirs();
        }
        return logPath;
    }
}
