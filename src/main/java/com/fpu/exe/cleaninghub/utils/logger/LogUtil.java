package com.fpu.exe.cleaninghub.utils.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class LogUtil {
    private static final Logger LOGGER = LogManager.getLogger(LogUtil.class);

    public static void logInfo(Object message) {
        LOGGER.info(message);
    }

    public static void logWarning(Object message) {
        LOGGER.warn(message);
    }

    public static void logError(Object message) {
        LOGGER.error(message);
    }

    public static void logDebug(Object message) {
        LOGGER.debug(message);
    }

    public static void logTrace(Object message) {
        LOGGER.trace(message);
    }

    public static void logWarn(Object message) {
        LOGGER.warn(message);
    }
}
