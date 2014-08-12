package com.derbysoft.bookit.push.job.component.impl;

import com.derbysoft.bookit.push.commons.models.LosRateChangePushRQPair;
import com.derbysoft.bookit.push.commons.models.LosRateChangePushRSPair;
import com.derbysoft.bookit.push.job.component.FailRetryPushService;
import com.derbysoft.bookit.push.job.component.LOSRateChangeBatchPushService;
import com.derbysoft.bookit.push.job.support.ChangeNotifyPair;
import com.derbysoft.bookit.push.job.support.ChangesPushTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FailRetryPushServiceImpl implements FailRetryPushService {
    private static final int RETRY_TIMES = 2;

    @Autowired
    private LOSRateChangeBatchPushService losRateChangeBatchPushService;

    @Override
    public boolean push(ChangeNotifyPair changeNotifyPair, ChangesPushTask changesPushTask, List<LosRateChangePushRQPair> requestPairs) {
        return push(0, changeNotifyPair, changesPushTask, requestPairs);
    }

    public boolean push(int retryTimes, ChangeNotifyPair changeNotifyPair, ChangesPushTask changesPushTask, List<LosRateChangePushRQPair> requestPairs) {
        if (!hasError(losRateChangeBatchPushService.batchPush(requestPairs, changeNotifyPair, changesPushTask))) {
            return true;
        }
        retryTimes++;
        return retryTimes <= RETRY_TIMES && push(retryTimes, changeNotifyPair, changesPushTask, requestPairs);
    }

    private boolean hasError(List<LosRateChangePushRSPair> losRateChangePushRSPairs) {
        for (LosRateChangePushRSPair losRateChangePushRSPair : losRateChangePushRSPairs) {
            if (!losRateChangePushRSPair.isSucceed()) {
                return true;
            }
        }
        return false;
    }

    public void setLosRateChangeBatchPushService(LOSRateChangeBatchPushService losRateChangeBatchPushService) {
        this.losRateChangeBatchPushService = losRateChangeBatchPushService;
    }
}
