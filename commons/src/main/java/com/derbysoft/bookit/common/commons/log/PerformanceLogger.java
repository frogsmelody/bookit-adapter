package com.derbysoft.bookit.common.commons.log;

import org.apache.log4j.Logger;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * Created by: jason
 * Date: 2012-06-12
 */
public final class PerformanceLogger {
    private PerformanceLogger() {
    }

    private static final Logger LOGGER = Logger.getLogger(PerformanceLogger.class);
    private static final ThreadLocal<PerformanceLogger> PERFORMANCE_LOGGER = new InheritableThreadLocal<PerformanceLogger>();
    private Date time = new Date();
    private Set<LogBody> logBodies = new LinkedHashSet<LogBody>();
    public static final int ONE_M = 1024;
    private static final long TEN_M = 10 * ONE_M;
    private static Executor executor;

    public static void init(Executor executor) {
        PerformanceLogger.executor = executor;
        PERFORMANCE_LOGGER.set(new PerformanceLogger());
    }

    public static void append(String key, String value) {
        PERFORMANCE_LOGGER.get().logBodies.add(LogBody.of(key, value));
    }

    public static void save() {
        final PerformanceLogger log = PERFORMANCE_LOGGER.get();

        PERFORMANCE_LOGGER.get().logBodies.addAll(createNowServerStatus());

        String totalElapsedTime = String.valueOf(System.currentTimeMillis() - log.time.getTime());
        PERFORMANCE_LOGGER.get().logBodies.add(LogBody.of(LogBody.PROCESS_DURATION, totalElapsedTime));

        PERFORMANCE_LOGGER.remove();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                PerformanceDailyAppender.setStandardLogStartTime(log.time);
                LOGGER.info(flattenLogBodies(log));
            }
        });
    }

    private static Collection<? extends LogBody> createNowServerStatus() {
        List<LogBody> serverStatus = new ArrayList<LogBody>();
        serverStatus.add(LogBody.of(LogBody.SYSTEM_LOAD_STATUS, checkCpu()));
        serverStatus.add(LogBody.of(LogBody.MEMORY_STATUS, checkMemory()));
        return serverStatus;
    }

    private static String flattenLogBodies(PerformanceLogger log) {
        StringBuilder builder = new StringBuilder();
        for (LogBody logBody : log.logBodies) {
            builder.append("<").append(logBody.getKey()).append("=").append(logBody.getValue()).append(">");
            builder.append(" ");
        }
        return builder.toString();
    }

    public static String checkMemory() {
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

    public static String checkCpu() {
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

    public static Logger getLogger(String clazzName) {
        return LOGGER;
    }
}
