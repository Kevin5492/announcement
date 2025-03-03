package com.kevin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
	private static final Logger log = LoggerFactory.getLogger(LogUtil.class);

    public static void logError(Exception e) {
        StackTraceElement element = Thread.currentThread().getStackTrace()[2]; // 取得呼叫者
        log.error("{}#{} 出現錯誤: {}", element.getClassName(), element.getMethodName(), e.getMessage(), e);
    }
}
