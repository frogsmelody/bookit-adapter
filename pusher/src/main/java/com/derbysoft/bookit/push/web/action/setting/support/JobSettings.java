package com.derbysoft.bookit.push.web.action.setting.support;

/**
 * Author: Jason Wu
 * Date  : 2013-09-26
 */
public class JobSettings {
    private JobSetting pushRateChangeJob;
    private JobSetting generateLOSRatePushTaskJob;

    public JobSetting getGenerateLOSRatePushTaskJob() {
        return generateLOSRatePushTaskJob;
    }

    public void setGenerateLOSRatePushTaskJob(JobSetting generateLOSRatePushTaskJob) {
        this.generateLOSRatePushTaskJob = generateLOSRatePushTaskJob;
    }

    public JobSetting getPushRateChangeJob() {
        return pushRateChangeJob;
    }

    public void setPushRateChangeJob(JobSetting pushRateChangeJob) {
        this.pushRateChangeJob = pushRateChangeJob;
    }
}
