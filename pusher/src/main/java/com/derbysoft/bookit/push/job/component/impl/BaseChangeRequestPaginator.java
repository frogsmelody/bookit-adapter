package com.derbysoft.bookit.push.job.component.impl;

import com.derbysoft.bookit.common.repository.SystemConfigRepository;
import com.derbysoft.bookit.dto.OTAMessage;
import com.derbysoft.bookit.push.commons.models.NotifyRequestsPair;
import com.derbysoft.bookit.push.job.component.NotifyRQPaginator;
import com.derbysoft.common.util.CollectionsUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public abstract class BaseChangeRequestPaginator<T extends OTAMessage> implements NotifyRQPaginator<T> {
    @Autowired
    protected SystemConfigRepository systemConfigRepository;

    @Override
    @SuppressWarnings("unchecked")
    public List<NotifyRequestsPair<T>> paginate(Map<String, T> notifyRequestMap) {
        List<String> checkInList = getCheckInList(notifyRequestMap);
        List<NotifyRequestsPair<T>> losRateChangePushRQPairs = new ArrayList<NotifyRequestsPair<T>>();
        int paginateNumber = getPaginateNumber();
        if (paginateNumber <= 1) {
            losRateChangePushRQPairs.add(new NotifyRequestsPair(CollectionsUtils.first(checkInList), CollectionsUtils.last(checkInList), new ArrayList<T>(notifyRequestMap.values())));
            return losRateChangePushRQPairs;
        }
        int total = checkInList.size();
        int totalPages = total % paginateNumber == 0 ? total / paginateNumber : total / paginateNumber + 1;
        for (int page = 0; page < totalPages; page++) {
            int start = page * paginateNumber;
            int end = (page + 1) * paginateNumber > total ? total : (page + 1) * paginateNumber;
            List<T> rateNotifyRequests = new ArrayList<T>();
            List<String> paginatedCheckIns = new ArrayList<String>();
            for (int index = start; index < end; index++) {
                paginatedCheckIns.add(checkInList.get(index));
                rateNotifyRequests.add(notifyRequestMap.get(checkInList.get(index)));
            }
            losRateChangePushRQPairs.add(new NotifyRequestsPair(CollectionsUtils.first(paginatedCheckIns), CollectionsUtils.last(paginatedCheckIns), rateNotifyRequests));
        }
        return losRateChangePushRQPairs;
    }

    protected abstract int getPaginateNumber();

    private String getCheckIn(List<String> checkInList) {
        return String.format("%s:%s", CollectionsUtils.first(checkInList), CollectionsUtils.last(checkInList));
    }

    private List<String> getCheckInList(Map<String, T> checkInAndNotifyRequestMap) {
        ArrayList<String> checkInList = new ArrayList<String>();
        for (String checkIn : checkInAndNotifyRequestMap.keySet()) {
            checkInList.add(checkIn);
        }
        Collections.sort(checkInList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return checkInList;
    }

    public void setSystemConfigRepository(SystemConfigRepository systemConfigRepository) {
        this.systemConfigRepository = systemConfigRepository;
    }
}
