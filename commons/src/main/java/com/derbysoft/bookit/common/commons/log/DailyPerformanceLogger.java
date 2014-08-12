package com.derbysoft.bookit.common.commons.log;

import org.apache.log4j.Logger;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Author: Jason Wu
 * Date  : 2013-10-17
 */
public class DailyPerformanceLogger {
    private static final Logger LOGGER = Logger.getLogger(DailyPerformanceLogger.class);
    private static final int ONE_M = 1024;
    private static final long TEN_M = 10 * ONE_M;

    private Executor executor;
    private Date initTime;
    private List<LogBody> logBodies = new ArrayList<LogBody>();


    public DailyPerformanceLogger(Executor executor) {
        this.executor = executor;
        initTime = new Date();
    }

    public void addLogBody(LogBody logBody) {
        logBodies.add(logBody);
    }

    public void printLog() {
        logBodies.add(LogBody.of(LogBody.PROCESS_DURATION, String.valueOf(System.currentTimeMillis() - initTime.getTime())));
        logBodies.addAll(getNowServerStatus());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                PerformanceDailyAppender.setStandardLogStartTime(initTime);
                LOGGER.info(flattenLogBodies(logBodies));
            }
        });
    }

    private static String flattenLogBodies(List<LogBody> logBodies) {
        StringBuilder builder = new StringBuilder();
        for (LogBody logBody : logBodies) {
            builder.append("<").append(logBody.getKey()).append("=").append(logBody.getValue()).append(">");
            builder.append(" ");
        }
        return builder.toString();
    }

    private static Collection<? extends LogBody> getNowServerStatus() {
        List<LogBody> serverStatus = new ArrayList<LogBody>();
        serverStatus.add(LogBody.of(LogBody.SYSTEM_LOAD_STATUS, checkCpu()));
        serverStatus.add(LogBody.of(LogBody.MEMORY_STATUS, checkMemory()));
        return serverStatus;
    }

    private static String checkMemory() {
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

    private static String checkCpu() {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        double load;
        try {
            load = operatingSystemMXBean.getSystemLoadAverage();
        } catch (Exception e) {
            load = -1;
        }
        return String.valueOf(load);
    }

    public static Logger getLogger(String clazzName) {
        return LOGGER;
    }
}
