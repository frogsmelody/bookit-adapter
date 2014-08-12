package com.derbysoft.bookit.push.job.support;

public final class ChangeNotifyPair {
    private String ratePlan;
    private String roomType;
    private ChangeType changeType;

    private ChangeNotifyPair(String ratePlan, String roomType, ChangeType changeType) {
        this.ratePlan = ratePlan;
        this.roomType = roomType;
        this.changeType = changeType;
    }

    public static ChangeNotifyPair build(String ratePlan, String roomType, ChangeType changeType) {
        return new ChangeNotifyPair(ratePlan, roomType, changeType);
    }

    public String getRatePlan() {
        return ratePlan;
    }

    public String getRoomType() {
        return roomType;
    }

    public ChangeType getChangeType() {
        return changeType;
    }
}
