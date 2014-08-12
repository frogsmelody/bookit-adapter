package com.derbysoft.bookit.common.commons.log;

import com.derbysoft.bookit.common.commons.DateTimeUtils;
import org.apache.log4j.FileAppender;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by: jason
 * Date: 2012-06-11
 */
public class PerformanceDailyAppender extends FileAppender {
    private static final ThreadLocal<Date> STANDARD_LOG_START_TIME = new ThreadLocal<Date>();
    private static final TimeZone GMT_TIME_ZONE = TimeZone.getTimeZone("GMT+00:00");

    public static void setStandardLogStartTime(Date time) {
        STANDARD_LOG_START_TIME.set(time);
    }

    private String now;
    private String originalFileName;

    @Override
    public void activateOptions() {
        if (fileName != null) {
            String utcTime = DateTimeUtils.formatDate(new Date(), DateTimeUtils.FULL_DATE_TIME_PATTERN, GMT_TIME_ZONE);
            now = DateTimeUtils.formatDate(DateTimeUtils.parse(utcTime, DateTimeUtils.FULL_DATE_TIME_PATTERN), DateTimeUtils.DATE_FORMAT);
            createNewLogFile();
        } else {
            LogLog.error("Either File option is not set for appender [" + name + "].");
        }
    }

    @Override
    public void setFile(String file) {
        super.setFile(file);
        originalFileName = fileName;
    }

    private String getFilename() {
        return originalFileName + "." + now;
    }

    void rollOver() throws IOException {
        closeFile();
        createNewLogFile();
    }

    private void createNewLogFile() {
        String filename = getFilename();
        try {
            this.setFile(filename, fileAppend, bufferedIO, bufferSize);
        } catch (IOException e) {
            errorHandler.error("setFile(" + filename + ", " + fileAppend + ") call failed.");
        }
    }

    @Override
    protected void subAppend(LoggingEvent event) {
        String utcTime = DateTimeUtils.formatDate(STANDARD_LOG_START_TIME.get(), DateTimeUtils.FULL_DATE_TIME_PATTERN, GMT_TIME_ZONE);
        String n = DateTimeUtils.formatDate(DateTimeUtils.parse(utcTime, DateTimeUtils.FULL_DATE_TIME_PATTERN), DateTimeUtils.DATE_FORMAT);
        if (!n.equals(now)) {
            now = n;
            try {
                rollOver();
            } catch (IOException ioe) {
                LogLog.error("rollOver() failed.", ioe);
            }
        }
        super.subAppend(event);
    }
}
