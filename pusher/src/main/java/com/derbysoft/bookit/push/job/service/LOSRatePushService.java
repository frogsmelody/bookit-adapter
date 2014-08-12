package com.derbysoft.bookit.push.job.service;

import com.derbysoft.bookit.push.job.support.ChangesPushTask;
import com.derbysoft.dswitch.dto.hotel.cds.LOSRateChangeDTO;

import java.util.List;

public interface LOSRatePushService {
    boolean push(List<LOSRateChangeDTO> losRateChangeDTOs, ChangesPushTask task);
}
