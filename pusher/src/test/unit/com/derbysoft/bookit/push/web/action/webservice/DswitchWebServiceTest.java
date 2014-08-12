package com.derbysoft.bookit.push.web.action.webservice;

import com.derbysoft.dswitch.dto.hotel.cds.LOSRateChangeRS;
import com.derbysoft.dswitch.remote.hotel.buyer.HotelBuyerRemoteService;
import com.derbysoft.dswitch.remote.hotel.dto.HotelLOSRateChangeRequest;
import com.derbysoft.dswitch.remote.hotel.dto.HotelLOSRateChangeResponse;
import com.derbysoft.synchronizer.remote.common.SynchronizerRemoteService;
import com.derbysoft.synchronizer.remote.dto.GetChangesRequest;
import com.derbysoft.synchronizer.remote.dto.GetChangesResponse;
import com.derbysoft.synchronizer.remote.dto.GetKeysRequest;
import com.derbysoft.synchronizer.remote.dto.GetKeysResponse;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;
import utils.XMLTestSupport;

public class DswitchWebServiceTest extends XMLTestSupport {
    DswitchWebService dswitchWebService = new DswitchWebService();
    HotelBuyerRemoteService rateChangeWebService;
    SynchronizerRemoteService synchronizerRemoteService;

    @Before
    public void setUp() throws Exception {
        rateChangeWebService = EasyMock.createMock(HotelBuyerRemoteService.class);
        synchronizerRemoteService = EasyMock.createMock(SynchronizerRemoteService.class);

        dswitchWebService.setDswitchGetCDSKey("cdsKey");
        dswitchWebService.setStandardSynchronizer("Synchronizer");
        dswitchWebService.setRateChangeWebService(rateChangeWebService);
        dswitchWebService.setSynchronizerRemoteService(synchronizerRemoteService);
    }

    @Test
    public void testGetLosRateChange() {
        WebServiceCondition condition = new WebServiceCondition();
        condition.setStart("2012-01-01");
        condition.setEnd("2012-01-03");
        condition.setHotelCodes("Hotel-A");
        condition.setLosList("2,3,5");
        condition.setRatePlans("RatePlan-A,RatePlan-B,RatePlan-C");
        condition.setRoomTypes("RoomType-A,RoomType-B,RoomType-C");

        EasyMock.expect(rateChangeWebService.getLOSRateChange(EasyMock.anyObject(HotelLOSRateChangeRequest.class))).andAnswer(new IAnswer<HotelLOSRateChangeResponse>() {
            @Override
            public HotelLOSRateChangeResponse answer() throws Throwable {
                HotelLOSRateChangeRequest request = (HotelLOSRateChangeRequest) EasyMock.getCurrentArguments()[0];
                assertEquals(readFile("GetLosRateChangeRQ.json"), formatObjectToJson(request, "taskId"));
                HotelLOSRateChangeResponse response = new HotelLOSRateChangeResponse();
                LOSRateChangeRS losRateChangeRS = new LOSRateChangeRS();
                losRateChangeRS.setHotelCode("Hotel-A");
                response.setLosRateChangeRS(losRateChangeRS);
                return response;
            }
        });
        EasyMock.replay(rateChangeWebService);
        dswitchWebService.getLosRateChange(condition);
        EasyMock.verify(rateChangeWebService);
    }

    @Test
    public void testGetKeys() {
        WebServiceCondition condition = new WebServiceCondition();
        condition.setHotelCodes("Hotel-A,Hotel-B,Hotel-C");
        EasyMock.expect(synchronizerRemoteService.getKeys(EasyMock.anyObject(GetKeysRequest.class))).andAnswer(new IAnswer<GetKeysResponse>() {
            @Override
            public GetKeysResponse answer() throws Throwable {
                GetKeysRequest request = (GetKeysRequest) EasyMock.getCurrentArguments()[0];
                assertEquals(readFile("GetKeyRQ.json"), formatObjectToJson(request, "taskId"));
                return new GetKeysResponse();
            }
        });
        EasyMock.replay(synchronizerRemoteService);
        dswitchWebService.getKeys(condition);
        EasyMock.verify(synchronizerRemoteService);
    }

    @Test
    public void testGetChanges() {
        WebServiceCondition condition = new WebServiceCondition();
        condition.setHotelCodes("Hotel-A");
        condition.setKeys("Key-1,Key-3,Key-5,");
        EasyMock.expect(synchronizerRemoteService.getChanges(EasyMock.anyObject(GetChangesRequest.class))).andAnswer(new IAnswer<GetChangesResponse>() {
            @Override
            public GetChangesResponse answer() throws Throwable {
                GetChangesRequest request = (GetChangesRequest) EasyMock.getCurrentArguments()[0];
                String taskId = formatObjectToJson(request, "taskId");
                assertEquals(readFile("GetChangesRQ.json"), taskId);
                return new GetChangesResponse();
            }
        });
        EasyMock.replay(synchronizerRemoteService);
        dswitchWebService.getChanges(condition);
        EasyMock.verify(synchronizerRemoteService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetChangeRQ() {
        WebServiceCondition condition = new WebServiceCondition();
        condition.setHotelCodes("Hotel-A");
        EasyMock.expect(synchronizerRemoteService.getChanges(EasyMock.anyObject(GetChangesRequest.class))).andAnswer(new IAnswer<GetChangesResponse>() {
            @Override
            public GetChangesResponse answer() throws Throwable {
                GetChangesRequest request = (GetChangesRequest) EasyMock.getCurrentArguments()[0];
                String taskId = formatObjectToJson(request, "taskId");
                assertEquals(readFile("GetChangesRQ.json"), taskId);
                return new GetChangesResponse();
            }
        });
        EasyMock.replay(synchronizerRemoteService);
        dswitchWebService.getChanges(condition);
        EasyMock.verify(synchronizerRemoteService);
    }
}
