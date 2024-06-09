package io.links.server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class LoggingUtils {
    Logger logger;

    public void logStackTraceAndMessage(Exception e) {
        assert logger != null;

        var stackTrace = Arrays.toString(e.getStackTrace());
        logger.error(stackTrace);
        logger.error(e.getMessage());
    }

    public void setClassname(Class<?> _class) {
        this.logger = LoggerFactory.getLogger(_class);
    }
}
