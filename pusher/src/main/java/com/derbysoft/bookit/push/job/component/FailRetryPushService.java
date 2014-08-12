package com.derbysoft.bookit.push.job.component;

import com.derbysoft.bookit.push.commons.models.LosRateChangePushRQPair;
import com.derbysoft.bookit.push.job.support.ChangeNotifyPair;
import com.derbysoft.bookit.push.job.support.ChangesPushTask;

import java.util.List;

public interface FailRetryPushService {
    boolean push(ChangeNotifyPair changeNotifyPair, ChangesPushTask changesPushTask, List<LosRateChangePushRQPair> requestPairs);
}
