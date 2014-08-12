package com.derbysoft.bookit.push.commons.models;

import com.derbysoft.bookit.dto.OTAMessage;

import java.util.List;

public final class NotifyRequestsPair<T extends OTAMessage> {
    private String start;
    private String end;
    private List<T> notifyRequests;

    public NotifyRequestsPair(String start, String end, List<T> notifyRequests) {
        this.start = start;
        this.end = end;
        this.notifyRequests = notifyRequests;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public List<T> getNotifyRequests() {
        return notifyRequests;
    }
}
