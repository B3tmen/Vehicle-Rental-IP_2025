package org.unibl.etf.carrentalbackend.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomLogger {
    private static final Logger logger = Logger.getLogger(CustomLogger.class.getName());
    private static CustomLogger instance;

    private CustomLogger() {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);

        logger.addHandler(consoleHandler);
        logger.setLevel(Level.ALL);
    }

    public static CustomLogger getInstance() {
        if (instance == null) {
            instance = new CustomLogger();
        }
        return instance;
    }

    // Method to log info messages
    public void info(String message) {
        logger.log(Level.INFO, message);
    }

    // Method to log warning messages
    public void warning(String message) {
        logger.log(Level.WARNING, message);
    }

    // Method to log error messages
    public void error(String message) {
        logger.log(Level.SEVERE, message);
    }
}
