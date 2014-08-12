package com.derbysoft.bookit.push.commons.loginterceptor;

import com.derbysoft.bookit.common.ccs.SystemConfig;
import com.derbysoft.bookit.common.commons.log.LogBody;
import com.derbysoft.bookit.common.commons.log.OperationType;
import com.derbysoft.bookit.push.domain.RateChangePushLog;
import com.derbysoft.bookit.common.model.SystemConfigKeys;
import com.derbysoft.bookit.common.repository.SystemConfigRepository;
import com.derbysoft.bookit.push.commons.log.SaveRateChangePushLogTask;
import com.derbysoft.bookit.push.commons.models.LosRateChangePushRQPair;
import com.derbysoft.bookit.push.commons.models.LosRateChangePushRSPair;
import com.derbysoft.bookit.push.job.support.ChangeNotifyPair;
import com.derbysoft.bookit.push.job.support.ChangesPushTask;
import com.derbysoft.bookit.push.web.action.support.ExceptionUtils;
import com.derbysoft.common.repository.partition.PartitionTableRepository;
import com.derbysoft.common.util.CollectionsUtils;
import com.derbysoft.common.util.PerformanceLogBuilder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.*;
import java.util.concurrent.Executor;

/**
 * Author: Jason Wu
 * Date  : 2014-03-19
 */
@Aspect
@Component
@SuppressWarnings("unchecked")
public class LOSRatePushInterceptor extends BaseLogInterceptor {
    protected static final Logger PERFORMANCE_LOGGER = LoggerFactory.getLogger("PERF_LOG");
    private static final Logger LOGGER = LoggerFactory.getLogger(LOSRatePushInterceptor.class);

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    @Autowired
    private PartitionTableRepository<RateChangePushLog> pushRateLogTablePartitionRepository;

    @Autowired
    private Executor saveLogExecutor;

    @Around("execution(* com.derbysoft.bookit.push.job.component.impl.LOSRateChangeBatchPushServiceImpl.batchPush(..))")
    public Object intercept(ProceedingJoinPoint joinPoint) {
        PerformanceLogBuilder builder = new PerformanceLogBuilder();
        RateChangePushLog rateChangePushLog = createRateChangePushLog(joinPoint.getArgs());
        List<LosRateChangePushRSPair> losRateChangePushRSPairs = new ArrayList<LosRateChangePushRSPair>();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            appendFromHeader(rateChangePushLog.getTaskId(), builder, joinPoint.getArgs());
            losRateChangePushRSPairs = (List<LosRateChangePushRSPair>) joinPoint.proceed();
            return losRateChangePushRSPairs;
        } catch (Throwable ex) {
            rateChangePushLog.setErrorMessage(ExceptionUtils.toString(ex));
            return losRateChangePushRSPairs;
        } finally {
            try {
                if (stopWatch.isRunning()) {
                    stopWatch.stop();
                }
                String spendTime = String.valueOf(stopWatch.getTotalTimeMillis());
                appendFromResponse(losRateChangePushRSPairs, builder, spendTime);
                saveTransactionLog(rateChangePushLog, losRateChangePushRSPairs, stopWatch.getTotalTimeMillis());
                PERFORMANCE_LOGGER.info(builder.toString());
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
    }

    private RateChangePushLog createRateChangePushLog(Object[] args) {
        List<LosRateChangePushRQPair> losRateChangePushRQPairs = (List<LosRateChangePushRQPair>) args[0];
        ChangeNotifyPair changeNotifyPair = (ChangeNotifyPair) args[1];
        ChangesPushTask changesPushTask = (ChangesPushTask) args[2];
        RateChangePushLog rateChangePushLog = new RateChangePushLog();
        rateChangePushLog.setTaskId(UUID.randomUUID().toString());
        rateChangePushLog.setHotelCode(changesPushTask.getHotelCode());
        rateChangePushLog.setRatePlanCode(changeNotifyPair.getRatePlan());
        rateChangePushLog.setRoomTypeCode(changeNotifyPair.getRoomType());
        rateChangePushLog.setChangeType(changeNotifyPair.getChangeType());
        sortByCheckIn(losRateChangePushRQPairs);
        rateChangePushLog.setStart(losRateChangePushRQPairs.get(0).getCheckIn());
        rateChangePushLog.setEnd(CollectionsUtils.last(losRateChangePushRQPairs).getCheckIn());
        return rateChangePushLog;
    }

    private void sortByCheckIn(List<LosRateChangePushRQPair> losRateChangePushRQPairs) {
        Collections.sort(losRateChangePushRQPairs, new Comparator<LosRateChangePushRQPair>() {
            @Override
            public int compare(LosRateChangePushRQPair o1, LosRateChangePushRQPair o2) {
                return o1.getCheckIn().compareTo(o2.getCheckIn());
            }
        });
    }

    private void saveTransactionLog(RateChangePushLog rateChangePushLog, List<LosRateChangePushRSPair> losRateChangePushRSPairs, Long spendTime) {
        rateChangePushLog.setTimeSpan(spendTime);
        String message = getErrorMessage(losRateChangePushRSPairs);
        if (message != null) {
            rateChangePushLog.setErrorMessage(message);
        }
        saveLogExecutor.execute(new SaveRateChangePushLogTask(pushRateLogTablePartitionRepository, rateChangePushLog, filterLogDetails(rateChangePushLog.getError(), losRateChangePushRSPairs)));
    }

    private String getErrorMessage(List<LosRateChangePushRSPair> losRateChangePushRSPairs) {
        for (LosRateChangePushRSPair losRateChangePushRSPair : losRateChangePushRSPairs) {
            if (!losRateChangePushRSPair.isSucceed()) {
                return losRateChangePushRSPair.getResponse();
            }
        }
        return null;
    }

    private Set<LosRateChangePushRSPair> filterLogDetails(boolean isError, List<LosRateChangePushRSPair> losRateChangePushRSPairs) {
        boolean saveSuccessfulLog = isSaveSucceedLog();
        Set<LosRateChangePushRSPair> logDetails = new HashSet<LosRateChangePushRSPair>();
        for (LosRateChangePushRSPair rateChangePushRSPair : losRateChangePushRSPairs) {
            if (saveSuccessfulLog || isError) {
                logDetails.add(rateChangePushRSPair);
            }
        }
        return logDetails;
    }

    private boolean isSaveSucceedLog() {
        SystemConfig systemConfig = systemConfigRepository.load(SystemConfigKeys.SAVE_SUCCEED_LOS_RATE_PUSH_LOG.getKey());
        return systemConfig != null && Boolean.valueOf(systemConfig.getValue());
    }

    private void appendFromResponse(List<LosRateChangePushRSPair> losRateChangePushRSPairs, PerformanceLogBuilder builder, String spendTime) {
        builder.append(LogBody.PROCESS_DURATION, spendTime);
        builder.append(LogBody.PROCESSED_RESULT, allIsWell(losRateChangePushRSPairs));
        builder.append(LogBody.SYSTEM_LOAD_STATUS, checkCpu());
        builder.append(LogBody.MEMORY_STATUS, checkMemory());
    }

    private boolean allIsWell(List<LosRateChangePushRSPair> losRateChangePushRSPairs) {
        for (LosRateChangePushRSPair losRateChangePushRSPair : losRateChangePushRSPairs) {
            if (!losRateChangePushRSPair.isSucceed()) {
                return false;
            }
        }
        return true;
    }

    private void appendFromHeader(String taskId, PerformanceLogBuilder builder, Object[] args) {
        List<LosRateChangePushRQPair> losRateChangePushRQPairs = (List<LosRateChangePushRQPair>) args[0];
        ChangeNotifyPair changeNotifyPair = (ChangeNotifyPair) args[1];
        ChangesPushTask changesPushTask = (ChangesPushTask) args[2];
        builder.append(LogBody.REQUEST_TYPE, OperationType.PUSH_LOS_RATE);
        builder.append(LogBody.TASK_ID, taskId);
        builder.append(LogBody.CHANNEL, "Bookit");
        builder.append(LogBody.PROVIDER_CHAIN, "Hyatt");
        builder.append(LogBody.PROVIDER_HOTEL_CODES, changesPushTask.getHotelCode());
        builder.append(LogBody.SYNC_LOS_KEY, changesPushTask.getKey());
        builder.append(LogBody.CHECK_IN_LIST, getCheckInPair(losRateChangePushRQPairs));
        builder.append(LogBody.CHANGE_TYPE, changeNotifyPair.getChangeType());
        builder.append(LogBody.RATE_PLAN, changeNotifyPair.getRatePlan());
        builder.append(LogBody.ROOM_TYPE, changeNotifyPair.getRoomType());
    }

    private String getCheckInPair(List<LosRateChangePushRQPair> losRateChangePushRQPairs) {
        List<String> checkInList = new ArrayList<String>();
        for (LosRateChangePushRQPair losRateChangePushRQPair : losRateChangePushRQPairs) {
            checkInList.add(losRateChangePushRQPair.getCheckIn());
        }
        Collections.sort(checkInList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return String.format("%s to %s", CollectionsUtils.first(checkInList), CollectionsUtils.last(checkInList));
    }

    public void setPushRateLogTablePartitionRepository(PartitionTableRepository<RateChangePushLog> pushRateLogTablePartitionRepository) {
        this.pushRateLogTablePartitionRepository = pushRateLogTablePartitionRepository;
    }

    public void setSaveLogExecutor(Executor saveLogExecutor) {
        this.saveLogExecutor = saveLogExecutor;
    }

    public void setSystemConfigRepository(SystemConfigRepository systemConfigRepository) {
        this.systemConfigRepository = systemConfigRepository;
    }
}