package com.derbysoft.bookit.push.job.support;

import com.derbysoft.bookit.common.commons.DateTimeUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

public final class ChangesPushTask implements PushTask {
    private String hotelCode;
    private String key;
    private ExecuteStatus executeStatus = ExecuteStatus.RUNNING;
    private TaskStatus rateChangeStatus = TaskStatus.WAITING;
    private TaskStatus inventoryChangeStatus = TaskStatus.WAITING;
    private Date createTime = new Date();

    @Override
    public void cancel() {
        this.executeStatus = ExecuteStatus.CANCELLED;
    }

    @Override
    public boolean isCancelled() {
        return executeStatus == ExecuteStatus.CANCELLED;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    private ChangesPushTask(String hotelCode, String key) {
        this.hotelCode = hotelCode;
        this.key = key;
        this.executeStatus = ExecuteStatus.RUNNING;
    }

    public static ChangesPushTask build(String hotelCode, String key) {
        return new ChangesPushTask(hotelCode, key);
    }

    public ExecuteStatus getExecuteStatus() {
        return executeStatus;
    }

    public String getCreateTime() {
        return DateTimeUtils.formatDateTime(createTime);
    }

    public String getHotelCode() {
        return hotelCode;
    }

    public String buildKey() {
        return String.format("%s:%s", hotelCode, key);
    }

    public TaskStatus getRateChangeStatus() {
        return rateChangeStatus;
    }

    public void setRateChangeStatus(TaskStatus rateChangeStatus) {
        this.rateChangeStatus = rateChangeStatus;
    }

    public TaskStatus getInventoryChangeStatus() {
        return inventoryChangeStatus;
    }

    public void setInventoryChangeStatus(TaskStatus inventoryChangeStatus) {
        this.inventoryChangeStatus = inventoryChangeStatus;
    }

    public void setExecuteStatus(ExecuteStatus executeStatus) {
        this.executeStatus = executeStatus;
    }

    @Override
    public boolean equals(Object obj) {
        return getClass().isInstance(obj) && StringUtils.equalsIgnoreCase(((ChangesPushTask) obj).buildKey(), this.buildKey());
    }

    @Override
    public int hashCode() {
        return this.buildKey().hashCode();
    }

    @Override
    public void pushRateChangeDone() {
        rateChangeStatus = TaskStatus.FINISH;
    }

    @Override
    public void pushInventoryChangeDone() {
        inventoryChangeStatus = TaskStatus.FINISH;
    }

    @Override
    public void pushInventoryChangeStart() {
        inventoryChangeStatus = TaskStatus.PUSHING;
    }

    @Override
    public void pushRateChangeStart() {
        rateChangeStatus = TaskStatus.PUSHING;
    }

    @Override
    public void pushDone() {
        rateChangeStatus = TaskStatus.FINISH;
        inventoryChangeStatus = TaskStatus.FINISH;
    }
}
