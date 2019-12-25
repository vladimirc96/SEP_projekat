package com.sep.paypalservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logging {

    protected final Logger logger;

    public Logging(Object object) {
        logger = LoggerFactory.getLogger(object.getClass());
    }

    public void logInfo(String message) {
        try {
            logger.info(message);
        } catch (Exception e) {
            System.out.println("Logger does not work");
        }
    }

    public void logError(String message) {
        try {
            logger.error(message);
        } catch (Exception e) {
            System.out.println("Logger does not work");
        }
    }

    public void logWarning(String message) {
        try {
            logger.warn(message);
        } catch (Exception e) {
            System.out.println("Logger does not work");
        }
    }
}