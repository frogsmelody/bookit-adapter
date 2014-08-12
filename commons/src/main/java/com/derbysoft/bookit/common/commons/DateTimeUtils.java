package com.derbysoft.bookit.common.commons;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.time.FastDateFormat;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public abstract class DateTimeUtils {
    public static final int AMOUNT = 60000;
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String FULL_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    public static Date parse(String input) {
        if (StringUtils.isBlank(input)) {
            return null;
        }

        if (input.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
            return parse(input, DATE_FORMAT);
        } else if (input.matches("\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}")) {
            return parse(input, DATE_TIME_FORMAT);
        } else if (input.matches("\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{3}")) {
            return parse(input, FULL_DATE_TIME_PATTERN);
        } else {
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
                return dateFormat.parse(input);
            } catch (ParseException e) {
                return null;
            }
        }
    }

    public static Date parse(String input, String pattern) {
        Validate.notEmpty(input);
        Validate.notEmpty(pattern);
        return DateTimeFormat.forPattern(pattern).parseDateTime(input).toDate();
    }

    public static Date today() {
        return DateTimeUtils.parse(formatDate(new Date()));
    }

    public static int getIntervalDays(Date begin, Date end) {
        Validate.notNull(begin);
        Validate.notNull(end);
        return new Period(
                begin.getTime(),
                end.getTime(),
                PeriodType.days()).getDays();
    }

    public static int getIntervalDays(String begin, String end) {
        Validate.notNull(begin);
        Validate.notNull(end);
        return getIntervalDays(parse(begin), parse(end));
    }

    public static Date addDays(Date date, int days) {
        return new DateTime(date.getTime()).plusDays(days).toDate();
    }

    public static String addDays(String date, int days) {
        return formatDate(addDays(parse(date), days));
    }

    public static Date addDays(int days) {
        return addDays(new Date(), days);
    }

    public static String getDateString(int offsetDaysFromNow) {
        return formatDate(addDays(new Date(), offsetDaysFromNow));
    }

    public static String formatDate(Date date) {
        return formatDateTime(date, DATE_FORMAT);
    }

    public static String formatDate(Date date, String format) {
        return formatDateTime(date, format);
    }

    public static String formatDate(Date date, String format, TimeZone timeZone) {
        if (date == null) {
            return null;
        } else {
            SimpleDateFormat df = getSimpleDateFormat(format);
            if (timeZone != null) {
                df.setTimeZone(timeZone);
            }
            return df.format(date);
        }
    }

    private static SimpleDateFormat getSimpleDateFormat(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setLenient(false);
        return simpleDateFormat;
    }

    public static String formatDateTime(Date dateTime) {
        return formatDateTime(dateTime, DATE_TIME_FORMAT);
    }

    public static String formatTime(Date time) {
        return formatDateTime(time, TIME_FORMAT);
    }

    public static String formatDateTime(Date dateTime, String format) {
        if (dateTime == null) {
            return null;
        }
        return FastDateFormat.getInstance(format).format(dateTime);
    }

    public static Date getStartOfMonth(int year, int month) {
        DateTime dateTime = new DateTime(year, month, 1, 0, 0, 0, 0);
        return dateTime.toDate();
    }

    public static Date getEndOfMonth(int year, int month) {
        DateTime dateTime = new DateTime(year, month, 1, 0, 0, 0, 0);
        dateTime = dateTime.plusMonths(1).minusMillis(1);
        return dateTime.toDate();
    }

    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static int getYear() {
        return getYear(new Date());
    }

    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMinimum(Calendar.MILLISECOND));
        return calendar.getTime();
    }

    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMaximum(Calendar.MILLISECOND));
        return calendar.getTime();
    }

    public static Date fromXML(XMLGregorianCalendar gregorianCalendar) {
        if (gregorianCalendar == null) {
            return null;
        }

        Calendar calendar = gregorianCalendar.toGregorianCalendar();
        return calendar.getTime();
    }

    public static Date addHours(Date date, int hours) {
        return new DateTime(date.getTime()).plusHours(hours).toDate();
    }

    public static Date addMinute(Date date, int minutes) {
        return new DateTime(date.getTime()).plusMinutes(minutes).toDate();
    }


    public static Date max(Date date1, Date date2) {
        return date1.after(date2) ? date1 : date2;
    }

    public static Date min(Date date1, Date date2) {
        return date1.before(date2) ? date1 : date2;
    }
}
