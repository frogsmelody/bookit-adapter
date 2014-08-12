package com.derbysoft.bookit.common.model;


public class HotelCondition {
    private String status;
    private String channelHotelCode;
    private String providerHotelCode;

    public String getChannelHotelCode() {
        return channelHotelCode;
    }

    public void setChannelHotelCode(String channelHotelCode) {
        this.channelHotelCode = channelHotelCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProviderHotelCode() {
        return providerHotelCode;
    }

    public void setProviderHotelCode(String providerHotelCode) {
        this.providerHotelCode = providerHotelCode;
    }
}
