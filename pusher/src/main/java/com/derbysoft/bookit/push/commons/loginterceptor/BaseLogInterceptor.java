package com.derbysoft.bookit.push.commons.loginterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

/**
 * Author: Jason Wu
 * Date  : 2014-04-11
 */
public abstract class BaseLogInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseLogInterceptor.class);

    private static final int ONE_M = 1024;
    private static final long TEN_M = 10 * ONE_M;

    protected static String checkMemory() {
        Runtime runtime = Runtime.getRuntime();
        long freeMemory = runtime.freeMemory();
        long totalMemory = runtime.totalMemory();
        long maxMemory = runtime.maxMemory();
        boolean ok = (maxMemory - (totalMemory - freeMemory) > TEN_M);
        String msg = "max:" + (maxMemory / ONE_M / ONE_M) + "M,total:"
                + (totalMemory / ONE_M / ONE_M) + "M,used:" + ((totalMemory / ONE_M / ONE_M) - (freeMemory / ONE_M / ONE_M))
                + "M,free:" + (freeMemory / ONE_M / ONE_M) + "M";

        return (ok ? "OK: " : "WARN: ") + msg;
    }
    protected static String checkCpu() {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        double load;
        try {
            load = operatingSystemMXBean.getSystemLoadAverage();
        } catch (Exception e) {
            load = -1;
            LOGGER.error(e.getMessage(), e);
        }
        return String.valueOf(load);
    }
}
