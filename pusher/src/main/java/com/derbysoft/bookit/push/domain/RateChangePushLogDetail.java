package com.derbysoft.bookit.push.domain;
import com.derbysoft.bookit.common.commons.log.Direction;

import javax.persistence.*;

@Entity
public class RateChangePushLogDetail {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 50)
    private String checkIn;

    @Column(columnDefinition = "bool default true")
    private boolean pushSucceed;

    @ManyToOne(fetch = FetchType.LAZY)
    private RateChangePushLog rateChangePushLog;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Direction direction;

    @Column(columnDefinition = "longtext")
    private String detail;

    public boolean isPushSucceed() {
        return pushSucceed;
    }

    public void setPushSucceed(boolean pushSucceed) {
        this.pushSucceed = pushSucceed;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RateChangePushLog getRateChangePushLog() {
        return rateChangePushLog;
    }

    public void setRateChangePushLog(RateChangePushLog rateChangePushLog) {
        this.rateChangePushLog = rateChangePushLog;
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
