package com.derbysoft.bookit.push.web.action.webservice;
import com.derbysoft.bookit.common.commons.XMLUtils;
import com.derbysoft.common.util.Constants;
import com.derbysoft.dswitch.dto.hotel.cds.LOSRateChangeRQ;
import com.derbysoft.dswitch.dto.hotel.cds.Type;
import com.derbysoft.dswitch.dto.hotel.common.DateRangeDTO;
import com.derbysoft.dswitch.remote.hotel.buyer.HotelBuyerRemoteService;
import com.derbysoft.dswitch.remote.hotel.dto.HotelLOSRateChangeRequest;
import com.derbysoft.dswitch.remote.hotel.dto.RequestHeader;
import com.derbysoft.synchronizer.dto.GetChangesRQ;
import com.derbysoft.synchronizer.dto.GetKeysRQ;
import com.derbysoft.synchronizer.dto.Level;
import com.derbysoft.synchronizer.remote.common.SynchronizerRemoteService;
import com.derbysoft.synchronizer.remote.dto.GetChangesRequest;
import com.derbysoft.synchronizer.remote.dto.GetKeysRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Author: Jason Wu
 * Date  : 2013-11-18
 */
@Service
public class DswitchWebService {
    private static final String SOURCE = "Bookit";

    @Value("standard.synchronizer")
    private String standardSynchronizer;

    @Value("dswitch.get.cds.key")
    private String dswitchGetCDSKey;

    @Autowired
    @Qualifier("hotelBuyerRemoteService")
    private HotelBuyerRemoteService rateChangeWebService;

    @Autowired
    private SynchronizerRemoteService synchronizerRemoteService;

    public String getLosRateChange(WebServiceCondition condition) {
        return XMLUtils.toXML(rateChangeWebService.getLOSRateChange(createLosRateChangeRequest(condition)));
    }

    public String getKeys(WebServiceCondition condition) {
        return XMLUtils.toXML(synchronizerRemoteService.getKeys(createGetKeyRequests(condition)));
    }

    public String getChanges(WebServiceCondition condition) {
        return XMLUtils.toXML(synchronizerRemoteService.getChanges(createGetChangeRequests(condition)));
    }

    private GetChangesRequest createGetChangeRequests(WebServiceCondition condition) {
        GetChangesRequest getChangesRequest = new GetChangesRequest();
        getChangesRequest.setHeader(createGetChangesKeysHeader());
        GetChangesRQ getChangesRQ = new GetChangesRQ();
        if (StringUtils.isBlank(condition.getHotelCodes())) {
            throw new IllegalArgumentException("HotelCode is blank.");
        }
        getChangesRQ.setHotel(condition.getHotelCodes());
        if (StringUtils.isBlank(condition.getKeys())) {
            throw new IllegalArgumentException("GetChangeKeys is blank.");
        }
        getChangesRQ.setKeysList(new ArrayList<String>());
        Collections.addAll(getChangesRQ.getKeysList(), StringUtils.split(condition.getKeys(), Constants.COMMA));
        getChangesRequest.setGetChangesRQ(getChangesRQ);
        return getChangesRequest;
    }

    private GetKeysRequest createGetKeyRequests(WebServiceCondition webServiceCondition) {
        GetKeysRequest getKeysRequest = new GetKeysRequest();
        getKeysRequest.setHeader(createGetChangesKeysHeader());
        GetKeysRQ getKeysRQ = new GetKeysRQ();
        if (StringUtils.isBlank(webServiceCondition.getLevel()) || StringUtils.equalsIgnoreCase(webServiceCondition.getLevel(), Level.RATE.name())) {
            getKeysRQ.setLevel(Level.RATE);
        }
        if (StringUtils.equalsIgnoreCase(webServiceCondition.getLevel(), Level.RATE_MONTH.name())) {
            getKeysRQ.setLevel(Level.RATE_MONTH);
        }
        if (StringUtils.isNotBlank(webServiceCondition.getHotelCodes())) {
            List<String> hotelCodes = new ArrayList<String>();
            Collections.addAll(hotelCodes, StringUtils.split(webServiceCondition.getHotelCodes(), Constants.COMMA));
            getKeysRQ.setHotelsList(hotelCodes);
        }

        getKeysRequest.setGetKeysRQ(getKeysRQ);
        return getKeysRequest;
    }

    private RequestHeader createGetChangesKeysHeader() {
        RequestHeader header = new RequestHeader();
        header.setTaskId(UUID.randomUUID().toString());
        header.setSource(SOURCE);
        header.setDestination(standardSynchronizer);
        return header;
    }

    private RequestHeader createGetLosHeader() {
        RequestHeader header = new RequestHeader();
        header.setTaskId(UUID.randomUUID().toString());
        header.setSource(SOURCE);
        header.setDestination(dswitchGetCDSKey);
        return header;
    }

    private HotelLOSRateChangeRequest createLosRateChangeRequest(WebServiceCondition webServiceCondition) {
        HotelLOSRateChangeRequest request = new HotelLOSRateChangeRequest();
        request.setHeader(createGetLosHeader());
        request.setLosRateChangeRQ(createLosRateChangeRQ(webServiceCondition));
        return request;
    }

    private LOSRateChangeRQ createLosRateChangeRQ(WebServiceCondition webServiceCondition) {
        LOSRateChangeRQ losRateChangeRQ = new LOSRateChangeRQ();
        losRateChangeRQ.setHotelCode(webServiceCondition.getHotelCodes());
        losRateChangeRQ.setType(StringUtils.isBlank(webServiceCondition.getType()) ? Type.Delta : Type.valueOf(Integer.parseInt(webServiceCondition.getType())));
        losRateChangeRQ.setTimestamp(webServiceCondition.getTimestamp());
        losRateChangeRQ.setDateRange(new DateRangeDTO(webServiceCondition.getStart(), webServiceCondition.getEnd()));
        if (StringUtils.isNotBlank(webServiceCondition.getLosList())) {
            ArrayList<Integer> losList = new ArrayList<Integer>();
            for (String los : StringUtils.split(webServiceCondition.getLosList(), Constants.COMMA)) {
                losList.add(Integer.parseInt(los));
            }
            losRateChangeRQ.setLosList(losList);
        }
        if (StringUtils.isNotBlank(webServiceCondition.getRatePlans())) {
            ArrayList<String> ratePlanList = new ArrayList<String>();
            Collections.addAll(ratePlanList, StringUtils.split(webServiceCondition.getRatePlans(), Constants.COMMA));
            losRateChangeRQ.setRatePlansList(ratePlanList);
        }
        if (StringUtils.isNotBlank(webServiceCondition.getRoomTypes())) {
            ArrayList<String> roomTypeList = new ArrayList<String>();
            Collections.addAll(roomTypeList, StringUtils.split(webServiceCondition.getRoomTypes(), Constants.COMMA));
            losRateChangeRQ.setRoomTypesList(roomTypeList);
        }
        return losRateChangeRQ;
    }

    public void setStandardSynchronizer(String standardSynchronizer) {
        this.standardSynchronizer = standardSynchronizer;
    }

    public void setDswitchGetCDSKey(String dswitchGetCDSKey) {
        this.dswitchGetCDSKey = dswitchGetCDSKey;
    }

    public void setRateChangeWebService(HotelBuyerRemoteService rateChangeWebService) {
        this.rateChangeWebService = rateChangeWebService;
    }

    public void setSynchronizerRemoteService(SynchronizerRemoteService synchronizerRemoteService) {
        this.synchronizerRemoteService = synchronizerRemoteService;
    }
}
