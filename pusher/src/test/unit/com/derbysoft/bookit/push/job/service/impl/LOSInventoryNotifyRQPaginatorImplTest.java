package com.derbysoft.bookit.push.job.service.impl;

import com.derbysoft.bookit.common.ccs.SystemConfig;
import com.derbysoft.bookit.common.repository.SystemConfigRepository;
import com.derbysoft.bookit.dto.OTAHotelAvailNotifRQ;
import com.derbysoft.bookit.push.commons.models.NotifyRequestsPair;
import com.derbysoft.bookit.push.job.component.impl.LOSInventoryNotifyRQPaginatorImpl;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.XMLTestSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LOSInventoryNotifyRQPaginatorImplTest extends XMLTestSupport {
    LOSInventoryNotifyRQPaginatorImpl paginator = new LOSInventoryNotifyRQPaginatorImpl();
    SystemConfigRepository systemConfigRepository;

    @Before
    public void setUp() throws Exception {
        systemConfigRepository = EasyMock.createMock(SystemConfigRepository.class);
        paginator.setSystemConfigRepository(systemConfigRepository);
    }

    @Test
    public void noPagination() throws Exception {
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setValue("0");
        EasyMock.expect(systemConfigRepository.load(EasyMock.anyObject(String.class))).andReturn(systemConfig);
        EasyMock.replay(systemConfigRepository);

        Map<String, OTAHotelAvailNotifRQ> expected = new HashMap<String, OTAHotelAvailNotifRQ>();
        expected.put("2012-01-01", new OTAHotelAvailNotifRQ());
        expected.put("2012-01-02", new OTAHotelAvailNotifRQ());
        expected.put("2012-01-03", new OTAHotelAvailNotifRQ());
        List<NotifyRequestsPair<OTAHotelAvailNotifRQ>> requestList = paginator.paginate(expected);
        Assert.assertTrue(requestList.get(0).getStart().equals("2012-01-01") && requestList.get(0).getEnd().equals("2012-01-03"));
        Assert.assertTrue(requestList.size() == 1);
    }

    @Test
    public void testPaginate() throws Exception {
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setValue("3");
        EasyMock.expect(systemConfigRepository.load(EasyMock.anyObject(String.class))).andReturn(systemConfig);
        EasyMock.replay(systemConfigRepository);

        Map<String, OTAHotelAvailNotifRQ> expected = new HashMap<String, OTAHotelAvailNotifRQ>();
        expected.put("2012-01-01", new OTAHotelAvailNotifRQ());
        expected.put("2012-01-02", new OTAHotelAvailNotifRQ());
        expected.put("2012-01-03", new OTAHotelAvailNotifRQ());
        expected.put("2012-01-04", new OTAHotelAvailNotifRQ());
        expected.put("2012-01-05", new OTAHotelAvailNotifRQ());
        expected.put("2012-01-06", new OTAHotelAvailNotifRQ());
        List<NotifyRequestsPair<OTAHotelAvailNotifRQ>> requestList = paginator.paginate(expected);
        Assert.assertTrue(requestList.get(0).getStart().equals("2012-01-01") && requestList.get(0).getEnd().equals("2012-01-03"));
        Assert.assertTrue(requestList.get(1).getStart().equals("2012-01-04") && requestList.get(1).getEnd().equals("2012-01-06"));
        Assert.assertTrue(requestList.size() == 2);
    }

    @Test
    public void testPaginate2() throws Exception {
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setValue("3");
        EasyMock.expect(systemConfigRepository.load(EasyMock.anyObject(String.class))).andReturn(systemConfig);
        EasyMock.replay(systemConfigRepository);

        Map<String, OTAHotelAvailNotifRQ> expected = new HashMap<String, OTAHotelAvailNotifRQ>();
        expected.put("2012-01-01", new OTAHotelAvailNotifRQ());
        expected.put("2012-01-02", new OTAHotelAvailNotifRQ());
        expected.put("2012-01-03", new OTAHotelAvailNotifRQ());
        expected.put("2012-01-04", new OTAHotelAvailNotifRQ());
        expected.put("2012-01-05", new OTAHotelAvailNotifRQ());
        expected.put("2012-01-06", new OTAHotelAvailNotifRQ());
        expected.put("2012-01-07", new OTAHotelAvailNotifRQ());
        expected.put("2012-01-08", new OTAHotelAvailNotifRQ());
        List<NotifyRequestsPair<OTAHotelAvailNotifRQ>> requestList = paginator.paginate(expected);
        Assert.assertTrue(requestList.get(0).getStart().equals("2012-01-01") && requestList.get(0).getEnd().equals("2012-01-03"));
        Assert.assertTrue(requestList.get(1).getStart().equals("2012-01-04") && requestList.get(1).getEnd().equals("2012-01-06"));
        Assert.assertTrue(requestList.get(2).getStart().equals("2012-01-07") && requestList.get(2).getEnd().equals("2012-01-08"));
        Assert.assertTrue(requestList.size() == 3);
    }
}
