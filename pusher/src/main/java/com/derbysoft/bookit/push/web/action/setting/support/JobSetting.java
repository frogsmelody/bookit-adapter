package com.derbysoft.bookit.push.web.action.setting.support;

import com.derbysoft.bookit.common.domain.common.ScheduleStatus;
import org.apache.commons.lang.StringUtils;

public class JobSetting {
    private String jobId;
    private ScheduleStatus scheduleStatus;
    private String startMinute;
    private String perMinute;
    private String startHour;
    private String perHour;

    public JobSetting() {
    }

    public JobSetting(String startMinute, String perMinute, String startHour, String perHour) {
        this.startMinute = startMinute;
        this.perMinute = perMinute;
        this.startHour = startHour;
        this.perHour = perHour;
    }

    public String buildStandardCronExpress() {
        return String.format("0 %s %s * * ?", getPattern(startMinute, perMinute), getPattern(startHour, perHour));
    }

    public static JobSetting fromStandardCronExpress(String standardCronExpress) {
        String[] fields = StringUtils.split(standardCronExpress, " ");
        JobSetting jobSetting = new JobSetting();
        if (fields[1].contains("/")) {
            String[] minuteFields = StringUtils.split(fields[1], "/");
            jobSetting.setStartMinute(minuteFields[0]);
            jobSetting.setPerMinute(minuteFields[1]);
        } else {
            jobSetting.setStartMinute(fields[1]);
        }
        if (fields[2].contains("/")) {
            String[] hourFields = StringUtils.split(fields[2], "/");
            jobSetting.setStartHour(hourFields[0]);
            jobSetting.setPerHour(hourFields[1]);
        } else {
            jobSetting.setStartHour(fields[2]);
        }
        return jobSetting;
    }

    private static String getPattern(String start, String per) {
        if (StringUtils.isNotBlank(start) && StringUtils.isNotBlank(per)) {
            if (per.equals("*") ||per.equals("0")) {
                return start;
            }
            return start + "/" + per;
        } else if (StringUtils.isNotBlank(start)) {
            return start;
        } else if (StringUtils.isNotBlank(per)) {
            return "0/" + per;
        }
        return "*";
    }

    public ScheduleStatus getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(ScheduleStatus scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getStartMinute() {
        return prettyOutput(startMinute);
    }

    public void setStartMinute(String startMinute) {
        this.startMinute = startMinute;
    }

    public String getPerMinute() {
        return prettyOutput(perMinute);
    }

    public void setPerMinute(String perMinute) {
        this.perMinute = perMinute;
    }

    public String getStartHour() {
        return prettyOutput(startHour);
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getPerHour() {
        return prettyOutput(perHour);
    }

    private String prettyOutput(String input) {
        if (StringUtils.isBlank(input)) {
            return "*";
        }
        return input;
    }

    public void setPerHour(String perHour) {
        this.perHour = perHour;
    }
}
