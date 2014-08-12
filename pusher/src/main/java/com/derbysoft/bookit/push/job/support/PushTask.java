package com.derbysoft.bookit.push.job.support;

public interface PushTask {
    String getKey();

    void cancel();

    boolean isCancelled();

    void pushRateChangeDone();

    void pushInventoryChangeDone();

    void pushInventoryChangeStart();

    void pushRateChangeStart();

    void pushDone();
}
