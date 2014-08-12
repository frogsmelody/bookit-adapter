package com.derbysoft.bookit.push.commons.models;

/**
 * Author: Jason Wu
 * Date  : 2013-09-23
 */
public class RateChangePushLogCondition {
    private String date;
    private Long id;
    private String checkIn;
    private String hasError;
    private String hotelCode;
    private String ratePlanCode;
    private String roomTypeCode;
    private String changeType;
    private String start;
    private String taskId;
    private String error;
    private Long minSpentTime;
    private Long maxSpentTime;

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getHasError() {
        return hasError;
    }

    public void setHasError(String hasError) {
        this.hasError = hasError;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getHotelCode() {
        return hotelCode;
    }

    public void setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
    }

    public String getRatePlanCode() {
        return ratePlanCode;
    }

    public void setRatePlanCode(String ratePlanCode) {
        this.ratePlanCode = ratePlanCode;
    }

    public String getRoomTypeCode() {
        return roomTypeCode;
    }

    public void setRoomTypeCode(String roomTypeCode) {
        this.roomTypeCode = roomTypeCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Long getMinSpentTime() {
        return minSpentTime;
    }

    public void setMinSpentTime(Long minSpentTime) {
        this.minSpentTime = minSpentTime;
    }

    public Long getMaxSpentTime() {
        return maxSpentTime;
    }

    public void setMaxSpentTime(Long maxSpentTime) {
        this.maxSpentTime = maxSpentTime;
    }
}
