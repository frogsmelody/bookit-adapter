package com.derbysoft.bookit.push.web.service;

import com.derbysoft.bookit.common.repository.impl.DetachedCriteriaSupport;
import com.derbysoft.bookit.push.commons.models.RateChangePushLogCondition;
import com.derbysoft.bookit.push.domain.RateChangePushLog;
import com.derbysoft.bookit.push.job.support.ChangeType;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

/**
 * Author: Jason Wu
 * Date  : 2013-11-27
 */
@Component
public class RateChangePushLogService extends DetachedCriteriaSupport {
    private static final String START = "start";
    private static final String END = "end";

    public DetachedCriteria createDetachedCriteria(RateChangePushLogCondition condition) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RateChangePushLog.class).addOrder(Order.desc("createTime"));
        if (condition == null) {
            return detachedCriteria;
        }
        eq(detachedCriteria, "taskId", condition.getTaskId());
        eq(detachedCriteria, "hotelCode", condition.getHotelCode());
        eq(detachedCriteria, "ratePlanCode", condition.getRatePlanCode());
        eq(detachedCriteria, "roomTypeCode", condition.getRoomTypeCode());
        if (StringUtils.isNotBlank(condition.getChangeType())) {
            detachedCriteria.add(Restrictions.eq("changeType", ChangeType.valueOf(condition.getChangeType())));
        }
        if (StringUtils.isNotBlank(condition.getError())) {
            eq(detachedCriteria, "error", Boolean.valueOf(condition.getError()));
        }
        ge(detachedCriteria, TIME_SPAN, condition.getMinSpentTime());
        le(detachedCriteria, TIME_SPAN, condition.getMaxSpentTime());
        if (StringUtils.isNotBlank(condition.getCheckIn())) {
            detachedCriteria.add(Restrictions.le(START, condition.getCheckIn()));
            detachedCriteria.add(Restrictions.ge(END, condition.getCheckIn()));
        }
        return detachedCriteria;
    }
}
