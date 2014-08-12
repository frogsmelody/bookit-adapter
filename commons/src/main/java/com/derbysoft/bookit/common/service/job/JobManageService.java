package com.derbysoft.bookit.common.service.job;

import com.derbysoft.bookit.common.commons.Constants;
import com.derbysoft.bookit.common.commons.exception.JobException;
import com.derbysoft.bookit.common.domain.common.JobConfig;
import com.derbysoft.bookit.common.domain.common.ScheduleStatus;
import com.derbysoft.bookit.common.repository.JobConfigRepository;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.text.ParseException;
import java.util.List;

public class JobManageService {

    private static final Logger LOGGER = Logger.getLogger(JobManageService.class);
    public static final String JOB_NOT_FOUND = "%s:JobNotFound";

    private JobConfigRepository jobConfigRepository;

    public void launchAll(ApplicationContext applicationContext) throws SchedulerException, ParseException {
        List<JobConfig> jobConfigs = jobConfigRepository.load(ScheduleStatus.RUNNING);

        for (JobConfig jobConfig : jobConfigs) {
            SyncJob syncJob = applicationContext.getBean(jobConfig.getJobId(), SyncJob.class);
            launch(syncJob, jobConfig);
        }
    }

    public void launch(String jobId) throws SchedulerException, ParseException {
        JobConfig jobConfig = jobConfigRepository.loadByJobId(jobId);
        if (jobConfig == null) {
            throw new JobException(String.format(JOB_NOT_FOUND, jobId));
        }
        launch(getJobSyncService(jobConfig.getJobId()), jobConfig);
    }

    private void launch(SyncJob syncJob, JobConfig jobConfig) throws SchedulerException, ParseException {
        Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
        String jobGroup = buildJobGroup(jobConfig.getJobId());
        JobKey detailKey = new JobKey(jobConfig.getJobId(), jobGroup);
        JobDetail jobDetail = defaultScheduler.getJobDetail(detailKey);
        if (jobDetail == null) {
            jobDetail = createJobDetail(detailKey);
            jobDetail.getJobDataMap().put(Constants.JOB_SERVICE_BEAN_NAME, syncJob);
            defaultScheduler.scheduleJob(jobDetail, createTrigger(jobConfig.getJobId(), jobConfig.getCronExpression()));
            defaultScheduler.start();
            LOGGER.info(String.format("Launching a new job:[%s]", jobConfig.getJobId()));
        } else {
            defaultScheduler.resumeJob(detailKey);
            LOGGER.info(String.format("Job resuming :[%s]", jobConfig.getJobId()));
        }
        jobConfig.setScheduleStatus(ScheduleStatus.RUNNING);
        jobConfigRepository.update(jobConfig);
    }

    private SyncJob getJobSyncService(String serviceName) {
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
        return applicationContext.getBean(serviceName, SyncJob.class);
    }

    private Trigger createTrigger(String jobId, String cronExpression) throws ParseException {
        CronTriggerImpl trigger = new CronTriggerImpl();
        String triggerName = buildTriggerName(jobId);
        trigger.setName(triggerName);
        trigger.setKey(buildTriggerKey(triggerName));
        trigger.setCronExpression(cronExpression);
        return trigger;
    }

    private JobDetail createJobDetail(JobKey jobKey) {
        JobDetailImpl jobDetail = new JobDetailImpl();
        jobDetail.setKey(jobKey);
        jobDetail.setJobClass(SyncJobImpl.class);
        JobDataMap jobDataMap = new JobDataMap();

        jobDetail.setJobDataMap(jobDataMap);
        return jobDetail;
    }

    public void pause(String jobId) throws SchedulerException, ParseException {
        JobConfig jobConfig = jobConfigRepository.loadByJobId(jobId);
        if (jobConfig == null) {
            throw new JobException(String.format(JOB_NOT_FOUND, jobId));
        }
        Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
        if (!defaultScheduler.isStarted()) {
            LOGGER.info(String.format("Job :[%s] isn't starting.", jobId));
            jobConfig.setScheduleStatus(ScheduleStatus.SHUTDOWN);
            jobConfigRepository.update(jobConfig);
            return;
        }
        defaultScheduler.pauseJob(new JobKey(jobId, String.format("%s-group", jobId)));
        LOGGER.info(String.format("Job pausing :[%s]", jobId));
        jobConfig.setScheduleStatus(ScheduleStatus.PAUSING);
        jobConfigRepository.update(jobConfig);
    }

    public void shutdown(String jobId) throws SchedulerException, ParseException {
        JobConfig jobConfig = jobConfigRepository.loadByJobId(jobId);
        if (jobConfig == null) {
            throw new JobException(String.format(JOB_NOT_FOUND, jobId));
        }
        Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
        if (defaultScheduler.isStarted()) {
            defaultScheduler.shutdown(true);
            LOGGER.info(String.format("Job shutdown :[%s]", jobId));
            jobConfig.setScheduleStatus(ScheduleStatus.SHUTDOWN);
            jobConfigRepository.update(jobConfig);
            return;
        }
        defaultScheduler.shutdown(true);
        LOGGER.info(String.format("Job has bean shutdown :[%s]", jobId));
        jobConfig.setScheduleStatus(ScheduleStatus.SHUTDOWN);
        jobConfigRepository.update(jobConfig);
    }

    public void refreshJob(JobConfig jobConfig) throws SchedulerException, ParseException {
        Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
        String triggerName = buildTriggerName(jobConfig.getJobId());
        TriggerKey triggerKey = buildTriggerKey(triggerName);
        CronTriggerImpl trigger = (CronTriggerImpl) defaultScheduler.getTrigger(triggerKey);
        if (trigger == null) {
            throw new SchedulerException(String.format("Trigger not found JobId:[%s],TriggerName:[%s]", jobConfig.getJobId(), triggerName));
        }
        trigger.setCronExpression(jobConfig.getCronExpression());
        defaultScheduler.rescheduleJob(triggerKey, trigger);
    }

    private static String buildTriggerName(String jobId) {
        return String.format("%s-trigger", jobId);
    }

    private static String buildJobGroup(String jobId) {
        return String.format("%s-group", jobId);
    }

    private static TriggerKey buildTriggerKey(String triggerName) {
        return new TriggerKey(triggerName, null);
    }

    public void setJobConfigRepository(JobConfigRepository jobConfigRepository) {
        this.jobConfigRepository = jobConfigRepository;
    }
}
