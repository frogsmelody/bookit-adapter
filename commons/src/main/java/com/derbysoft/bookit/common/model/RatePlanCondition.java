package com.derbysoft.bookit.common.model;

/**
 * Author: Jason Wu
 * Date  : 2013-09-22
 */
public class RatePlanCondition {
    private String ratePlanCode;
    private String ratePlanName;

    public String getRatePlanName() {
        return ratePlanName;
    }
    public void setRatePlanName(String ratePlanName) {
        this.ratePlanName = ratePlanName;
    }

    public String getRatePlanCode() {
        return ratePlanCode;
    }

    public void setRatePlanCode(String ratePlanCode) {
        this.ratePlanCode = ratePlanCode;
    }
}
