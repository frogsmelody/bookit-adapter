package com.derbysoft.bookit.push.translator;

import com.derbysoft.bookit.common.commons.DateTimeUtils;
import com.derbysoft.bookit.dto.*;
import com.derbysoft.common.util.date.LocalDateTimes;
import com.derbysoft.dswitch.remote.hotel.dto.RequestHeader;
import org.apache.commons.lang.time.FastDateFormat;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public abstract class BaseTranslator {
    protected static final String RATE_PLAN_TYPE = "RatePlanCode";
    protected static final String ROOM_TYPE_TYPE = "InvCode";
    private static final String TIME_STAMP_PATTERN = "yyyy-MM-dd'T'HH:mm:ss%s:00";

    @Value("#{properties['channel.code']}")
    protected String channelCode;

    @Value("#{properties['provider.code']}")
    protected String providerCode;

    protected RequestHeader createHeader() {
        RequestHeader requestHeader = new RequestHeader();
        requestHeader.setTaskId(UUID.randomUUID().toString().toLowerCase());
        requestHeader.setSource(channelCode);
        requestHeader.setDestination(providerCode);
        return requestHeader;
    }

    protected <T extends OTAMessage> void setHeader(T changeNotifyRQ) {
        changeNotifyRQ.setVersion("1.000");
        changeNotifyRQ.setPrimaryLangID("en-us");
        changeNotifyRQ.setTarget("PROD");
        changeNotifyRQ.setTimeStamp(formatDateTime(new Date()));
        POS pos = new POS();
        Source source = new Source();
        RequestorID requestorID = new RequestorID();
        requestorID.setType("PROD");
        requestorID.setID(channelCode);
        source.setRequestorID(requestorID);
        BookingChannel bookingChannel = new BookingChannel();
        bookingChannel.setType("2");
        bookingChannel.setPrimary("true");
        bookingChannel.setCompanyName(channelCode);
        source.setBookingChannel(bookingChannel);
        pos.setSource(source);
        changeNotifyRQ.setPOS(pos);
    }

    public static String formatDateTime(Date date) {
        return FastDateFormat.getInstance(String.format(TIME_STAMP_PATTERN, getOffset())).format(date);
    }

    private static String getOffset() {
        Calendar calendar = Calendar.getInstance();
        int offsetHours = calendar.getTimeZone().getRawOffset() / (60 * 60 * 1000);
        String prefix = offsetHours > 0 ? "'+'" : "'-'";
        if (Math.abs(offsetHours) > 9) {
            return prefix + Math.abs(offsetHours);
        }
        return prefix + "0" + Math.abs(offsetHours);
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }
}
