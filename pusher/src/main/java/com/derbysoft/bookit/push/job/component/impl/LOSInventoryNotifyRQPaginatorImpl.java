package com.derbysoft.bookit.push.job.component.impl;

import com.derbysoft.bookit.common.ccs.SystemConfig;
import com.derbysoft.bookit.common.model.SystemConfigKeys;
import com.derbysoft.bookit.dto.OTAHotelAvailNotifRQ;
import org.springframework.stereotype.Component;

@Component("losInventoryNotifyRQPaginator")
public class LOSInventoryNotifyRQPaginatorImpl extends BaseChangeRequestPaginator<OTAHotelAvailNotifRQ> {
    @Override
    public int getPaginateNumber() {
        SystemConfig systemConfig = systemConfigRepository.load(SystemConfigKeys.HOW_MANY_CHECK_IN_IN_A_INVENTORY_PUSH_RQ.getKey());
        if (systemConfig == null || Integer.parseInt(systemConfig.getValue()) <= 1) {
            return 1;
        }
        return Integer.parseInt(systemConfig.getValue());
    }
}
