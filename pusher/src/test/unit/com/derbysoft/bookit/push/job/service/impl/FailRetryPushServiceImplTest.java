package com.derbysoft.bookit.push.job.service.impl;

import com.derbysoft.bookit.push.commons.models.LosRateChangePushRQPair;
import com.derbysoft.bookit.push.commons.models.LosRateChangePushRSPair;
import com.derbysoft.bookit.push.job.component.LOSRateChangeBatchPushService;
import com.derbysoft.bookit.push.job.component.impl.FailRetryPushServiceImpl;
import com.derbysoft.bookit.push.job.support.ChangeNotifyPair;
import com.derbysoft.bookit.push.job.support.ChangeType;
import com.derbysoft.bookit.push.job.support.ChangesPushTask;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class FailRetryPushServiceImplTest {
    FailRetryPushServiceImpl retryPushService = new FailRetryPushServiceImpl();
    LOSRateChangeBatchPushService losRateChangeBatchPushService;

    @Before
    public void setUp() throws Exception {
        losRateChangeBatchPushService = EasyMock.createMock(LOSRateChangeBatchPushService.class);
        retryPushService.setLosRateChangeBatchPushService(losRateChangeBatchPushService);
    }

    @Test
    public void pushSucceedAtFirstTime() throws Exception {
        List<LosRateChangePushRSPair> losRateChangePushRSPairs1 = new ArrayList<LosRateChangePushRSPair>();
        losRateChangePushRSPairs1.add(LosRateChangePushRSPair.build(LosRateChangePushRQPair.build("2012-01-01", ""), "", true));

        EasyMock.expect(losRateChangeBatchPushService.batchPush(EasyMock.anyObject(List.class), EasyMock.anyObject(ChangeNotifyPair.class), EasyMock.anyObject(ChangesPushTask.class))).andReturn(losRateChangePushRSPairs1);
        EasyMock.replay(losRateChangeBatchPushService);

        Assert.assertTrue(retryPushService.push(ChangeNotifyPair.build("","", ChangeType.LOS_RATE), ChangesPushTask.build("", ""), new ArrayList<LosRateChangePushRQPair>()));
    }

    @Test
    public void retrySucceed() throws Exception {
        List<LosRateChangePushRSPair> losRateChangePushRSPairs1 = new ArrayList<LosRateChangePushRSPair>();
        losRateChangePushRSPairs1.add(LosRateChangePushRSPair.build(LosRateChangePushRQPair.build("2012-01-01", ""), "", false));

        List<LosRateChangePushRSPair> losRateChangePushRSPairs2 = new ArrayList<LosRateChangePushRSPair>();
        losRateChangePushRSPairs2.add(LosRateChangePushRSPair.build(LosRateChangePushRQPair.build("2012-01-01", ""), "", true));

        EasyMock.expect(losRateChangeBatchPushService.batchPush(EasyMock.anyObject(List.class), EasyMock.anyObject(ChangeNotifyPair.class),  EasyMock.anyObject(ChangesPushTask.class))).andReturn(losRateChangePushRSPairs1);
        EasyMock.expect(losRateChangeBatchPushService.batchPush(EasyMock.anyObject(List.class), EasyMock.anyObject(ChangeNotifyPair.class), EasyMock.anyObject(ChangesPushTask.class))).andReturn(losRateChangePushRSPairs2);
        EasyMock.replay(losRateChangeBatchPushService);

        Assert.assertTrue(retryPushService.push(ChangeNotifyPair.build("","", ChangeType.LOS_RATE), ChangesPushTask.build("", ""), new ArrayList<LosRateChangePushRQPair>()));
    }

    @Test
    public void retry2TimesSucceed() throws Exception {
        List<LosRateChangePushRSPair> losRateChangePushRSPairs1 = new ArrayList<LosRateChangePushRSPair>();
        losRateChangePushRSPairs1.add(LosRateChangePushRSPair.build(LosRateChangePushRQPair.build("2012-01-01", ""), "", false));

        List<LosRateChangePushRSPair> losRateChangePushRSPairs2 = new ArrayList<LosRateChangePushRSPair>();
        losRateChangePushRSPairs2.add(LosRateChangePushRSPair.build(LosRateChangePushRQPair.build("2012-01-01", ""), "", false));

        List<LosRateChangePushRSPair> losRateChangePushRSPairs3 = new ArrayList<LosRateChangePushRSPair>();
        losRateChangePushRSPairs3.add(LosRateChangePushRSPair.build(LosRateChangePushRQPair.build("2012-01-01", ""), "", true));

        EasyMock.expect(losRateChangeBatchPushService.batchPush(EasyMock.anyObject(List.class), EasyMock.anyObject(ChangeNotifyPair.class),  EasyMock.anyObject(ChangesPushTask.class))).andReturn(losRateChangePushRSPairs1);
        EasyMock.expect(losRateChangeBatchPushService.batchPush(EasyMock.anyObject(List.class), EasyMock.anyObject(ChangeNotifyPair.class),  EasyMock.anyObject(ChangesPushTask.class))).andReturn(losRateChangePushRSPairs2);
        EasyMock.expect(losRateChangeBatchPushService.batchPush(EasyMock.anyObject(List.class), EasyMock.anyObject(ChangeNotifyPair.class),  EasyMock.anyObject(ChangesPushTask.class))).andReturn(losRateChangePushRSPairs3);
        EasyMock.replay(losRateChangeBatchPushService);

        Assert.assertTrue(retryPushService.push(ChangeNotifyPair.build("","", ChangeType.LOS_RATE), ChangesPushTask.build("", ""), new ArrayList<LosRateChangePushRQPair>()));
    }

    @Test
    public void exceededRetryLimit() throws Exception {
        List<LosRateChangePushRSPair> losRateChangePushRSPairs1 = new ArrayList<LosRateChangePushRSPair>();
        losRateChangePushRSPairs1.add(LosRateChangePushRSPair.build(LosRateChangePushRQPair.build("2012-01-01", ""), "", false));

        List<LosRateChangePushRSPair> losRateChangePushRSPairs2 = new ArrayList<LosRateChangePushRSPair>();
        losRateChangePushRSPairs2.add(LosRateChangePushRSPair.build(LosRateChangePushRQPair.build("2012-01-01", ""), "", false));

        List<LosRateChangePushRSPair> losRateChangePushRSPairs3 = new ArrayList<LosRateChangePushRSPair>();
        losRateChangePushRSPairs3.add(LosRateChangePushRSPair.build(LosRateChangePushRQPair.build("2012-01-01", ""), "", false));

        EasyMock.expect(losRateChangeBatchPushService.batchPush(EasyMock.anyObject(List.class), EasyMock.anyObject(ChangeNotifyPair.class),  EasyMock.anyObject(ChangesPushTask.class))).andReturn(losRateChangePushRSPairs1);
        EasyMock.expect(losRateChangeBatchPushService.batchPush(EasyMock.anyObject(List.class), EasyMock.anyObject(ChangeNotifyPair.class),  EasyMock.anyObject(ChangesPushTask.class))).andReturn(losRateChangePushRSPairs2);
        EasyMock.expect(losRateChangeBatchPushService.batchPush(EasyMock.anyObject(List.class), EasyMock.anyObject(ChangeNotifyPair.class),  EasyMock.anyObject(ChangesPushTask.class))).andReturn(losRateChangePushRSPairs3);
        EasyMock.replay(losRateChangeBatchPushService);

        Assert.assertFalse(retryPushService.push(ChangeNotifyPair.build("","", ChangeType.LOS_RATE), ChangesPushTask.build("", ""), new ArrayList<LosRateChangePushRQPair>()));
    }
}
