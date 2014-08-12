package com.derbysoft.bookit.push.job.component;

import com.derbysoft.bookit.push.commons.models.LosRateChangePushRQPair;
import com.derbysoft.bookit.push.commons.models.LosRateChangePushRSPair;
import com.derbysoft.bookit.push.job.support.ChangeNotifyPair;
import com.derbysoft.bookit.push.job.support.ChangesPushTask;

import java.util.List;

public interface LOSRateChangeBatchPushService {
    List<LosRateChangePushRSPair> batchPush(List<LosRateChangePushRQPair> losRateChangePushRQPairs,
                                            ChangeNotifyPair changeNotifyPair,
                                            ChangesPushTask changesPushTask);
}
