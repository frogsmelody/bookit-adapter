package com.derbysoft.bookit.push.job.support;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author: Jason Wu
 * Date  : 2014-03-14
 */
public abstract class RunningTaskManager {

    private static final AtomicBoolean IS_LOS_RATE_PUSH_TASK_KEY_GENERATE_JOB_RUNNING = new AtomicBoolean(false);
    private static final AtomicBoolean IS_LOS_RATE_PUSH_JOB_RUNNING = new AtomicBoolean(false);

    public static void generateLosRateKeyStart() {
        IS_LOS_RATE_PUSH_TASK_KEY_GENERATE_JOB_RUNNING.set(true);
    }

    public static void generateLosRateKeyDone() {
        IS_LOS_RATE_PUSH_TASK_KEY_GENERATE_JOB_RUNNING.set(false);
    }

    public static boolean generateLosRateKeyIsRunning() {
        return IS_LOS_RATE_PUSH_TASK_KEY_GENERATE_JOB_RUNNING.get();
    }

    public static void pushLOSRateJobStart() {
        IS_LOS_RATE_PUSH_JOB_RUNNING.set(true);
    }

    public static void pushLOSRateJobDone() {
        IS_LOS_RATE_PUSH_JOB_RUNNING.set(false);
    }

    public static boolean pushLOSRateJobIsRunning() {
        return IS_LOS_RATE_PUSH_JOB_RUNNING.get();
    }
}
