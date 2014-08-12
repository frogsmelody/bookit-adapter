package com.derbysoft.bookit.common.service.job;
import com.derbysoft.bookit.common.commons.Constants;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: Jason Wu
 * Date  : 2013-09-27
 */
public class SyncJobImpl implements Job {

    private static Logger logger = LoggerFactory.getLogger(SyncJobImpl.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            pauseTrigger(context);
            JobDataMap mergedJobDataMap = context.getMergedJobDataMap();
            SyncJob syncJob = (SyncJob) mergedJobDataMap.get(Constants.JOB_SERVICE_BEAN_NAME);
            syncJob.sync();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            resumeTrigger(context);
        }
    }

    private void resumeTrigger(JobExecutionContext context) {
        try {
            context.getScheduler().resumeTrigger(context.getTrigger().getKey());
        } catch (SchedulerException e) {
            logger.error("Resume trigger error", e);
        }
    }

    private void pauseTrigger(JobExecutionContext context) {
        try {
            context.getScheduler().pauseTrigger(context.getTrigger().getKey());
        } catch (SchedulerException e) {
            logger.error("Pause trigger error", e);
        }
    }
}
