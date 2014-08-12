package com.derbysoft.bookit.push.job.service.impl;

import com.derbysoft.bookit.common.translator.Translator;
import com.derbysoft.bookit.dto.OTAHotelRateAmountNotifRQ;
import com.derbysoft.bookit.push.commons.models.LosRateChangePushRQPair;
import com.derbysoft.bookit.push.commons.models.NotifyRequestsPair;
import com.derbysoft.bookit.push.job.component.FailRetryPushService;
import com.derbysoft.bookit.push.job.component.NotifyRQPaginator;
import com.derbysoft.bookit.push.job.service.LOSRatePushService;
import com.derbysoft.bookit.push.job.support.ChangeNotifyPair;
import com.derbysoft.bookit.push.job.support.ChangeType;
import com.derbysoft.bookit.push.job.support.ChangesPushTask;
import com.derbysoft.common.util.Constants;
import com.derbysoft.dswitch.dto.hotel.cds.LOSRateChangeDTO;
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
public class LOSRatePushServiceImpl implements LOSRatePushService {
    @Autowired
    @Qualifier("jaxb2Marshaller")
    private Jaxb2Marshaller jaxb2Marshaller;

    @Autowired
    private Translator<List<LOSRateChangeDTO>, String, Map<String, OTAHotelRateAmountNotifRQ>> rateChangePushRQTranslator;

    @Autowired
    private NotifyRQPaginator<OTAHotelRateAmountNotifRQ> losRateNotifyRQPaginator;

    @Autowired
    private FailRetryPushService failRetryPushService;

    @Override
    public boolean push(List<LOSRateChangeDTO> losRateChanges, ChangesPushTask changesPushTask) {
        Map<String, List<LOSRateChangeDTO>> roomStayMap = groupByRoomStay(losRateChanges);
        for (Map.Entry<String, List<LOSRateChangeDTO>> entry : roomStayMap.entrySet()) {
            String fields[] = StringUtils.split(entry.getKey(), Constants.COLON);
            ChangeNotifyPair changeNotifyPair = ChangeNotifyPair.build(fields[0], fields[1], ChangeType.LOS_RATE);
            //TODO mock hotel code
            Map<String, OTAHotelRateAmountNotifRQ> otaHotelRateAmountNotifRQs = rateChangePushRQTranslator.translate(entry.getValue(), "1727782");
            List<NotifyRequestsPair<OTAHotelRateAmountNotifRQ>> rateAmountNotifyRequests = losRateNotifyRQPaginator.paginate(otaHotelRateAmountNotifRQs);
            for (NotifyRequestsPair<OTAHotelRateAmountNotifRQ> notifyRequestList : rateAmountNotifyRequests) {
                if (!failRetryPushService.push(changeNotifyPair, changesPushTask, createRequestPairs(notifyRequestList))) {
                    return false;
                }
            }
        }
        return true;
    }

    private Map<String, List<LOSRateChangeDTO>> groupByRoomStay(List<LOSRateChangeDTO> losRateChanges) {
        Map<String, List<LOSRateChangeDTO>> roomStayMap = new HashMap<String, List<LOSRateChangeDTO>>();
        for (LOSRateChangeDTO losRateChange : losRateChanges) {
            String key = String.format("%s:%s", losRateChange.getRatePlanCode(), losRateChange.getRoomTypeCode());
            List<LOSRateChangeDTO> losRateChangeDTOs = roomStayMap.get(key);
            if (losRateChangeDTOs == null) {
                losRateChangeDTOs = new ArrayList<LOSRateChangeDTO>();
                roomStayMap.put(key, losRateChangeDTOs);
            }
            losRateChangeDTOs.add(losRateChange);
        }
        return roomStayMap;
    }

    private List<LosRateChangePushRQPair> createRequestPairs(NotifyRequestsPair<OTAHotelRateAmountNotifRQ> rateAmountNotifyRequest) {
        List<LosRateChangePushRQPair> losRateChangePushRQPairs = new ArrayList<LosRateChangePushRQPair>();
        for (OTAHotelRateAmountNotifRQ otaHotelRateAmountNotifRQ : rateAmountNotifyRequest.getNotifyRequests()) {
            Writer writer = new StringWriter();
            jaxb2Marshaller.marshal(otaHotelRateAmountNotifRQ, new StreamResult(writer));
            String request = writer.toString();
            String checkIn = otaHotelRateAmountNotifRQ.getRateAmountMessages().getRateAmountMessage().get(0).getStatusApplicationControl().getStart();
            losRateChangePushRQPairs.add(LosRateChangePushRQPair.build(checkIn, request));
        }
        return losRateChangePushRQPairs;
    }

    public void setJaxb2Marshaller(Jaxb2Marshaller jaxb2Marshaller) {
        this.jaxb2Marshaller = jaxb2Marshaller;
    }

    public void setRateChangePushRQTranslator(Translator<List<LOSRateChangeDTO>, String, Map<String, OTAHotelRateAmountNotifRQ>> rateChangePushRQTranslator) {
        this.rateChangePushRQTranslator = rateChangePushRQTranslator;
    }

    public void setLosRateNotifyRQPaginator(NotifyRQPaginator<OTAHotelRateAmountNotifRQ> losRateNotifyRQPaginator) {
        this.losRateNotifyRQPaginator = losRateNotifyRQPaginator;
    }

    public void setFailRetryPushService(FailRetryPushService failRetryPushService) {
        this.failRetryPushService = failRetryPushService;
    }
}
