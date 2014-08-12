package com.derbysoft.bookit.common.model;

/**
 * Author: Jason Wu
 * Date  : 2013-09-22
 */
public class LosRateChangeCondition {
    private String id;
    private String hotelCode;
    private String ratePlanCode;
    private String roomTypeCode;
    private String checkIn;
    private String loses;
    private String haveRates;


    public String getHaveRates() {
        return haveRates;
    }

    public void setHaveRates(String haveRates) {
        this.haveRates = haveRates;
    }

    public String getLoses() {
        return loses;
    }

    public void setLoses(String loses) {
        this.loses = loses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomTypeCode() {
        return roomTypeCode;
    }

    public void setRoomTypeCode(String roomTypeCode) {
        this.roomTypeCode = roomTypeCode;
    }

    public String getHotelCode() {
        return hotelCode;
    }

    public void setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getRatePlanCode() {
        return ratePlanCode;
    }

    public void setRatePlanCode(String ratePlanCode) {
        this.ratePlanCode = ratePlanCode;
    }
}
