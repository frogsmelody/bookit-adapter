package com.derbysoft.bookit.push.web.action.transaction;
import com.derbysoft.bookit.common.commons.DateTimeUtils;
import com.derbysoft.bookit.push.commons.models.RateChangePushLogCondition;
import com.derbysoft.bookit.push.commons.models.RateChangePushLogDetailPair;
import com.derbysoft.bookit.push.domain.RateChangePushLog;
import com.derbysoft.bookit.push.domain.RateChangePushLogDetail;
import com.derbysoft.bookit.push.web.action.support.AbstractDMXAction;
import com.derbysoft.bookit.push.web.service.RateChangePushLogService;
import com.derbysoft.common.repository.partition.PartitionTableRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.*;

/**
 * Author: Jason Wu
 * Date  : 2013-09-23
 */
public class RateChangePushLogAction extends AbstractDMXAction {

    protected static final String TABLE_PARTITION_SUFFIX_PATTERN = "yyyyMMdd";

    private RateChangePushLogCondition condition;

    @Autowired
    private RateChangePushLogService rateChangePushLogService;

    @Autowired
    @Qualifier("pushRateLogTablePartitionRepository")
    private PartitionTableRepository<RateChangePushLog> logTablePartitionRepository;

    @Action("queryRatePushLog")
    public String queryRateAndAvailLog() {
        try {
            Date date;
            if (condition != null && StringUtils.isNotBlank(condition.getDate())) {
                date = DateTimeUtils.parse(condition.getDate());
            } else {
                date = DateTimeUtils.today();
            }
            DetachedCriteria detachedCriteria = rateChangePushLogService.createDetachedCriteria(condition);
            logTablePartitionRepository.paginate(DateTimeUtils.formatDate(date, TABLE_PARTITION_SUFFIX_PATTERN), detachedCriteria, paginater);
            return populateSucceededJsonResult(getPaginater(), "rateChangePushLogDetails");
        } catch (Exception e) {
            return populateFailedJsonResult(e);
        }
    }


    @Action("loadRatePushDetail")
    public String loadRateAndAvailLogDetail() {
        try {
            if (condition == null || condition.getId() == null) {
                return populateFailedJsonResult("RateChangeSyncLog not found !!");
            }
            Date date;
            if (StringUtils.isNotBlank(condition.getDate())) {
                date = DateTimeUtils.parse(condition.getDate(), DateTimeUtils.DATE_FORMAT);
            } else {
                date = DateTimeUtils.today();
            }
            RateChangePushLog rateChangePushLog = logTablePartitionRepository.get(DateTimeUtils.formatDate(date, TABLE_PARTITION_SUFFIX_PATTERN), condition.getId());
            if (rateChangePushLog == null) {
                return populateFailedJsonResult(String.format("Date:%s,RateChangePushLogId:%s not found !!", DateTimeUtils.formatDate(date), condition.getId()));
            }
            List<RateChangePushLogDetail> filtered = filter(rateChangePushLog.getRateChangePushLogDetails());
            List<RateChangePushLogDetail> paginated = paginate(filtered);
            List<RateChangePushLogDetail> rateChangePushLogDetails = prettyFormatDetails(paginated);
            paginater.setTotalCount(filtered.size());
            paginater.setObjects(rateChangePushLogDetails);
            return populateSucceededJsonResult(new RateChangePushLogDetailPair(rateChangePushLog, paginater), "rateChangePushLogDetails");
        } catch (Exception ex) {
            return populateFailedJsonResult(ex);
        }
    }

    private List<RateChangePushLogDetail> filter(List<RateChangePushLogDetail> rateChangePushLogDetails) {
        if (StringUtils.isBlank(condition.getCheckIn()) && StringUtils.isBlank(condition.getHasError())) {
            return rateChangePushLogDetails;
        }
        ArrayList<RateChangePushLogDetail> filteredRateChangePushLogDetails = new ArrayList<RateChangePushLogDetail>();
        for (RateChangePushLogDetail rateChangePushLogDetail : rateChangePushLogDetails) {
            if (sameCheckIn(rateChangePushLogDetail) && samePushResult(rateChangePushLogDetail)) {
                filteredRateChangePushLogDetails.add(rateChangePushLogDetail);
            }
        }
        Collections.sort(filteredRateChangePushLogDetails, new Comparator<RateChangePushLogDetail>() {
            @Override
            public int compare(RateChangePushLogDetail o1, RateChangePushLogDetail o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        return filteredRateChangePushLogDetails;
    }

    private boolean samePushResult(RateChangePushLogDetail rateChangePushLogDetail) {
        if (StringUtils.isBlank(condition.getHasError())) {
            return true;
        }
        boolean pushSucceed = StringUtils.equalsIgnoreCase(condition.getHasError(), "NO");
        return rateChangePushLogDetail.isPushSucceed() == pushSucceed;
    }

    private boolean sameCheckIn(RateChangePushLogDetail rateChangePushLogDetail) {
        return StringUtils.isBlank(condition.getCheckIn()) || rateChangePushLogDetail.getCheckIn().equals(condition.getCheckIn().trim());
    }

    private List<RateChangePushLogDetail> paginate(List<RateChangePushLogDetail> rateChangePushLogDetails) {
        int start = (paginater.getCurrentPage() - 1) * paginater.getPageSize();
        int end = start + paginater.getPageSize() > rateChangePushLogDetails.size() ? rateChangePushLogDetails.size() : start + paginater.getPageSize();
        ArrayList<RateChangePushLogDetail> paginatedRateChangePushLogDetails = new ArrayList<RateChangePushLogDetail>();
        for (int i = start; i < end; i++) {
            paginatedRateChangePushLogDetails.add(rateChangePushLogDetails.get(i));
        }
        return paginatedRateChangePushLogDetails;
    }

    private List<RateChangePushLogDetail> prettyFormatDetails(List<RateChangePushLogDetail> transactionDetails) {
        ArrayList<RateChangePushLogDetail> formattedTransactionDetails = new ArrayList<RateChangePushLogDetail>();
        for (RateChangePushLogDetail transactionDetail : transactionDetails) {
            transactionDetail.setDetail(prettyFormat(transactionDetail.getDetail()));
            formattedTransactionDetails.add(transactionDetail);
        }
        return formattedTransactionDetails;
    }

    public RateChangePushLogCondition getCondition() {
        return condition;
    }

    public void setCondition(RateChangePushLogCondition condition) {
        this.condition = condition;
    }

    public void setRateChangePushLogService(RateChangePushLogService rateChangePushLogService) {
        this.rateChangePushLogService = rateChangePushLogService;
    }

    public void setLogTablePartitionRepository(PartitionTableRepository<RateChangePushLog> logTablePartitionRepository) {
        this.logTablePartitionRepository = logTablePartitionRepository;
    }
}
