package com.demo.commons.logger;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 日志管理的统一入口
 *
 */
public class APILog {

    private static final Logger logger = LogManager.getLogger(APILog.class);

    private static final Logger error = LogManager.getLogger("error");

    private static final Logger timemonitor = LogManager.getLogger("timemonitor");

    public static boolean isDebugEnabled() {
        return true;
    }

    public static boolean isInfoEnabled() {
        return true;
    }

    public static boolean isWarnEnabled() {
        return true;
    }

    public static void debug(String log) {
        logger.debug(log);
    }

    public static void info(String log) {
        logger.info(log);
    }

    public static void error(String log, Throwable e) {
        error.error(log, e);
    }

    public static void warn(String log, Throwable e) {
        logger.warn(log, e);
    }

    /**
     * 用于记录请求日志
     *
     * @param log
     */
    public static void accessLog(String log) {
        timemonitor.info(log);
    }
}
