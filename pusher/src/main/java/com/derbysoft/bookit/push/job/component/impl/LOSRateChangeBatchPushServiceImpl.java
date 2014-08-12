package com.derbysoft.bookit.push.job.component.impl;

import com.derbysoft.bookit.push.commons.factory.RatePushThreadPoolFactory;
import com.derbysoft.bookit.push.commons.models.LosRateChangePushRQPair;
import com.derbysoft.bookit.push.commons.models.LosRateChangePushRSPair;
import com.derbysoft.bookit.push.job.component.LOSRateChangeBatchPushService;
import com.derbysoft.bookit.push.job.support.ChangeNotifyPair;
import com.derbysoft.bookit.push.job.support.ChangesPushTask;
import com.derbysoft.bookit.push.ws.WebService;
import com.derbysoft.common.util.ExceptionUtils;
import com.derbysoft.common.util.concurrent.ExecutorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Author: Jason Wu
 * Date  : 2014-03-26
 */
@Component("batchPushLosRateChangeService")
public class LOSRateChangeBatchPushServiceImpl implements LOSRateChangeBatchPushService {

    @Autowired
    private WebService providerPushService;

    @Override
    public List<LosRateChangePushRSPair> batchPush(List<LosRateChangePushRQPair> losRateChangePushRQPairs,
                                                   ChangeNotifyPair changeNotifyPair, final ChangesPushTask changesPushTask) {
        ArrayList<Callable<LosRateChangePushRSPair>> tasks = new ArrayList<Callable<LosRateChangePushRSPair>>();
        for (LosRateChangePushRQPair losRateChangePushRQPair : losRateChangePushRQPairs) {
            tasks.add(createTask(losRateChangePushRQPair, changesPushTask));
        }

        return ExecutorUtils.batchExecute(RatePushThreadPoolFactory.getExecutor(), tasks);
    }

    private Callable<LosRateChangePushRSPair> createTask(final LosRateChangePushRQPair losRateChangePushRQPair,
                                                         final ChangesPushTask changesPushTask) {
        return new Callable<LosRateChangePushRSPair>() {
            @Override
            public LosRateChangePushRSPair call() {
                try {
                    if (changesPushTask.isCancelled()) {
                        return LosRateChangePushRSPair.build(losRateChangePushRQPair, "Cancelled Task", true);
                    }
                    String response = providerPushService.send(losRateChangePushRQPair.getRateChangePushRequest());
                    return LosRateChangePushRSPair.build(losRateChangePushRQPair, response, pushSucceed(response));
                } catch (Exception ex) {
                    return LosRateChangePushRSPair.build(losRateChangePushRQPair, ExceptionUtils.toString(ex), false);
                }
            }
        };
    }

    private boolean pushSucceed(String response) {
        return matchKeyWord(response, "<Success/>");
    }

    private boolean matchKeyWord(String pushHotel, String response) {
        return StringUtils.containsIgnoreCase(pushHotel, response);
    }
}
