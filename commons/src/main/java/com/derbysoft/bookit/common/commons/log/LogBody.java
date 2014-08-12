package com.derbysoft.bookit.common.commons.log;

import org.apache.commons.lang.ObjectUtils;

public final class LogBody {
    public static final String TASK_ID = "echo.token";
    public static final String CHANNEL = "channel";
    public static final String PROVIDER_CHAIN = "supplier";
    public static final String REQUEST_TYPE = "process";
    public static final String PROVIDER_HOTEL_CODES = "provider.hotel.codes";
    public static final String RATE_PLAN = "rate.plan";
    public static final String ROOM_TYPE = "room.type";
    public static final String PROCESSED_RESULT = "process.result";
    public static final String PROCESS_DURATION = "process.duration";
    public static final String SYSTEM_LOAD_STATUS = "system.load.status";
    public static final String MEMORY_STATUS = "memory.status";
    public static final String SYNC_LOS_KEY = "sync.los.key";
    public static final String CHECK_IN_LIST = "check.in.list";
    public static final String CHANGE_TYPE = "change.type";

    private String key;
    private String value;

    public LogBody(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static LogBody of(String key, String value) {
        return new LogBody(key, value);
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LogBody) {
            boolean isEqual = ObjectUtils.equals(key, ((LogBody) obj).getKey());
            isEqual &= ObjectUtils.equals(value, ((LogBody) obj).getValue());
            return isEqual;
        }
        return false;
    }
}
