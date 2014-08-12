package com.derbysoft.bookit.push.job.service.impl;

import com.derbysoft.bookit.common.translator.Translator;
import com.derbysoft.bookit.dto.OTAHotelRateAmountNotifRQ;
import com.derbysoft.bookit.dto.RateAmountMessage;
import com.derbysoft.bookit.dto.RateAmountMessages;
import com.derbysoft.bookit.dto.StatusApplicationControl;
import com.derbysoft.bookit.push.commons.models.NotifyRequestsPair;
import com.derbysoft.bookit.push.job.component.FailRetryPushService;
import com.derbysoft.bookit.push.job.component.NotifyRQPaginator;
import com.derbysoft.bookit.push.job.support.ChangeNotifyPair;
import com.derbysoft.bookit.push.job.support.ChangesPushTask;
import com.derbysoft.dswitch.dto.hotel.cds.LOSRateChangeDTO;
import com.derbysoft.synchronizer.remote.common.SynchronizerRemoteService;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import utils.XMLTestSupport;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class RateChangePushServiceImplTest extends XMLTestSupport {
    LOSRatePushServiceImpl rateChangePushService = new LOSRatePushServiceImpl();

    Translator<List<LOSRateChangeDTO>, String, Map<String, OTAHotelRateAmountNotifRQ>> rateChangePushRQTranslator;
    SynchronizerRemoteService synchronizerWebService;
    FailRetryPushService failRetryPushService;
    Jaxb2Marshaller jaxb2Marshaller;
    NotifyRQPaginator<OTAHotelRateAmountNotifRQ> losRateNotifyRQPaginator;

    @Before
    public void setUp() throws Exception {
        rateChangePushRQTranslator = EasyMock.createMock(Translator.class);
        jaxb2Marshaller = EasyMock.createMock(Jaxb2Marshaller.class);
        synchronizerWebService = EasyMock.createMock(SynchronizerRemoteService.class);
        failRetryPushService = EasyMock.createMock(FailRetryPushService.class);
        losRateNotifyRQPaginator = EasyMock.createMock(NotifyRQPaginator.class);

        rateChangePushService.setFailRetryPushService(failRetryPushService);
        rateChangePushService.setJaxb2Marshaller(jaxb2Marshaller);
        rateChangePushService.setLosRateNotifyRQPaginator(losRateNotifyRQPaginator);
        rateChangePushService.setRateChangePushRQTranslator(rateChangePushRQTranslator);
    }

    @Test
    public void pushFailed() {
        jaxb2Marshaller.marshal(EasyMock.anyObject(Object.class), EasyMock.anyObject(Result.class));
        EasyMock.expectLastCall().atLeastOnce();
        EasyMock.replay(jaxb2Marshaller);

        EasyMock.expect(rateChangePushRQTranslator.translate(EasyMock.anyObject(ArrayList.class), EasyMock.anyObject(String.class))).andAnswer(new IAnswer<Map<String, OTAHotelRateAmountNotifRQ>>() {
            @Override
            public Map<String, OTAHotelRateAmountNotifRQ> answer() throws Throwable {
                List<LOSRateChangeDTO> losRateChangeDTOs = (List<LOSRateChangeDTO>) EasyMock.getCurrentArguments()[0];
                if (losRateChangeDTOs.size() > 1) {
                    String ratePlan = losRateChangeDTOs.get(0).getRatePlanCode();
                    String roomType = losRateChangeDTOs.get(0).getRoomTypeCode();
                    for (LOSRateChangeDTO losRateChangeDTO : losRateChangeDTOs) {
                        Assert.assertTrue(losRateChangeDTO.getRatePlanCode().equals(ratePlan));
                        Assert.assertTrue(losRateChangeDTO.getRoomTypeCode().equals(roomType));
                    }
                }
                return new HashMap<String, OTAHotelRateAmountNotifRQ>();
            }
        }).atLeastOnce();
        EasyMock.replay(rateChangePushRQTranslator);

        EasyMock.expect(losRateNotifyRQPaginator.paginate(EasyMock.anyObject(Map.class))).andReturn(createRateAmountNotifyRequests());
        EasyMock.replay(losRateNotifyRQPaginator);

        EasyMock.expect(failRetryPushService.push(EasyMock.anyObject(ChangeNotifyPair.class), EasyMock.anyObject(ChangesPushTask.class), EasyMock.anyObject(ArrayList.class))).andReturn(false);
        EasyMock.replay(failRetryPushService);
        List<LOSRateChangeDTO> losRateChangeDTOs = parseJsonArray(readFile("RateChangePushServiceImplTest_input_01.json"), LOSRateChangeDTO.class);
        Assert.assertFalse(rateChangePushService.push(losRateChangeDTOs, ChangesPushTask.build("Hotel-A", "Key-A")));
    }

    @Test
    public void pushSucceed() {
        jaxb2Marshaller.marshal(EasyMock.anyObject(Object.class), EasyMock.anyObject(Result.class));
        EasyMock.expectLastCall().atLeastOnce();
        EasyMock.replay(jaxb2Marshaller);

        EasyMock.expect(rateChangePushRQTranslator.translate(EasyMock.anyObject(ArrayList.class), EasyMock.anyObject(String.class))).andAnswer(new IAnswer<Map<String, OTAHotelRateAmountNotifRQ>>() {
            @Override
            public Map<String, OTAHotelRateAmountNotifRQ> answer() throws Throwable {
                List<LOSRateChangeDTO> losRateChangeDTOs = (List<LOSRateChangeDTO>) EasyMock.getCurrentArguments()[0];
                if (losRateChangeDTOs.size() > 1) {
                    String ratePlan = losRateChangeDTOs.get(0).getRatePlanCode();
                    String roomType = losRateChangeDTOs.get(0).getRoomTypeCode();
                    for (LOSRateChangeDTO losRateChangeDTO : losRateChangeDTOs) {
                        Assert.assertTrue(losRateChangeDTO.getRatePlanCode().equals(ratePlan));
                        Assert.assertTrue(losRateChangeDTO.getRoomTypeCode().equals(roomType));
                    }
                }
                return new HashMap<String, OTAHotelRateAmountNotifRQ>();
            }
        }).atLeastOnce();
        EasyMock.replay(rateChangePushRQTranslator);

        EasyMock.expect(losRateNotifyRQPaginator.paginate(EasyMock.anyObject(Map.class))).andReturn(createRateAmountNotifyRequests()).atLeastOnce();
        EasyMock.replay(losRateNotifyRQPaginator);

        EasyMock.expect(failRetryPushService.push(EasyMock.anyObject(ChangeNotifyPair.class), EasyMock.anyObject(ChangesPushTask.class), EasyMock.anyObject(ArrayList.class))).andReturn(true).atLeastOnce();
        EasyMock.replay(failRetryPushService);
        List<LOSRateChangeDTO> losRateChangeDTOs = parseJsonArray(readFile("RateChangePushServiceImplTest_input_01.json"), LOSRateChangeDTO.class);
        Assert.assertTrue(rateChangePushService.push(losRateChangeDTOs, ChangesPushTask.build("Hotel-A", "Key-A")));
    }

    private ArrayList<NotifyRequestsPair<OTAHotelRateAmountNotifRQ>> createRateAmountNotifyRequests() {
        ArrayList<NotifyRequestsPair<OTAHotelRateAmountNotifRQ>> otaHotelRateAmountNotifRQs = new ArrayList<NotifyRequestsPair<OTAHotelRateAmountNotifRQ>>();
        OTAHotelRateAmountNotifRQ otaHotelRateAmountNotifRQ = new OTAHotelRateAmountNotifRQ();
        RateAmountMessages rateAmountMessages = new RateAmountMessages();
        rateAmountMessages.setHotelCode("Hotel-A");
        RateAmountMessage rateAmountMessage = new RateAmountMessage();
        StatusApplicationControl statusApplicationControl = new StatusApplicationControl();
        statusApplicationControl.setStart("2012-01-02");
        rateAmountMessage.setStatusApplicationControl(statusApplicationControl);
        rateAmountMessages.getRateAmountMessage().add(rateAmountMessage);
        otaHotelRateAmountNotifRQ.setRateAmountMessages(rateAmountMessages);
        ArrayList<OTAHotelRateAmountNotifRQ> hotelRateAmountNotifRQs = new ArrayList<OTAHotelRateAmountNotifRQ>();
        hotelRateAmountNotifRQs.add(otaHotelRateAmountNotifRQ);
        otaHotelRateAmountNotifRQs.add(new NotifyRequestsPair("2012-01-02","2012-01-02", hotelRateAmountNotifRQs));
        return otaHotelRateAmountNotifRQs;
    }
}
