package com.derbysoft.bookit.push.web.action.task;

/**
 * Author: Jason Wu
 * Date  : 2014-03-25
 */
public class ChangedRateCountCondition {
    private String hotelCode;
    private String key;
    private String executeStatus;
    private String rateChangeStatus;
    private String inventoryChangeStatus;

    public String getRateChangeStatus() {
        return rateChangeStatus;
    }

    public void setRateChangeStatus(String rateChangeStatus) {
        this.rateChangeStatus = rateChangeStatus;
    }

    public String getInventoryChangeStatus() {
        return inventoryChangeStatus;
    }

    public void setInventoryChangeStatus(String inventoryChangeStatus) {
        this.inventoryChangeStatus = inventoryChangeStatus;
    }

    public String getHotelCode() {
        return hotelCode;
    }

    public void setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(String executeStatus) {
        this.executeStatus = executeStatus;
    }
}
