package com.derbysoft.bookit.push.commons.factory;

import com.derbysoft.bookit.common.commons.concurrent.DynamicThreadPoolExecutor;
import com.derbysoft.bookit.push.commons.EnvironmentUtils;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Author: Jason Wu
 * Date  : 2013-09-30
 */
public abstract class RatePushThreadPoolFactory {
    private static final int CAPACITY = 500;
    private static volatile DynamicThreadPoolExecutor executor = null;

    public static DynamicThreadPoolExecutor getExecutor() {
        if (executor != null) {
            return executor;
        }
        int corePoolSize = Integer.parseInt(EnvironmentUtils.get("los_rate_push_thread_count"));
        executor = new DynamicThreadPoolExecutor(new LinkedBlockingQueue(CAPACITY), corePoolSize);
        return executor;
    }
}
