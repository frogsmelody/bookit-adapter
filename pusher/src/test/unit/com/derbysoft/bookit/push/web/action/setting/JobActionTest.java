package com.derbysoft.bookit.push.web.action.setting;

import com.derbysoft.bookit.common.domain.common.JobConfig;
import com.derbysoft.bookit.common.model.SystemConfigKeys;
import com.derbysoft.bookit.common.repository.JobConfigRepository;
import com.derbysoft.bookit.common.service.job.JobManageService;
import com.derbysoft.bookit.push.web.action.setting.support.JobSetting;
import com.derbysoft.bookit.push.web.action.setting.support.JobSettings;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.quartz.SchedulerException;
import utils.XMLTestSupport;

import java.text.ParseException;
import java.util.ArrayList;

public class JobActionTest extends XMLTestSupport {
    JobAction jobAction = new JobAction();

    JobConfigRepository jobConfigRepository;
    JobManageService jobManageService;

    @Before
    public void setUp() throws Exception {
        jobConfigRepository = EasyMock.createMock(JobConfigRepository.class);
        jobManageService = EasyMock.createMock(JobManageService.class);

        jobAction.setJobConfigRepository(jobConfigRepository);
        jobAction.setJobManageService(jobManageService);
    }

    @Test
    public void testList() {
        ArrayList<JobConfig> jobConfigs = new ArrayList<JobConfig>();
        JobConfig jobConfig = new JobConfig();
        jobConfig.setCronExpression("0 0/10 * * * ?");
        jobConfigs.add(jobConfig);

        JobConfig jobConfig2 = new JobConfig();
        jobConfig2.setCronExpression("0 */1 * * * ?");
        jobConfigs.add(jobConfig2);
        EasyMock.expect(jobConfigRepository.loadAll()).andReturn(jobConfigs);
        EasyMock.replay(jobConfigRepository);
        jobAction.listJobConfigs();
        assertEquals(readFile("jobconfig.json"), formatObjectToJson(jobAction.getMsg()));
    }

    @Test
    public void updateJobConfig() throws ParseException, SchedulerException {
        JobSettings jobSettings = new JobSettings();
        JobSetting pushRateChangeJob = new JobSetting();
        pushRateChangeJob.setStartMinute("0");
        pushRateChangeJob.setPerMinute("10");
        pushRateChangeJob.setStartHour("0");
        pushRateChangeJob.setPerHour("1");
        jobSettings.setPushRateChangeJob(pushRateChangeJob);

        JobSetting losRatePushTaskKeyGenerateJob = new JobSetting();
        losRatePushTaskKeyGenerateJob.setStartMinute("10");
        losRatePushTaskKeyGenerateJob.setPerMinute("*");
        losRatePushTaskKeyGenerateJob.setStartHour("0");
        losRatePushTaskKeyGenerateJob.setPerHour("6");
        jobSettings.setGenerateLOSRatePushTaskJob(losRatePushTaskKeyGenerateJob);

        jobAction.setJobSettings(jobSettings);
        EasyMock.expect(jobConfigRepository.loadByJobId(EasyMock.anyObject(String.class))).andAnswer(new IAnswer<JobConfig>() {
            @Override
            public JobConfig answer() throws Throwable {
                String input = (String) EasyMock.getCurrentArguments()[0];
                JobConfig jobConfig = new JobConfig();
                if (input.equals(SystemConfigKeys.LOS_RATE_PUSH_TASK_GENERATE_JOB.getKey())) {
                    jobConfig.setJobId(SystemConfigKeys.LOS_RATE_PUSH_TASK_GENERATE_JOB.getKey());
                    jobConfig.setCronExpression("0 */10 * * * ?");
                }
                if (input.equals(SystemConfigKeys.LOS_RATE_PUSH_JOB.getKey())) {
                    jobConfig.setJobId(SystemConfigKeys.LOS_RATE_PUSH_JOB.getKey());
                    jobConfig.setCronExpression("0 0/20 * * * ?");
                }
                return jobConfig;
            }
        }).atLeastOnce();
        jobConfigRepository.update(EasyMock.anyObject(JobConfig.class));
        EasyMock.expectLastCall().andAnswer(new IAnswer<Object>() {
            @Override
            public Object answer() throws Throwable {
                JobConfig jobConfig = (JobConfig) EasyMock.getCurrentArguments()[0];
                if (jobConfig.getJobId().equals(SystemConfigKeys.LOS_RATE_PUSH_JOB.getKey())) {
                    Assert.assertTrue(jobConfig.getCronExpression().equals("0 0/10 0/1 * * ?"));
                }
                if (jobConfig.getJobId().equals(SystemConfigKeys.LOS_RATE_PUSH_TASK_GENERATE_JOB.getKey())) {
                    Assert.assertTrue(jobConfig.getCronExpression().equals("0 10 0/6 * * ?"));
                }
                return null;
            }
        }).atLeastOnce();
        EasyMock.replay(jobConfigRepository);
        jobManageService.refreshJob(EasyMock.anyObject(JobConfig.class));
        EasyMock.expectLastCall().atLeastOnce();
        EasyMock.replay(jobManageService);
        jobAction.saveJobConfigs();
        EasyMock.verify(jobConfigRepository, jobManageService);
    }
}
