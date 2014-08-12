package com.derbysoft.bookit.push.commons.models;

/**
 * Author: Jason Wu
 * Date  : 2013-11-22
 */
public final class LosRateChangePushRQPair {
    private String checkIn;
    private String rateChangePushRequest;

    private LosRateChangePushRQPair(String checkIn, String rateChangePushRequest) {
        this.checkIn = checkIn;
        this.rateChangePushRequest = rateChangePushRequest;

    }

    public static LosRateChangePushRQPair build(String checkIn, String rateChangePushRequest) {
        return new LosRateChangePushRQPair(checkIn, rateChangePushRequest);
    }

    public String getCheckIn() {
        return checkIn;
    }

    public String getRateChangePushRequest() {
        return rateChangePushRequest;
    }
}
