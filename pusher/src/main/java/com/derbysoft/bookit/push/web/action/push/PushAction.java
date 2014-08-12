package com.derbysoft.bookit.push.web.action.push;

import com.derbysoft.bookit.common.repository.HotelRepository;
import com.derbysoft.bookit.push.job.component.PushTaskQueueService;
import com.derbysoft.bookit.push.job.support.ChangesPushTask;
import com.derbysoft.bookit.push.web.action.support.AbstractDMXAction;
import com.derbysoft.synchronizer.common.ccs.Hotel;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PushAction extends AbstractDMXAction {

    @Autowired
    private PushTaskQueueService<ChangesPushTask> pushTaskQueueService;

    @Autowired
    private HotelRepository hotelRepository;

    private PushingHotel pushingHotel;

    @Action("listPushingHotel")
    public String listPushingHotel() {
        try {
            pushingHotel = new PushingHotel();
            Set<String> hotelCodes = pushTaskQueueService.getHotelCodes();
            pushingHotel.getWaitingHotels().addAll(hotelCodes);
            List<Hotel> allHotels = hotelRepository.loadAll();
            List<String> enableHotels = getHotelCodes(allHotels, false);
            pushingHotel.getFinishedHotels().addAll(getFinishedHotels(enableHotels, hotelCodes));
            return populateSucceededJsonResult(pushingHotel);
        } catch (Exception e) {
            return populateFailedJsonResult(e);
        }
    }

    private List<String> getFinishedHotels(List<String> enableHotels, Set<String> waitingHotels) {
        ArrayList<String> finishedHotels = new ArrayList<String>();
        for (String enableHotel : enableHotels) {
            if (waitingHotels.contains(enableHotel)) {
                continue;
            }
            finishedHotels.add(enableHotel);
        }
        return finishedHotels;
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

    @Action("refreshPushingQueue")
    public String refreshPushingQueue() {
        try {
            return populateSucceededJsonResult("");
        } catch (Exception e) {
            return populateFailedJsonResult(e);
        }
    }

    public PushingHotel getPushingHotel() {
        return pushingHotel;
    }

    public void setPushTaskQueueService(PushTaskQueueService<ChangesPushTask> pushTaskQueueService) {
        this.pushTaskQueueService = pushTaskQueueService;
    }

    public void setHotelRepository(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }
}
