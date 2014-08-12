package com.derbysoft.bookit.push.translator;

import com.derbysoft.bookit.common.translator.Translator;
import com.derbysoft.bookit.dto.*;
import com.derbysoft.dswitch.dto.hotel.cds.LOSRateChangeDTO;
import com.derbysoft.dswitch.dto.hotel.cds.LOSRateDTO;
import com.derbysoft.dswitch.dto.hotel.cds.OccupancyRateDTO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component("rateChangePushRQTranslator")
public class RateChangePushRQTranslator extends BaseTranslator implements Translator<List<LOSRateChangeDTO>, String, Map<String, OTAHotelRateAmountNotifRQ>> {

    @Override
    public Map<String, OTAHotelRateAmountNotifRQ> translate(List<LOSRateChangeDTO> losRateChangeDTOs, String hotelCode) {
        HashMap<String, OTAHotelRateAmountNotifRQ> rateAmountNotifRQMapByCheckIn = new LinkedHashMap<String, OTAHotelRateAmountNotifRQ>();
        for (LOSRateChangeDTO losRateChangeDTO : losRateChangeDTOs) {
            if (rateAmountNotifRQMapByCheckIn.containsKey(losRateChangeDTO.getCheckInDate())) {
                continue;
            }
            OTAHotelRateAmountNotifRQ otaHotelRateAmountNotifRQ = translateRateNotifyRQ(hotelCode, losRateChangeDTO);
            if (otaHotelRateAmountNotifRQ != null) {
                rateAmountNotifRQMapByCheckIn.put(losRateChangeDTO.getCheckInDate(), otaHotelRateAmountNotifRQ);
            }
        }
        return rateAmountNotifRQMapByCheckIn;
    }

    private OTAHotelRateAmountNotifRQ translateRateNotifyRQ(String hotelCode, LOSRateChangeDTO losRateChangeDTO) {
        List<RateAmountMessage> rateAmountMessages = translateRateAmountMessages(losRateChangeDTO);
        if (rateAmountMessages.isEmpty()) {
            return null;
        }
        OTAHotelRateAmountNotifRQ otaHotelRateAmountNotifRQ = new OTAHotelRateAmountNotifRQ();
        RateAmountMessages amountMessages = new RateAmountMessages();
        amountMessages.setHotelCode(hotelCode);
        amountMessages.getRateAmountMessage().addAll(rateAmountMessages);
        otaHotelRateAmountNotifRQ.setRateAmountMessages(amountMessages);
        setHeader(otaHotelRateAmountNotifRQ);
        return otaHotelRateAmountNotifRQ;
    }

    private List<RateAmountMessage> translateRateAmountMessages(LOSRateChangeDTO losRateChangeDTO) {
        List<RateAmountMessage> rateAmountMessages = new ArrayList<RateAmountMessage>();
        for (LOSRateDTO losRateDTO : losRateChangeDTO.getLosRatesList()) {
            if (isClosed(losRateDTO)) {
                continue;
            }
            rateAmountMessages.add(createRateMessage(losRateDTO, losRateChangeDTO));
        }
        return rateAmountMessages;
    }

    private RateAmountMessage createRateMessage(LOSRateDTO losRateDTO, LOSRateChangeDTO losRateChangeDTO) {
        Rates rates = new Rates();
        Rate rate = new Rate();
        rate.setNumberOfUnits(1);
        rate.setRateTimeUnit("Day");
        rate.setUnitMultiplier(1);
        rate.setDecimalPlaces(2);
        rate.setBaseByGuestAmts(createBaseByGuestAmts(losRateDTO.getRatesList().get(0).getOccupancyRatesList(), losRateChangeDTO.getCurrency()));
        rates.getRate().add(rate);
        RateAmountMessage rateAmountMessage = new RateAmountMessage();
        rateAmountMessage.setRates(rates);
        rateAmountMessage.setStatusApplicationControl(createStatusApplicationControl(losRateChangeDTO, losRateDTO.getLos()));
        return rateAmountMessage;
    }

    private boolean isClosed(LOSRateDTO losRateDTO) {
        return CollectionUtils.isEmpty(losRateDTO.getRatesList())
                || losRateDTO.getRatesList().get(0).getOccupancyRatesList() == null
                || CollectionUtils.isEmpty(losRateDTO.getRatesList().get(0).getOccupancyRatesList());
    }

    private BaseByGuestAmts createBaseByGuestAmts(List<OccupancyRateDTO> occupancyRates, String currencyCode) {
        BaseByGuestAmts baseByGuestAmts = new BaseByGuestAmts();
        String singleRate = getRateByGuestCount(1, occupancyRates);
        if (singleRate != null) {
            baseByGuestAmts.getBaseByGuestAmt().add(createBaseByGuestAmt(singleRate, 1, currencyCode));
        }
        String doubleRate = getRateByGuestCount(2, occupancyRates);
        if (doubleRate != null) {
            baseByGuestAmts.getBaseByGuestAmt().add(createBaseByGuestAmt(doubleRate, 2, currencyCode));
        }
        String trebleRate = getRateByGuestCount(3, occupancyRates);
        if (trebleRate != null) {
            baseByGuestAmts.getBaseByGuestAmt().add(createBaseByGuestAmt(trebleRate, 3, currencyCode));
        }
        String quadrupleRate = getRateByGuestCount(4, occupancyRates);
        if (quadrupleRate != null) {
            baseByGuestAmts.getBaseByGuestAmt().add(createBaseByGuestAmt(quadrupleRate, 4, currencyCode));
        }
        return baseByGuestAmts;
    }

    private String getRateByGuestCount(int guestCount, List<OccupancyRateDTO> occupancyRates) {
        for (OccupancyRateDTO occupancyRate : occupancyRates) {
            if (occupancyRate.getAdult() + occupancyRate.getChild() == guestCount) {
                return String.valueOf(occupancyRate.getAmountBeforeTax());
            }
        }
        return null;
    }

    private BaseByGuestAmt createBaseByGuestAmt(String amountBeforeTax, int guestCount, String currencyCode) {
        BaseByGuestAmt baseByGuestAmt = new BaseByGuestAmt();
        baseByGuestAmt.setCurrencyCode(currencyCode);
        baseByGuestAmt.setNumberOfGuests(guestCount);
        baseByGuestAmt.setAmountBeforeTax(new BigDecimal(amountBeforeTax).scaleByPowerOfTen(2).toString());
        return baseByGuestAmt;
    }

    private StatusApplicationControl createStatusApplicationControl(LOSRateChangeDTO losRateChangeDTO, int los) {
        StatusApplicationControl statusApplicationControl = new StatusApplicationControl();
        statusApplicationControl.setStart(losRateChangeDTO.getCheckInDate());
        statusApplicationControl.setEnd(losRateChangeDTO.getCheckInDate());
        statusApplicationControl.setDuration(String.format("P%dD", los));
        statusApplicationControl.setRatePlanCodeType(RATE_PLAN_TYPE);
        statusApplicationControl.setRatePlanCode(losRateChangeDTO.getRatePlanCode());
        statusApplicationControl.setInvCodeApplication(ROOM_TYPE_TYPE);
        statusApplicationControl.setInvCode(losRateChangeDTO.getRoomTypeCode());
        return statusApplicationControl;
    }
}
