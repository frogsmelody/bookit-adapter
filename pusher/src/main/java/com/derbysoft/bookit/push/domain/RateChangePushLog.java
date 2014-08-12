package com.derbysoft.bookit.push.domain;


import com.derbysoft.bookit.push.job.support.ChangeType;
import com.derbysoft.common.domain.BasePersistenceSupport;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class RateChangePushLog extends BasePersistenceSupport {

    @Column(unique = true, length = 100)
    private String taskId;

    @Column(nullable = false)
    private String hotelCode;

    @Column(nullable = false)
    private String ratePlanCode;

    @Column(nullable = false)
    private String roomTypeCode;

    @Column
    @Enumerated(EnumType.STRING)
    private ChangeType changeType;

    private String start;

    private String end;

    private Long timeSpan;

    @Column(columnDefinition = "boolean default false")
    private Boolean error = false;

    @Column(columnDefinition = "longtext")
    private String errorMessage;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "rateChangePushLog")
    private List<RateChangePushLogDetail> rateChangePushLogDetails = new ArrayList<RateChangePushLogDetail>();

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

    public String getHotelCode() {
        return hotelCode;
    }

    public void setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.error = true;
        this.errorMessage = errorMessage;
    }

    public List<RateChangePushLogDetail> getRateChangePushLogDetails() {
        return rateChangePushLogDetails;
    }

    public void addRateChangeSyncLogDetail(RateChangePushLogDetail rateChangePushLogDetail) {
        this.rateChangePushLogDetails.add(rateChangePushLogDetail);
        rateChangePushLogDetail.setRateChangePushLog(this);
    }

    public void setRateChangePushLogDetails(List<RateChangePushLogDetail> rateChangePushLogDetails) {
        this.rateChangePushLogDetails = rateChangePushLogDetails;
    }
}
