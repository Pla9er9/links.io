package io.links.server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class LoggingUtils {
    private final Logger logger;

    private LoggingUtils(Class<?> _class) {
        this.logger = LoggerFactory.getLogger(_class);
    }

    public static LoggingUtils getLoggingUtils(Class<?> clazz) {
        return new LoggingUtils(clazz);
    }

    public void logStackTraceAndMessage(Exception e) {
        var stackTrace = Arrays.toString(e.getStackTrace());
        logger.error(stackTrace);
        logger.error(e.getMessage());
    }
}
