package com.derbysoft.bookit.push.translator;

import com.derbysoft.bookit.dto.BaseByGuestAmt;
import com.derbysoft.bookit.dto.OTAHotelRateAmountNotifRQ;
import com.derbysoft.bookit.dto.Rate;
import com.derbysoft.bookit.dto.RateAmountMessage;
import com.derbysoft.dswitch.dto.hotel.cds.LOSRateChangeDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import utils.XMLTestSupport;

import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class RateChangePushRQTranslatorTest extends XMLTestSupport {
    RateChangePushRQTranslator translator = new RateChangePushRQTranslator();

    @Test
    public void testTranslate() {
        List<LOSRateChangeDTO> losRateChangeDTOs = parseJsonArray(readFile("LOS_RateChange_input_01.json"), LOSRateChangeDTO.class);
        Map<String, OTAHotelRateAmountNotifRQ> otaHotelRateAmountNotifRQs = translator.translate(losRateChangeDTOs, "Hotel-A");
        String actual = formatArrayToJson(otaHotelRateAmountNotifRQs, "timeStamp");
        assertEquals(readFile("LOS_RateChange_output_01.json"), actual);
        String shortMessage = getShortMessage(otaHotelRateAmountNotifRQs);
        assertEquals(readFile("LOSRates_01_expected.txt"), shortMessage);
    }

    @Test
    public void includeNoAvail() throws Exception {
        List<LOSRateChangeDTO> losRateChangeDTOs = parseJsonArray(readFile("LOS_RateChange_input_02.json"), LOSRateChangeDTO.class);
        Map<String, OTAHotelRateAmountNotifRQ> otaHotelRateAmountNotifRQs = translator.translate(losRateChangeDTOs, "Hotel-A");
        String actual = formatArrayToJson(otaHotelRateAmountNotifRQs, "timeStamp");
        assertEquals(readFile("LOS_RateChange_output_02.json"), actual);
        String shortMessage = getShortMessage(otaHotelRateAmountNotifRQs);
        assertEquals(readFile("LOSRates_02_expected.txt"), shortMessage);
    }

    private String getShortMessage(Map<String, OTAHotelRateAmountNotifRQ> otaHotelRateAmountNotifRQs) {
        StringBuilder builder = new StringBuilder();
        for (OTAHotelRateAmountNotifRQ otaHotelRateAmountNotifRQ : otaHotelRateAmountNotifRQs.values()) {
            for (RateAmountMessage rateAmountMessage : otaHotelRateAmountNotifRQ.getRateAmountMessages().getRateAmountMessage()) {
                builder.append(rateAmountMessage.getStatusApplicationControl().getStart()).append("|");
                builder.append(rateAmountMessage.getStatusApplicationControl().getRatePlanCode()).append("|");
                builder.append(rateAmountMessage.getStatusApplicationControl().getInvCode()).append("|");
                builder.append(rateAmountMessage.getStatusApplicationControl().getDuration()).append("|");
                if (rateAmountMessage.getRates() != null && CollectionUtils.isNotEmpty(rateAmountMessage.getRates().getRate())) {
                    builder.append(getRates(rateAmountMessage.getRates().getRate()));
                }
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    private String getRates(List<Rate> rates) {
        if (CollectionUtils.isEmpty(rates) || rates.get(0).getBaseByGuestAmts() == null || CollectionUtils.isEmpty(rates.get(0).getBaseByGuestAmts().getBaseByGuestAmt())) {
            return "";
        }
        StringBuilder build = new StringBuilder();
        for (BaseByGuestAmt baseByGuestAmt : rates.get(0).getBaseByGuestAmts().getBaseByGuestAmt()) {
            build.append(baseByGuestAmt.getNumberOfGuests()).append(":");
            build.append(baseByGuestAmt.getAmountBeforeTax()).append(",");
        }
        return StringUtils.removeEnd(build.toString(), ",");
    }
}
