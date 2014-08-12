package com.derbysoft.bookit.push.commons.models;

import com.derbysoft.bookit.push.domain.RateChangePushLog;
import com.derbysoft.common.paginater.Paginater;

/**
 * Author: Jason Wu
 * Date  : 2013-11-27
 */
public class RateChangePushLogDetailPair {
    private RateChangePushLog rateChangePushLog;
    private Paginater paginater;

    public RateChangePushLogDetailPair() {
    }

    public RateChangePushLogDetailPair(RateChangePushLog rateChangePushLog, Paginater paginater) {
        this.rateChangePushLog = rateChangePushLog;
        this.paginater = paginater;
    }

    public RateChangePushLog getRateChangePushLog() {
        return rateChangePushLog;
    }

    public void setRateChangePushLog(RateChangePushLog rateChangePushLog) {
        this.rateChangePushLog = rateChangePushLog;
    }

    public Paginater getPaginater() {
        return paginater;
    }

    public void setPaginater(Paginater paginater) {
        this.paginater = paginater;
    }
}
