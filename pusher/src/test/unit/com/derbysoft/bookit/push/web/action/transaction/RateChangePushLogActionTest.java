package com.derbysoft.bookit.push.web.action.transaction;
import com.derbysoft.bookit.push.domain.RateChangePushLog;
import com.derbysoft.bookit.push.commons.models.RateChangePushLogCondition;
import com.derbysoft.common.repository.partition.PartitionTableRepository;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import utils.XMLTestSupport;

/**
 * Author: Jason Wu
 * Date  : 2014-03-27
 */
@SuppressWarnings("ALL")
public class RateChangePushLogActionTest extends XMLTestSupport {
    RateChangePushLogAction rateChangePushLogAction = new RateChangePushLogAction();
    PartitionTableRepository<RateChangePushLog> logTablePartitionRepository;

    @Before
    public void setUp() throws Exception {
        logTablePartitionRepository = EasyMock.createMock(PartitionTableRepository.class);
        rateChangePushLogAction.setLogTablePartitionRepository(logTablePartitionRepository);
    }

    @Test
    public void noFilter() throws Exception {
        RateChangePushLogCondition condition = new RateChangePushLogCondition();
        condition.setId(12L);
        condition.setDate("2012-01-01");
        rateChangePushLogAction.setCondition(condition);

        RateChangePushLog rateChangePushLog = parseJsonObject(readFile("RateChangePushLogDetail_input_01.json"), RateChangePushLog.class);

        EasyMock.expect(logTablePartitionRepository.get(EasyMock.anyObject(String.class), EasyMock.anyObject(Long.class))).andReturn(rateChangePushLog);
        EasyMock.replay(logTablePartitionRepository);

        rateChangePushLogAction.loadRateAndAvailLogDetail();
        EasyMock.verify(logTablePartitionRepository);
        String actual = sortResultMap(rateChangePushLogAction.getAjaxResult());
        assertEquals(readFile("RateChangePushLogDetail_output_01.json"), actual);
    }

    @Test
    public void filterByCheckIn() throws Exception {
        RateChangePushLogCondition condition = new RateChangePushLogCondition();
        condition.setId(12L);
        condition.setDate("2012-01-01");
        condition.setCheckIn("2014-03-26");
        rateChangePushLogAction.setCondition(condition);

        RateChangePushLog rateChangePushLog = parseJsonObject(readFile("RateChangePushLogDetail_input_01.json"), RateChangePushLog.class);

        EasyMock.expect(logTablePartitionRepository.get(EasyMock.anyObject(String.class), EasyMock.anyObject(Long.class))).andReturn(rateChangePushLog);
        EasyMock.replay(logTablePartitionRepository);

        rateChangePushLogAction.loadRateAndAvailLogDetail();
        EasyMock.verify(logTablePartitionRepository);

        String actual = sortResultMap(rateChangePushLogAction.getAjaxResult());
        assertEquals(readFile("RateChangePushLogDetail_output_02.json"), actual);
    }

    @Test
    public void filterHasError() throws Exception {
        RateChangePushLogCondition condition = new RateChangePushLogCondition();
        condition.setId(12L);
        condition.setDate("2012-01-01");
        condition.setHasError("YES");
        rateChangePushLogAction.setCondition(condition);

        RateChangePushLog rateChangePushLog = parseJsonObject(readFile("RateChangePushLogDetail_input_01.json"), RateChangePushLog.class);

        EasyMock.expect(logTablePartitionRepository.get(EasyMock.anyObject(String.class), EasyMock.anyObject(Long.class))).andReturn(rateChangePushLog);
        EasyMock.replay(logTablePartitionRepository);

        rateChangePushLogAction.loadRateAndAvailLogDetail();
        EasyMock.verify(logTablePartitionRepository);
        String actual = sortResultMap(rateChangePushLogAction.getAjaxResult());

        assertEquals(readFile("RateChangePushLogDetail_output_03.json"), actual);
    }

    @Test
    public void filterNoError() throws Exception {
        RateChangePushLogCondition condition = new RateChangePushLogCondition();
        condition.setId(12L);
        condition.setDate("2012-01-01");
        condition.setHasError("NO");
        rateChangePushLogAction.setCondition(condition);

        RateChangePushLog rateChangePushLog = parseJsonObject(readFile("RateChangePushLogDetail_input_01.json"), RateChangePushLog.class);

        EasyMock.expect(logTablePartitionRepository.get(EasyMock.anyObject(String.class), EasyMock.anyObject(Long.class))).andReturn(rateChangePushLog);
        EasyMock.replay(logTablePartitionRepository);

        rateChangePushLogAction.loadRateAndAvailLogDetail();
        EasyMock.verify(logTablePartitionRepository);
        String actual = sortResultMap(rateChangePushLogAction.getAjaxResult());
        assertEquals(readFile("RateChangePushLogDetail_output_04.json"), actual);
    }
}
