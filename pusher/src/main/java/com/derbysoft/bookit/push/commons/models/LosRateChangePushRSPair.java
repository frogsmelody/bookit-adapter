package com.derbysoft.bookit.push.commons.models;

/**
 * Author: Jason Wu
 * Date  : 2013-11-22
 */
public final class LosRateChangePushRSPair {
    private LosRateChangePushRQPair requestPair;
    private String response;
    private boolean succeed;

    private LosRateChangePushRSPair(LosRateChangePushRQPair requestPair, String response, boolean succeed) {
        this.requestPair = requestPair;
        this.response = response;
        this.succeed = succeed;
    }

    public LosRateChangePushRQPair getRequestPair() {
        return requestPair;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public static LosRateChangePushRSPair build(LosRateChangePushRQPair request, String response, boolean succeed) {
        return new LosRateChangePushRSPair(request, response, succeed);
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
