package com.derbysoft.bookit.push.job.service.impl;

import com.derbysoft.bookit.common.translator.Translator;
import com.derbysoft.bookit.dto.OTAHotelAvailNotifRQ;
import com.derbysoft.bookit.push.commons.models.LosRateChangePushRQPair;
import com.derbysoft.bookit.push.commons.models.NotifyRequestsPair;
import com.derbysoft.bookit.push.job.component.FailRetryPushService;
import com.derbysoft.bookit.push.job.component.NotifyRQPaginator;
import com.derbysoft.bookit.push.job.service.LOSInventoryPushService;
import com.derbysoft.bookit.push.job.support.ChangeNotifyPair;
import com.derbysoft.bookit.push.job.support.ChangeType;
import com.derbysoft.bookit.push.job.support.ChangesPushTask;
import com.derbysoft.common.util.Constants;
import com.derbysoft.dswitch.dto.hotel.cds.LOSInventoryDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LOSInventoryPushServiceImpl implements LOSInventoryPushService {
    @Autowired
    @Qualifier("jaxb2Marshaller")
    private Jaxb2Marshaller jaxb2Marshaller;

    @Autowired
    private Translator<List<LOSInventoryDTO>, String, Map<String, OTAHotelAvailNotifRQ>> inventoryChangePushRQTranslator;

    @Autowired
    private NotifyRQPaginator<OTAHotelAvailNotifRQ> losInventoryNotifyRQPaginator;

    @Autowired
    private FailRetryPushService failRetryPushService;

    @Override
    public boolean push(List<LOSInventoryDTO> inventoryChanges, ChangesPushTask task) {
        Map<String, List<LOSInventoryDTO>> losInventoryMap = groupByRatePlanRoomType(inventoryChanges);
        for (Map.Entry<String, List<LOSInventoryDTO>> entry : losInventoryMap.entrySet()) {
            String fields[] = StringUtils.split(entry.getKey(), Constants.COLON);
            ChangeNotifyPair changeNotifyPair = ChangeNotifyPair.build(fields[0], fields[1], ChangeType.LOS_INVENTORY);
            List<LOSInventoryDTO> losInventoryDTOs = entry.getValue();

            //TODO mock hotel code
            Map<String, OTAHotelAvailNotifRQ> availNotifRQMapByCheckIn = inventoryChangePushRQTranslator.translate(losInventoryDTOs, "1727782");

            List<NotifyRequestsPair<OTAHotelAvailNotifRQ>> hotelAvailNotifyRequestses = losInventoryNotifyRQPaginator.paginate(availNotifRQMapByCheckIn);
            for (NotifyRequestsPair<OTAHotelAvailNotifRQ> hotelAvailNotifyRequest : hotelAvailNotifyRequestses) {
                if (!failRetryPushService.push(changeNotifyPair, task, createRequestPairs(hotelAvailNotifyRequest))) {
                    return false;
                }
            }
        }
        return false;
    }

    private List<LosRateChangePushRQPair> createRequestPairs(NotifyRequestsPair<OTAHotelAvailNotifRQ> hotelAvailNotifyRequest) {
        List<LosRateChangePushRQPair> losRateChangePushRQPairs = new ArrayList<LosRateChangePushRQPair>();
        for (OTAHotelAvailNotifRQ otaHotelAvailNotifRQ : hotelAvailNotifyRequest.getNotifyRequests()) {
            Writer writer = new StringWriter();
            jaxb2Marshaller.marshal(otaHotelAvailNotifRQ, new StreamResult(writer));
            String request = writer.toString();
            String checkIn = otaHotelAvailNotifRQ.getAvailStatusMessages().getAvailStatusMessage().get(0).getStatusApplicationControl().getStart();
            losRateChangePushRQPairs.add(LosRateChangePushRQPair.build(checkIn, request));
        }
        return losRateChangePushRQPairs;
    }

    private Map<String, List<LOSInventoryDTO>> groupByRatePlanRoomType(List<LOSInventoryDTO> inventoryChanges) {
        Map<String, List<LOSInventoryDTO>> losInventoryMap = new HashMap<String, List<LOSInventoryDTO>>();
        for (LOSInventoryDTO inventoryChange : inventoryChanges) {
            String key = String.format("%s:%s", inventoryChange.getRatePlanCode(), inventoryChange.getRoomTypeCode());
            List<LOSInventoryDTO> losInventoryDTOs = losInventoryMap.get(key);
            if (losInventoryDTOs == null) {
                losInventoryDTOs = new ArrayList<LOSInventoryDTO>();
                losInventoryMap.put(key, losInventoryDTOs);
            }
            losInventoryDTOs.add(inventoryChange);
        }
        return losInventoryMap;
    }
}
