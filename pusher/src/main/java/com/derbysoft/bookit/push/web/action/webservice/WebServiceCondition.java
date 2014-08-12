package com.derbysoft.bookit.push.web.action.webservice;

/**
 * Author: Jason Wu
 * Date  : 2013-11-18
 */
public class WebServiceCondition {
    private String level;
    private String hotelCodes;
    private String keys;
    private String start;
    private String end;
    private String timestamp;
    private String type;
    private String losList;
    private String ratePlans;
    private String roomTypes;

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getHotelCodes() {
        return hotelCodes;
    }

    public void setHotelCodes(String hotelCodes) {
        this.hotelCodes = hotelCodes;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLosList() {
        return losList;
    }

    public void setLosList(String losList) {
        this.losList = losList;
    }

    public String getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(String roomTypes) {
        this.roomTypes = roomTypes;
    }

    public String getRatePlans() {
        return ratePlans;
    }

    public void setRatePlans(String ratePlans) {
        this.ratePlans = ratePlans;
    }
}
