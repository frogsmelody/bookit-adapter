package com.derbysoft.bookit.push.job.job;

import com.derbysoft.bookit.common.repository.HotelRepository;
import com.derbysoft.bookit.common.service.job.SyncJob;
import com.derbysoft.bookit.common.translator.Translator;
import com.derbysoft.bookit.push.job.component.PushTaskQueueService;
import com.derbysoft.bookit.push.job.support.ChangesPushTask;
import com.derbysoft.bookit.push.job.support.RunningTaskManager;
import com.derbysoft.synchronizer.common.ccs.Hotel;
import com.derbysoft.synchronizer.dto.HotelKeysDTO;
import com.derbysoft.synchronizer.remote.common.SynchronizerRemoteService;
import com.derbysoft.synchronizer.remote.dto.GetKeysRequest;
import com.derbysoft.synchronizer.remote.dto.GetKeysResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("generateLOSRatePushTaskJob")
public class GenerateLOSRatePushTaskJob implements SyncJob {
    private static final Logger LOGGER = Logger.getLogger(GenerateLOSRatePushTaskJob.class);

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private PushTaskQueueService<ChangesPushTask> pushTaskQueueService;

    @Autowired
    private SynchronizerRemoteService synchronizerWebService;

    @Autowired
    private Translator<List<String>, Void, GetKeysRequest> getKeysRequestTranslator;

    @Override
    public void sync() {
        if (RunningTaskManager.generateLosRateKeyIsRunning()) {
            LOGGER.info("Last job is not finish, skip this time");
            return;
        }
        try {
            RunningTaskManager.generateLosRateKeyStart();
            LOGGER.info("generate LOSRateChangeKeys begin ...");
            List<Hotel> allHotels = hotelRepository.loadAll();
            List<String> enableHotels = getHotelCodes(allHotels, false);
            List<String> disabledHotels = getHotelCodes(allHotels, true);
            cancelPushTask(disabledHotels);
            GetKeysRequest getKeysRequest = getKeysRequestTranslator.translate(enableHotels, null);
            GetKeysResponse getKeysResponse = synchronizerWebService.getKeys(getKeysRequest);
            if (getKeysResponse.hasError() || CollectionUtils.isEmpty(getKeysResponse.getGetKeysRS().getHotelKeysList())) {
                return;
            }
            for (HotelKeysDTO hotelKeysDTO : getKeysResponse.getGetKeysRS().getHotelKeysList()) {
                pushTaskQueueService.add(hotelKeysDTO.getHotel(), hotelKeysDTO.getKeysList());
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            RunningTaskManager.generateLosRateKeyDone();
            LOGGER.info("generate LOSRateChangeKeys done.");
        }
    }

    private void cancelPushTask(List<String> disabledHotels) {
        for (String disabledHotel : disabledHotels) {
            pushTaskQueueService.cancel(disabledHotel);
        }
    }

    private List<String> getHotelCodes(List<Hotel> allHotels, boolean isDisabled) {
        List<String> hotelCodes = new ArrayList<String>();
        for (Hotel hotel : allHotels) {
            if (hotel.isDisabled() == isDisabled) {
                hotelCodes.add(hotel.getProviderHotelCode());
            }
        }
        return hotelCodes;
    }

    public void setSynchronizerWebService(SynchronizerRemoteService synchronizerWebService) {
        this.synchronizerWebService = synchronizerWebService;
    }

    public void setGetKeysRequestTranslator(Translator<List<String>, Void, GetKeysRequest> getKeysRequestTranslator) {
        this.getKeysRequestTranslator = getKeysRequestTranslator;
    }

    public void setHotelRepository(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public void setPushTaskQueueService(PushTaskQueueService<ChangesPushTask> pushTaskQueueService) {
        this.pushTaskQueueService = pushTaskQueueService;
    }
}
