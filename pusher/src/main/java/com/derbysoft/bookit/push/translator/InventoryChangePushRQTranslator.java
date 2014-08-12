package com.derbysoft.bookit.push.translator;

import com.derbysoft.bookit.common.ccs.SystemConfig;
import com.derbysoft.bookit.common.model.SystemConfigKeys;
import com.derbysoft.bookit.common.repository.SystemConfigRepository;
import com.derbysoft.bookit.common.translator.Translator;
import com.derbysoft.bookit.dto.*;
import com.derbysoft.common.util.Constants;
import com.derbysoft.dswitch.dto.hotel.cds.LOSInventoryDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component("inventoryChangePushRQTranslator")
public class InventoryChangePushRQTranslator extends BaseTranslator implements Translator<List<LOSInventoryDTO>, String, Map<String, OTAHotelAvailNotifRQ>> {

    public static final int DEFAULT_CHANGES_DATE_LENGTH = 7;
    @Autowired
    private SystemConfigRepository systemConfigRepository;

    @Override
    public Map<String, OTAHotelAvailNotifRQ> translate(List<LOSInventoryDTO> inventoryList, String hotelCode) {
        Map<String, OTAHotelAvailNotifRQ> hotelAvailNotifRQHashMap = new LinkedHashMap<String, OTAHotelAvailNotifRQ>();
        int changesDateRange = getChangesDateLength();
        for (LOSInventoryDTO losInventoryDTO : inventoryList) {
            OTAHotelAvailNotifRQ otaHotelAvailNotifRQ = hotelAvailNotifRQHashMap.get(losInventoryDTO.getCheckInDate());
            if (otaHotelAvailNotifRQ == null) {
                otaHotelAvailNotifRQ = new OTAHotelAvailNotifRQ();
                hotelAvailNotifRQHashMap.put(losInventoryDTO.getCheckInDate(), otaHotelAvailNotifRQ);
            }
            if (otaHotelAvailNotifRQ.getAvailStatusMessages() == null) {
                AvailStatusMessagesType availStatusMessages = new AvailStatusMessagesType();
                availStatusMessages.setHotelCode(hotelCode);
                otaHotelAvailNotifRQ.setAvailStatusMessages(availStatusMessages);
            }
            otaHotelAvailNotifRQ.getAvailStatusMessages().getAvailStatusMessage().addAll(createAvailStatusMessages(changesDateRange, losInventoryDTO));
            setHeader(otaHotelAvailNotifRQ);
        }
        return hotelAvailNotifRQHashMap;
    }

    private int getChangesDateLength() {
        SystemConfig systemConfig = systemConfigRepository.load(SystemConfigKeys.CHANGES_DATE_LENGTH.getKey());
        if (systemConfig == null) {
            return DEFAULT_CHANGES_DATE_LENGTH;
        }
        return Integer.parseInt(systemConfig.getValue());
    }

    private List<AvailStatusMessageType> createAvailStatusMessages(int changesDateRange, LOSInventoryDTO losInventoryDTO) {
        ArrayList<AvailStatusMessageType> availStatusMessageTypes = new ArrayList<AvailStatusMessageType>();
        String fplos = losInventoryDTO.getFplos();
        String[] inventories = StringUtils.split(fplos, Constants.COMMA);
        int endIndex = inventories.length > changesDateRange ? changesDateRange : inventories.length;
        for (int index = 0; index < endIndex; index++) {
            AvailStatusMessageType availStatusMessageType = new AvailStatusMessageType();
            availStatusMessageType.setBookingLimitMessageType("SetLimit");
            availStatusMessageType.setBookingLimit(Integer.parseInt(inventories[index]));
            StatusApplicationControl statusApplicationControl = new StatusApplicationControl();
            statusApplicationControl.setStart(losInventoryDTO.getCheckInDate());
            statusApplicationControl.setEnd(losInventoryDTO.getCheckInDate());
            statusApplicationControl.setDuration(String.format("P%dD", index + 1));
            statusApplicationControl.setRatePlanCodeType(RATE_PLAN_TYPE);
            statusApplicationControl.setRatePlanCode(losInventoryDTO.getRatePlanCode());
            statusApplicationControl.setInvCodeApplication(ROOM_TYPE_TYPE);
            statusApplicationControl.setInvCode(losInventoryDTO.getRoomTypeCode());
            availStatusMessageType.setStatusApplicationControl(statusApplicationControl);
            availStatusMessageTypes.add(availStatusMessageType);
        }
        return availStatusMessageTypes;
    }

    public void setSystemConfigRepository(SystemConfigRepository systemConfigRepository) {
        this.systemConfigRepository = systemConfigRepository;
    }
}
