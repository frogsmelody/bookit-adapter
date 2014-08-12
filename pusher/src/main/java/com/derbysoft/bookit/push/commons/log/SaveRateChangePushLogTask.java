package com.derbysoft.bookit.push.commons.log;

import com.derbysoft.bookit.common.commons.log.Direction;
import com.derbysoft.bookit.push.commons.models.LosRateChangePushRSPair;
import com.derbysoft.bookit.push.domain.RateChangePushLog;
import com.derbysoft.bookit.push.domain.RateChangePushLogDetail;
import com.derbysoft.common.repository.partition.PartitionTableRepository;

import java.util.Set;

public class SaveRateChangePushLogTask implements Runnable {
    private PartitionTableRepository<RateChangePushLog> tablePartitionService;
    private RateChangePushLog getRateChangePushLog;
    private Set<LosRateChangePushRSPair> logDetailPairs;

    public SaveRateChangePushLogTask(PartitionTableRepository<RateChangePushLog> tablePartitionService,
                                     RateChangePushLog getRateChangePushLog,
                                     Set<LosRateChangePushRSPair> logDetailPairs) {
        this.tablePartitionService = tablePartitionService;
        this.getRateChangePushLog = getRateChangePushLog;
        this.logDetailPairs = logDetailPairs;
    }

    @Override
    public void run() {
        saveLog();
    }

    private void saveLog() {
        for (LosRateChangePushRSPair logDetailPair : logDetailPairs) {
            RateChangePushLogDetail toChannel = new RateChangePushLogDetail();
            toChannel.setCheckIn(logDetailPair.getRequestPair().getCheckIn());
            toChannel.setDirection(Direction.TO_CHANNEL);
            toChannel.setDetail(logDetailPair.getRequestPair().getRateChangePushRequest());
            toChannel.setPushSucceed(logDetailPair.isSucceed());
            getRateChangePushLog.addRateChangeSyncLogDetail(toChannel);

            RateChangePushLogDetail fromChannel = new RateChangePushLogDetail();
            fromChannel.setCheckIn(logDetailPair.getRequestPair().getCheckIn());
            fromChannel.setDirection(Direction.FROM_CHANNEL);
            fromChannel.setDetail(logDetailPair.getResponse());
            fromChannel.setPushSucceed(logDetailPair.isSucceed());
            getRateChangePushLog.addRateChangeSyncLogDetail(fromChannel);
        }
        tablePartitionService.save(getRateChangePushLog);
    }

    public RateChangePushLog getGetRateChangePushLog() {
        return getRateChangePushLog;
    }

    public Set<LosRateChangePushRSPair> getLogDetailPairs() {
        return logDetailPairs;
    }

    public PartitionTableRepository<RateChangePushLog> getTablePartitionService() {
        return tablePartitionService;
    }
}
