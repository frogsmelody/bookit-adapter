package com.derbysoft.bookit.push.domain;


import com.derbysoft.bookit.common.commons.DateTimeUtils;
import com.derbysoft.common.domain.BasePersistenceSupport;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class RateChangePullLog extends BasePersistenceSupport {

    @Column(unique = true, length = 100)
    private String echoToken;

    @Column(nullable = false)
    private String hiltonHotelCode;

    @Column(nullable = false)
    private String start;

    @Column(nullable = false)
    private String end;

    private String retrieveTimestamp;

    //used dmx page
    @Transient
    private String retrieveDateTime;

    private String loses;

    private String ratePlans;

    private Long timeSpan;

    @Column(columnDefinition = "boolean default false")
    private Boolean error = false;

    @Column(columnDefinition = "longtext")
    private String errorMessage;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "rateChangePullLog")
    private List<RateChangePullLogDetail> rateChangePullLogDetails = new ArrayList<RateChangePullLogDetail>();

    public String getRetrieveDateTime() {
        if (StringUtils.isBlank(retrieveTimestamp)) {
            return null;
        }
        Date date = new Date(Long.valueOf(retrieveTimestamp));
        return DateTimeUtils.formatDate(date, DateTimeUtils.DATE_TIME_FORMAT);
    }

    public String getRetrieveTimestamp() {
        return retrieveTimestamp;
    }

    public void setRetrieveTimestamp(String retrieveTimestamp) {
        this.retrieveTimestamp = retrieveTimestamp;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getLoses() {
        return loses;
    }

    public void setLoses(String loses) {
        this.loses = loses;
    }

    public String getRatePlans() {
        return ratePlans;
    }

    public void setRatePlans(String ratePlans) {
        this.ratePlans = ratePlans;
    }

    public String getHiltonHotelCode() {
        return hiltonHotelCode;
    }

    public void setHiltonHotelCode(String hiltonHotelCode) {
        this.hiltonHotelCode = hiltonHotelCode;
    }

    public String getEchoToken() {
        return echoToken;
    }

    public void setEchoToken(String echoToken) {
        this.echoToken = echoToken;
    }

    public Long getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(Long timeSpan) {
        this.timeSpan = timeSpan;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<RateChangePullLogDetail> getRateChangePullLogDetails() {
        return rateChangePullLogDetails;
    }

    public void addRateChangeSyncLogDetail(RateChangePullLogDetail rateChangePullLogDetail) {
        this.rateChangePullLogDetails.add(rateChangePullLogDetail);
        rateChangePullLogDetail.setRateChangePullLog(this);
    }

    public void setRateChangePullLogDetails(List<RateChangePullLogDetail> rateChangePullLogDetails) {
        this.rateChangePullLogDetails = rateChangePullLogDetails;
    }
}
