package com.derbysoft.bookit.common.repository.impl;

import com.derbysoft.bookit.common.domain.common.JobConfig;
import com.derbysoft.bookit.common.domain.common.ScheduleStatus;
import com.derbysoft.bookit.common.repository.JobConfigRepository;
import com.derbysoft.common.service.CommonService;

import java.util.List;

/**
 * Author: Jason Wu
 * Date  : 2013-09-26
 */
public class JobConfigRepositoryImpl extends CommonService<JobConfig>
        implements JobConfigRepository {
    @Override
    public List<JobConfig> load(ScheduleStatus scheduleStatus) {
        return find("scheduleStatus", scheduleStatus);
    }

    @Override
    public JobConfig loadByJobId(String jobId) {
        return load("jobId", jobId);
    }

    @Override
    public void update(JobConfig jobConfig) {
        super.save(jobConfig);
    }

    @Override
    public void saveAll(List<JobConfig> jobConfigs) {
        for (JobConfig jobConfig : jobConfigs) {
            super.commonRepository.save(jobConfig);
        }
    }
}
