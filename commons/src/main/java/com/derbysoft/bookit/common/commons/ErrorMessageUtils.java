package com.derbysoft.bookit.common.commons;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * Created by: jason
 * Date: 2012-08-29
 */
public abstract class ErrorMessageUtils {
    private static final int STACK_DEEP = 20;

    public static String buildErrorMessage(Throwable ex) {
        StringBuilder builder = new StringBuilder();
        String[] rootCauseStackTrace = ExceptionUtils.getRootCauseStackTrace(ex);
        int counter = 0;
        for (String stackTrace : rootCauseStackTrace) {
            counter ++;
            if (counter > STACK_DEEP) {
                break;
            }
            builder.append(stackTrace).append("\n");
        }
        return builder.toString();
    }

    public static String buildSimpleErrorMessage(Throwable ex) {
        String errorMsg = ex.getMessage();
        if (StringUtils.isBlank(errorMsg)) {
            errorMsg = ex.getClass().getName();
        }
        return errorMsg;
    }
}
