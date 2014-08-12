package com.derbysoft.bookit.common.repository;

import com.derbysoft.bookit.common.domain.common.JobConfig;
import com.derbysoft.bookit.common.domain.common.ScheduleStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface JobConfigRepository {
    List<JobConfig> loadAll();

    List<JobConfig> load(ScheduleStatus scheduleStatus);

    JobConfig loadByJobId(String jobId);

    void update(JobConfig jobConfig);

    @Transactional(readOnly = false)
    void saveAll(List<JobConfig> jobConfigs);
}
