package com.derbysoft.bookit.push.web.action.setting;

import com.derbysoft.bookit.common.domain.common.JobConfig;
import com.derbysoft.bookit.common.domain.common.ScheduleStatus;
import com.derbysoft.bookit.common.model.SystemConfigKeys;
import com.derbysoft.bookit.common.repository.JobConfigRepository;
import com.derbysoft.bookit.common.service.job.JobManageService;
import com.derbysoft.bookit.push.web.action.setting.support.JobSetting;
import com.derbysoft.bookit.push.web.action.setting.support.JobSettings;
import com.derbysoft.bookit.push.web.action.support.AbstractDMXAction;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Jason Wu
 * Date  : 2013-09-26
 */
public class JobAction extends AbstractDMXAction {
    public static final String UPDATE_SUCCEED = "Update succeed !!";
    @Autowired
    private JobConfigRepository jobConfigRepository;

    @Autowired
    private JobManageService jobManageService;

    private String jobId;
    private JobSettings jobSettings = new JobSettings();

    @Action("listJobConfigs")
    public String listJobConfigs() {
        try {
            return populateSucceededJsonResult(toJobSettings(jobConfigRepository.loadAll()));
        } catch (Exception e) {
            return populateFailedJsonResult(e.getMessage());
        }
    }

    private List<JobSetting> toJobSettings(List<JobConfig> jobConfigs) {
        ArrayList<JobSetting> settings = new ArrayList<JobSetting>();
        for (JobConfig jobConfig : jobConfigs) {
            JobSetting jobSetting = JobSetting.fromStandardCronExpress(jobConfig.getCronExpression());
            jobSetting.setJobId(jobConfig.getJobId());
            jobSetting.setScheduleStatus(jobConfig.getScheduleStatus());
            settings.add(jobSetting);
        }
        return settings;
    }

    @Action("saveJobConfigs")
    public String saveJobConfigs() {
        try {
            update(jobSettings.getPushRateChangeJob(), SystemConfigKeys.LOS_RATE_PUSH_JOB);
            update(jobSettings.getGenerateLOSRatePushTaskJob(), SystemConfigKeys.LOS_RATE_PUSH_TASK_GENERATE_JOB);
            return populateSucceededJsonResult(UPDATE_SUCCEED);
        } catch (Exception e) {
            return populateFailedJsonResult(e.getMessage());
        }
    }

    @Action("launchJob")
    public String launchJob() {
        try {
            validateJobId();
            jobManageService.launch(jobId);
            return populateSucceededJsonResult(UPDATE_SUCCEED);
        } catch (Exception e) {
            return populateFailedJsonResult(e.getMessage());
        }
    }

    @Action("pauseJob")
    public String pauseJob() {
        try {
            validateJobId();
            jobManageService.pause(jobId);
            return populateSucceededJsonResult(UPDATE_SUCCEED);
        } catch (Exception e) {
            return populateFailedJsonResult(e.getMessage());
        }
    }

    @Action("shutdownJob")
    public String shutdownJob() {
        try {
            validateJobId();
            jobManageService.shutdown(jobId);
            return populateSucceededJsonResult(UPDATE_SUCCEED);
        } catch (Exception e) {
            return populateFailedJsonResult(e.getMessage());
        }
    }

    private void validateJobId() {
        if (StringUtils.isBlank(jobId)) {
            throw new IllegalArgumentException(jobId);
        }
    }

    private void update(JobSetting newConfig, SystemConfigKeys systemConfigKeys) throws SchedulerException, ParseException {
        if (newConfig == null) {
            throw new IllegalArgumentException(String.format("Update failed, key:[ %s ] doesn't allow blank value.", systemConfigKeys.getKey()));
        }
        JobConfig jobConfig = load(systemConfigKeys.getKey());
        if (jobConfig != null && diffJobConfig(newConfig, jobConfig)) {
            jobConfig.setCronExpression(newConfig.buildStandardCronExpress());
            //refresh db
            jobConfigRepository.update(jobConfig);
            //refreshJob
            jobManageService.refreshJob(jobConfig);
        }
    }

    private boolean diffJobConfig(JobSetting newConfig, JobConfig jobConfig) {
        return !StringUtils.equalsIgnoreCase(newConfig.buildStandardCronExpress(), jobConfig.getCronExpression());
    }

    private JobConfig load(String jobId) {
        JobConfig jobConfig = jobConfigRepository.loadByJobId(jobId);
        if (jobConfig == null) {
            throw new IllegalStateException(String.format("JobConfigNotFound:%s", jobId));
        }
        return jobConfig;
    }

    public JobSettings getJobSettings() {
        return jobSettings;
    }

    public void setJobSettings(JobSettings jobSettings) {
        this.jobSettings = jobSettings;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void setJobConfigRepository(JobConfigRepository jobConfigRepository) {
        this.jobConfigRepository = jobConfigRepository;
    }

    public void setJobManageService(JobManageService jobManageService) {
        this.jobManageService = jobManageService;
    }
}
