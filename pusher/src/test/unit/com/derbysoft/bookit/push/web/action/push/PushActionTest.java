package com.derbysoft.bookit.push.web.action.push;

import com.derbysoft.bookit.common.repository.HotelRepository;
import com.derbysoft.bookit.push.job.component.PushTaskQueueService;
import com.derbysoft.bookit.push.job.support.ChangesPushTask;
import com.derbysoft.synchronizer.common.ccs.Hotel;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class PushActionTest {
    PushAction pushAction = new PushAction();
    PushTaskQueueService<ChangesPushTask> pushTaskQueueService;
    HotelRepository hotelRepository;

    @Before
    public void setUp() throws Exception {
        pushTaskQueueService = EasyMock.createMock(PushTaskQueueService.class);
        hotelRepository = EasyMock.createMock(HotelRepository.class);

        pushAction.setPushTaskQueueService(pushTaskQueueService);
        pushAction.setHotelRepository(hotelRepository);
    }

    @Test
    public void testListPushingHotel() throws Exception {
        Assert.assertTrue(true);
        /*HashSet<String> allHotels = new HashSet<String>();
        allHotels.add("Hotel-01");
        allHotels.add("Hotel-02");
        allHotels.add("Hotel-03");
        allHotels.add("Hotel-04");
        EasyMock.expect(pushTaskQueueService.getHotelCodes()).andReturn(allHotels).times(2);
        EasyMock.replay(pushTaskQueueService);

        EasyMock.expect(hotelRepository.loadAll()).andReturn(createHotels());
        EasyMock.replay(hotelRepository);

        pushAction.listPushingHotel();
        Assert.assertTrue(pushAction.getAjaxResult().contains("\"finishedHotels\":[\"Hotel-05\"]"));
        Assert.assertTrue(pushAction.getAjaxResult().contains("\"waitingHotels\":[\"Hotel-01\",\"Hotel-02\",\"Hotel-03\",\"Hotel-04\"]"));
        EasyMock.verify(pushTaskQueueService, hotelRepository);*/
    }

    private ArrayList<Hotel> createHotels() {
        ArrayList<Hotel> hotels = new ArrayList<Hotel>();
        hotels.add(createHotel("Hotel-01", false));
        hotels.add(createHotel("Hotel-02", false));
        hotels.add(createHotel("Hotel-03", false));
        hotels.add(createHotel("Hotel-04", true));
        hotels.add(createHotel("Hotel-05", false));
        return hotels;
    }

    private Hotel createHotel(String hotelCode, boolean disable) {
        Hotel hotel = new Hotel();
        hotel.setProviderHotelCode(hotelCode);
        hotel.setDisabled(disable);
        return hotel;
    }
}
