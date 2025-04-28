package org.unibl.etf.clientapp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomLogger {
    private final Logger logger;

    private CustomLogger(Class<?> aClass) {
        this.logger = LoggerFactory.getLogger(aClass);
    }

    public static CustomLogger getInstance(Class<?> aClass) {
        return new CustomLogger(aClass);
    }

    public void info(String message, Object... args) {
        logger.info(message, args);
    }

    public void debug(String message, Object... args) {
        logger.debug(message, args);
    }

    public void warn(String message, Object... args) {
        logger.warn(message, args);
    }

    public void error(String message, Object... args) {
        logger.error(message, args);
    }

    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
}
