package com.derbysoft.bookit.push.web.action.task;

import com.derbysoft.bookit.push.job.component.PushTaskQueueService;
import com.derbysoft.bookit.push.job.support.ChangesPushTask;
import com.derbysoft.bookit.push.job.support.ExecuteStatus;
import com.derbysoft.bookit.push.job.support.TaskStatus;
import com.derbysoft.bookit.push.web.action.support.AbstractDMXAction;
import com.derbysoft.common.paginater.Paginater;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Jason Wu
 * Date  : 2013-10-16
 */
public class MonitorAction extends AbstractDMXAction {

    private ChangedRateCountCondition condition;

    @Autowired
    private PushTaskQueueService<ChangesPushTask> pushTaskQueueService;

    @Action("listRatePushKeys")
    public String queryPushRateQueueCount() {
        try {
            paginate(getPaginater(), flattenChangedKeys());
            return populateSucceededJsonResult(getPaginater());
        } catch (Exception e) {
            return populateFailedJsonResult(e);
        }
    }

    @Action("removeTask")
    public String removeTask() {
        try {
            if (condition == null) {
                throw new IllegalStateException("please input valid param !!");
            }
            if (StringUtils.isBlank(condition.getHotelCode())) {
                throw new IllegalStateException("HotelCode is blank.");
            }
            if (StringUtils.isNotBlank(condition.getKey())) {
                pushTaskQueueService.remove(condition.getHotelCode(), condition.getKey());
                return populateSucceededJsonResult(String.format("Remove %s,%s done !!", condition.getHotelCode(), condition.getKey()));
            }
            pushTaskQueueService.remove(condition.getHotelCode());
            return populateSucceededJsonResult(String.format("Remove %s done !!", condition.getHotelCode()));
        } catch (Exception e) {
            return populateFailedJsonResult(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void paginate(Paginater paginater, List<T> objects) {
        paginater.setTotalCount(objects.size());
        int currentPage = paginater.getCurrentPage();
        if (currentPage < 1 || objects.isEmpty()) {
            currentPage = 1;
        } else {
            int totalPage = objects.size() % paginater.getPageSize() > 0
                    ? objects.size() / paginater.getPageSize() + 1
                    : objects.size() / paginater.getPageSize();
            if (currentPage > totalPage) {
                currentPage = totalPage;
            }
        }
        paginater.setPageNo(currentPage);
        int begin = (currentPage - 1) * paginater.getPageSize();
        int end = currentPage * paginater.getPageSize();
        for (int i = begin; i < end; i++) {
            if (i > objects.size() - 1) {
                break;
            }
            paginater.getObjects().add(objects.get(i));
        }
    }

    private List<ChangesPushTask> flattenChangedKeys() {
        List<ChangesPushTask> rateChangeKeys = new ArrayList<ChangesPushTask>();
        List<ChangesPushTask> losRateChangeKeys = pushTaskQueueService.getAllTasks();
        for (ChangesPushTask losRateChangeKey : losRateChangeKeys) {
            rateChangeKeys.add(losRateChangeKey);
        }
        return filter(rateChangeKeys);
    }

    private List<ChangesPushTask> filter(List<ChangesPushTask> LosRateChangeKeys) {
        ArrayList<ChangesPushTask> filteredLosRateChangeKeys = new ArrayList<ChangesPushTask>();
        for (ChangesPushTask losRateChangeKey : LosRateChangeKeys) {
            if (notMatchHotel(losRateChangeKey)) {
                continue;
            }
            if (notMatchKey(losRateChangeKey)) {
                continue;
            }
            if (notMatchStatus(losRateChangeKey)) {
                continue;
            }
            if (notMatchRateChangeStatus(losRateChangeKey)) {
                continue;
            }
            if (notMatchInventoryChangeStatus(losRateChangeKey)) {
                continue;
            }
            filteredLosRateChangeKeys.add(losRateChangeKey);
        }
        return filteredLosRateChangeKeys;
    }

    private boolean notMatchInventoryChangeStatus(ChangesPushTask losRateChangeKey) {
        return StringUtils.isNotBlank(condition.getInventoryChangeStatus()) && losRateChangeKey.getInventoryChangeStatus() != TaskStatus.valueOf(condition.getInventoryChangeStatus());
    }

    private boolean notMatchRateChangeStatus(ChangesPushTask losRateChangeKey) {
        return StringUtils.isNotBlank(condition.getRateChangeStatus()) && losRateChangeKey.getRateChangeStatus() != TaskStatus.valueOf(condition.getRateChangeStatus());
    }

    private boolean notMatchStatus(ChangesPushTask losRateChangeKey) {
        return StringUtils.isNotBlank(condition.getExecuteStatus()) && losRateChangeKey.getExecuteStatus() != ExecuteStatus.valueOf(condition.getExecuteStatus());
    }

    private boolean notMatchKey(ChangesPushTask losRateChangeKey) {
        return StringUtils.isNotBlank(condition.getKey()) && !StringUtils.containsIgnoreCase(losRateChangeKey.getKey(), condition.getKey());
    }

    private boolean notMatchHotel(ChangesPushTask losRateChangeKey) {
        return StringUtils.isNotBlank(condition.getHotelCode()) && !StringUtils.equalsIgnoreCase(condition.getHotelCode(), losRateChangeKey.getHotelCode());
    }

    public ChangedRateCountCondition getCondition() {
        return condition;
    }

    public void setCondition(ChangedRateCountCondition condition) {
        this.condition = condition;
    }
}
