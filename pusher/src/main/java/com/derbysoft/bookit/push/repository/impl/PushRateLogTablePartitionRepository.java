package com.derbysoft.bookit.push.repository.impl;

import com.derbysoft.bookit.push.domain.RateChangePushLog;
import com.derbysoft.common.repository.partition.PartitionByDayRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Author: Jason Wu
 * Date  : 2013-11-04
 */
@Repository("pushRateLogTablePartitionRepository")
public class PushRateLogTablePartitionRepository extends PartitionByDayRepository<RateChangePushLog> {

    @Value("table.partition")
    private String tablePartition;

    public PushRateLogTablePartitionRepository() {
        super();
    }

    @Override
    public boolean getPartitionRequired() {
        return Boolean.valueOf(tablePartition);
    }
}
