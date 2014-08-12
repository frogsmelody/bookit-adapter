package com.derbysoft.bookit.push.commons.loginterceptor;

import com.derbysoft.bookit.common.ccs.SystemConfig;
import com.derbysoft.bookit.push.domain.RateChangePushLog;
import com.derbysoft.bookit.common.repository.SystemConfigRepository;
import com.derbysoft.bookit.push.commons.log.SaveRateChangePushLogTask;
import com.derbysoft.bookit.push.commons.models.LosRateChangePushRQPair;
import com.derbysoft.bookit.push.commons.models.LosRateChangePushRSPair;
import com.derbysoft.bookit.push.job.support.ChangeNotifyPair;
import com.derbysoft.bookit.push.job.support.ChangeType;
import com.derbysoft.bookit.push.job.support.ChangesPushTask;
import com.derbysoft.common.repository.partition.PartitionTableRepository;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.XMLTestSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("ALL")
public class LOSRatePushInterceptorTest extends XMLTestSupport {
    private SystemConfigRepository systemConfigRepository;
    private PartitionTableRepository<RateChangePushLog> pushRateLogTablePartitionRepository;
    private Executor saveLogExecutor;

    LOSRatePushInterceptor interceptor = new LOSRatePushInterceptor();

    @Before
    public void setUp() {
        systemConfigRepository = EasyMock.createMock(SystemConfigRepository.class);
        pushRateLogTablePartitionRepository = EasyMock.createMock(PartitionTableRepository.class);
        saveLogExecutor = EasyMock.createMock(Executor.class);

        interceptor.setSystemConfigRepository(systemConfigRepository);
        interceptor.setPushRateLogTablePartitionRepository(pushRateLogTablePartitionRepository);
        interceptor.setSaveLogExecutor(saveLogExecutor);
    }

    @Test
    public void testException() {
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setValue("true");
        EasyMock.expect(systemConfigRepository.load(EasyMock.anyObject(String.class))).andReturn(systemConfig);
        EasyMock.replay(systemConfigRepository);

        saveLogExecutor.execute(EasyMock.anyObject(Runnable.class));
        EasyMock.expectLastCall().andAnswer(new IAnswer<Object>() {
            @Override
            public Object answer() throws Throwable {
                SaveRateChangePushLogTask saveRateChangePushLogTask = (SaveRateChangePushLogTask) EasyMock.getCurrentArguments()[0];
                Assert.assertTrue(saveRateChangePushLogTask.getGetRateChangePushLog().getError());
                Assert.assertTrue(saveRateChangePushLogTask.getGetRateChangePushLog().getErrorMessage().contains("java.util.concurrent.TimeoutException"));
                return null;
            }
        });
        EasyMock.replay(saveLogExecutor);

        Object[] arguments = new Object[3];
        arguments[0] = createLosRateChangePushRQPairs();
        arguments[1] = ChangeNotifyPair.build("RatePlan-A", "RoomType-B", ChangeType.LOS_RATE);
        arguments[2] = ChangesPushTask.build("Hotel-A", "Key-A");
        interceptor.intercept(new ProxyMethodInvocationImpl(arguments, new TimeoutException("timeout")));
        EasyMock.verify(systemConfigRepository, saveLogExecutor);
    }

    @Test
    public void testIntercept() {
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setValue("true");
        EasyMock.expect(systemConfigRepository.load(EasyMock.anyObject(String.class))).andReturn(systemConfig);
        EasyMock.replay(systemConfigRepository);

        saveLogExecutor.execute(EasyMock.anyObject(Runnable.class));
        EasyMock.expectLastCall().andAnswer(new IAnswer<Object>() {
            @Override
            public Object answer() throws Throwable {
                SaveRateChangePushLogTask saveRateChangePushLogTask = (SaveRateChangePushLogTask) EasyMock.getCurrentArguments()[0];
                Assert.assertTrue(!saveRateChangePushLogTask.getGetRateChangePushLog().getError());
                Assert.assertTrue(saveRateChangePushLogTask.getLogDetailPairs().size() == 1);
                assertEquals(readFile("RateChangePushLog_output_01.json"), formatArrayToJson(saveRateChangePushLogTask.getGetRateChangePushLog(), "taskId", "rateChangePushLogDetails", "timeSpan"));
                return null;
            }
        });
        EasyMock.replay(saveLogExecutor);

        Object[] arguments = new Object[3];
        arguments[0] = createLosRateChangePushRQPairs();
        arguments[1] = ChangeNotifyPair.build("RatePlan-A", "RoomType-B", ChangeType.LOS_RATE);
        arguments[2] = ChangesPushTask.build("Hotel-A", "Key-A");
        List<LosRateChangePushRSPair> response = new ArrayList<LosRateChangePushRSPair>();
        response.add(LosRateChangePushRSPair.build(LosRateChangePushRQPair.build("", ""), "", true));
        List<LosRateChangePushRSPair> result = (List<LosRateChangePushRSPair>) interceptor.intercept(new ProxyMethodInvocationImpl(arguments, response));
        Assert.assertTrue(result.get(0).isSucceed());
        EasyMock.verify(systemConfigRepository, saveLogExecutor);
    }

    @Test
    public void notSaveSucceedLog() {
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setValue("false");
        EasyMock.expect(systemConfigRepository.load(EasyMock.anyObject(String.class))).andReturn(systemConfig);
        EasyMock.replay(systemConfigRepository);

        saveLogExecutor.execute(EasyMock.anyObject(Runnable.class));
        EasyMock.expectLastCall().andAnswer(new IAnswer<Object>() {
            @Override
            public Object answer() throws Throwable {
                SaveRateChangePushLogTask saveRateChangePushLogTask = (SaveRateChangePushLogTask) EasyMock.getCurrentArguments()[0];
                Assert.assertTrue(saveRateChangePushLogTask.getLogDetailPairs().isEmpty());
                return null;
            }
        });
        EasyMock.replay(saveLogExecutor);

        Object[] arguments = new Object[3];
        arguments[0] = createLosRateChangePushRQPairs();
        arguments[1] = ChangeNotifyPair.build("RatePlan-A", "RoomType-B", ChangeType.LOS_RATE);
        arguments[2] = ChangesPushTask.build("Hotel-A", "Key-A");
        List<LosRateChangePushRSPair> response = new ArrayList<LosRateChangePushRSPair>();
        response.add(LosRateChangePushRSPair.build(LosRateChangePushRQPair.build("", ""), "", true));
        List<LosRateChangePushRSPair> result = (List<LosRateChangePushRSPair>) interceptor.intercept(new ProxyMethodInvocationImpl(arguments, response));
        Assert.assertTrue(result.get(0).isSucceed());
        EasyMock.verify(systemConfigRepository, saveLogExecutor);
    }

    private List<LosRateChangePushRQPair> createLosRateChangePushRQPairs() {
        ArrayList<LosRateChangePushRQPair> losRateChangePushRQPairs = new ArrayList<LosRateChangePushRQPair>();
        losRateChangePushRQPairs.add(LosRateChangePushRQPair.build("2012-01-01", ""));
        losRateChangePushRQPairs.add(LosRateChangePushRQPair.build("2012-01-03", ""));
        losRateChangePushRQPairs.add(LosRateChangePushRQPair.build("2012-01-04", ""));
        losRateChangePushRQPairs.add(LosRateChangePushRQPair.build("2012-01-02", ""));
        return losRateChangePushRQPairs;
    }

    private List<LosRateChangePushRSPair> createLosRateChangePushRSPair() {
        ArrayList<LosRateChangePushRSPair> losRateChangePushRSPairs = new ArrayList<LosRateChangePushRSPair>();
        losRateChangePushRSPairs.add(LosRateChangePushRSPair.build(LosRateChangePushRQPair.build("", ""), "", true));
        return losRateChangePushRSPairs;
    }
}
