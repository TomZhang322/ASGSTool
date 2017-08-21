package com.adcc.tool.msgCenter.util;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;

/**
 * 日志处理类
 */
public final class LogUtil {

    private static final Logger logger = Logger.getLogger(Log.class);

    public static void info(String log) {
        logger.info(log);
    }

    public static void debug(String log) {
        logger.debug(log);
    }

    public static void debug(String log, Exception e) {
        logger.debug(log, e);
    }

    public static void error(String log) {
        logger.error(log);
    }

    public static void error(String log, Exception e) {
        logger.error(log,e);
    }
}
