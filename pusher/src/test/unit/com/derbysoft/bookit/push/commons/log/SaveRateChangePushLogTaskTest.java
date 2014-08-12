package com.derbysoft.bookit.push.commons.log;

import com.derbysoft.bookit.push.domain.RateChangePushLog;
import com.derbysoft.bookit.push.commons.models.LosRateChangePushRQPair;
import com.derbysoft.bookit.push.commons.models.LosRateChangePushRSPair;
import com.derbysoft.common.repository.partition.PartitionTableRepository;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("ALL")
public class SaveRateChangePushLogTaskTest {
    PartitionTableRepository<RateChangePushLog> tablePartitionService;

    @Before
    public void setUp() throws Exception {
        tablePartitionService = EasyMock.createMock(PartitionTableRepository.class);
    }

    @Test
    public void testSave() throws Exception {
        RateChangePushLog getRateChangePushLog = new RateChangePushLog();
        Set<LosRateChangePushRSPair> logDetailPairs = new HashSet<LosRateChangePushRSPair>();
        logDetailPairs.add(LosRateChangePushRSPair.build(LosRateChangePushRQPair.build("2012-01-01", ""), "", true));
        logDetailPairs.add(LosRateChangePushRSPair.build(LosRateChangePushRQPair.build("2012-01-02", ""), "", true));
        SaveRateChangePushLogTask saveRateChangePushLogTask = new SaveRateChangePushLogTask(tablePartitionService, getRateChangePushLog, logDetailPairs);
        saveRateChangePushLogTask.run();
        Assert.assertTrue(saveRateChangePushLogTask.getLogDetailPairs().size() == 2);
    }
}
