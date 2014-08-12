package com.derbysoft.bookit.push.domain;


import com.derbysoft.bookit.common.commons.log.Direction;

import javax.persistence.*;

@Entity
public class RateChangePullLogDetail {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private RateChangePullLog rateChangePullLog;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Direction direction;

    @Column(columnDefinition = "longtext")
    private String detail;

    @Column(columnDefinition = "boolean default true")
    private boolean succeed;

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RateChangePullLog getRateChangePullLog() {
        return rateChangePullLog;
    }

    public void setRateChangePullLog(RateChangePullLog rateChangePullLog) {
        this.rateChangePullLog = rateChangePullLog;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
