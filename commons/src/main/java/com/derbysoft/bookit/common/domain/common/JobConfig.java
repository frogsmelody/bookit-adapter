package com.derbysoft.bookit.common.domain.common;

import com.derbysoft.common.domain.PersistenceSupport;

import javax.persistence.*;

/**
 * Author: Jason Wu
 * Date  : 2013-09-25
 */
@Entity
public class JobConfig extends PersistenceSupport {

    @Column(unique = true)
    private String jobId;

    @Column(nullable = false)
    private String cronExpression;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "default RUNNING")
    private ScheduleStatus scheduleStatus = ScheduleStatus.RUNNING;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public ScheduleStatus getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(ScheduleStatus scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }
}
