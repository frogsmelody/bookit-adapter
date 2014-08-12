package com.derbysoft.bookit.push.job.service;

import com.derbysoft.bookit.push.job.support.ChangesPushTask;
import com.derbysoft.dswitch.dto.hotel.cds.LOSInventoryDTO;

import java.util.List;

public interface LOSInventoryPushService {
    boolean push(List<LOSInventoryDTO> inventoryChanges, ChangesPushTask task);
}
