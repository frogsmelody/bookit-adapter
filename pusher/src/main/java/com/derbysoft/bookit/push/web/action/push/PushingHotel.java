package com.derbysoft.bookit.push.web.action.push;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class PushingHotel {
    private List<String> waitingHotels = new ArrayList<String>();
    private List<String> finishedHotels = new ArrayList<String>();

    public List<String> getWaitingHotels() {
        Collections.sort(waitingHotels, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return waitingHotels;
    }

    public List<String> getFinishedHotels() {
        Collections.sort(finishedHotels, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return finishedHotels;
    }
}
