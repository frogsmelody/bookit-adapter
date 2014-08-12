package com.derbysoft.bookit.push.job.job;

import com.derbysoft.bookit.common.repository.HotelRepository;
import com.derbysoft.bookit.common.translator.Translator;
import com.derbysoft.bookit.push.job.component.PushTaskQueueService;
import com.derbysoft.bookit.push.job.support.ChangesPushTask;
import com.derbysoft.dswitch.dto.common.ErrorDTO;
import com.derbysoft.synchronizer.common.ccs.Hotel;
import com.derbysoft.synchronizer.remote.common.SynchronizerRemoteService;
import com.derbysoft.synchronizer.remote.dto.GetKeysRequest;
import com.derbysoft.synchronizer.remote.dto.GetKeysResponse;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import utils.XMLTestSupport;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class GenerateLOSRatePushTaskJobTest extends XMLTestSupport {
    GenerateLOSRatePushTaskJob generateLosRatePushTaskJob = new GenerateLOSRatePushTaskJob();

    HotelRepository hotelRepository;
    PushTaskQueueService<ChangesPushTask> pushTaskQueueService;
    SynchronizerRemoteService synchronizerWebService;
    Translator<List<String>, Void, GetKeysRequest> getKeysRequestTranslator;

    @Before
    public void setUp() throws Exception {
        hotelRepository = EasyMock.createMock(HotelRepository.class);
        pushTaskQueueService = EasyMock.createMock(PushTaskQueueService.class);
        synchronizerWebService = EasyMock.createMock(SynchronizerRemoteService.class);
        getKeysRequestTranslator = EasyMock.createMock(Translator.class);

        generateLosRatePushTaskJob.setHotelRepository(hotelRepository);
        generateLosRatePushTaskJob.setPushTaskQueueService(pushTaskQueueService);
        generateLosRatePushTaskJob.setGetKeysRequestTranslator(getKeysRequestTranslator);
        generateLosRatePushTaskJob.setSynchronizerWebService(synchronizerWebService);
    }

    @Test
    public void testHasError() throws Exception {
        EasyMock.expect(hotelRepository.loadAll()).andReturn(createHotels());
        EasyMock.replay(hotelRepository);

        EasyMock.expect(getKeysRequestTranslator.translate(EasyMock.anyObject(List.class), EasyMock.anyObject(Void.class))).andReturn(new GetKeysRequest());
        EasyMock.replay(getKeysRequestTranslator);
        GetKeysResponse response = new GetKeysResponse();
        response.setError(new ErrorDTO());
        EasyMock.expect(synchronizerWebService.getKeys(EasyMock.anyObject(GetKeysRequest.class))).andReturn(response);
        EasyMock.replay(synchronizerWebService);
        generateLosRatePushTaskJob.sync();
        EasyMock.verify(hotelRepository, getKeysRequestTranslator, synchronizerWebService);
    }

    private List<Hotel> createHotels() {
        ArrayList<Hotel> hotels = new ArrayList<Hotel>();
        hotels.add(createHotel("Hotel-01", false));
        hotels.add(createHotel("Hotel-02", false));
        hotels.add(createHotel("Hotel-03", true));
        hotels.add(createHotel("Hotel-04", false));
        return hotels;
    }

    private Hotel createHotel(String hotelCode, boolean disable) {
        Hotel hotel = new Hotel();
        hotel.setProviderHotelCode(hotelCode);
        hotel.setDisabled(disable);
        return hotel;
    }
}
