package com.derbysoft.bookit.push.job.job;

import com.derbysoft.bookit.common.service.job.SyncJob;
import com.derbysoft.bookit.common.translator.Translator;
import com.derbysoft.bookit.push.job.service.LOSInventoryPushService;
import com.derbysoft.bookit.push.job.service.LOSRatePushService;
import com.derbysoft.bookit.push.job.component.PushTaskQueueService;
import com.derbysoft.bookit.push.job.support.ChangesPushTask;
import com.derbysoft.bookit.push.job.support.RunningTaskManager;
import com.derbysoft.synchronizer.dto.ChangesDTO;
import com.derbysoft.synchronizer.remote.common.SynchronizerRemoteService;
import com.derbysoft.synchronizer.remote.dto.DeleteKeysRequest;
import com.derbysoft.synchronizer.remote.dto.GetChangesRequest;
import com.derbysoft.synchronizer.remote.dto.GetChangesResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("pushRateChangeJob")
public class LOSRateChangePushJob implements SyncJob {

    private static final Logger LOGGER = Logger.getLogger(LOSRateChangePushJob.class);

    @Autowired
    private LOSRatePushService losRatePushService;

    @Autowired
    private LOSInventoryPushService losInventoryPushService;

    @Autowired
    private Translator<String, String, GetChangesRequest> getChangesRQTranslator;

    @Autowired
    private Translator<ChangesPushTask, String, DeleteKeysRequest> deleteKeysRQTranslator;

    @Autowired
    private PushTaskQueueService<ChangesPushTask> pushTaskQueueService;

    @Autowired
    private SynchronizerRemoteService synchronizerWebService;

    @Override
    public void sync() {
        if (RunningTaskManager.pushLOSRateJobIsRunning()) {
            LOGGER.info("Last 'LOSRateChangePushJob' is running, skip this time");
            return;
        }
        RunningTaskManager.pushLOSRateJobStart();
        try {
            String hotelCode = pushTaskQueueService.takeHotel();
            if (hotelCode == null) {
                return;
            }
            Collection<ChangesPushTask> hotelAllTask = pushTaskQueueService.takeHotelTask(hotelCode);
            if (hotelAllTask.isEmpty()) {
                return;
            }
            for (ChangesPushTask task : hotelAllTask) {
                GetChangesResponse response = getChanges(task);
                if (response == null) {
                    continue;
                }
                if (hasRateChange(response)) {
                    pushRateChange(task, response);
                }
                if (hasInventoryChange(response)) {
                    pushInventoryChange(task, response);
                }
                task.pushDone();
            }
            pushTaskQueueService.remove(hotelCode);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            RunningTaskManager.pushLOSRateJobDone();
        }
    }

    private GetChangesResponse getChanges(ChangesPushTask task) {
        GetChangesRequest request = getChangesRQTranslator.translate(task.getKey(), task.getHotelCode());
        GetChangesResponse response = synchronizerWebService.getChanges(request);
        if (response.hasError() || CollectionUtils.isEmpty(response.getGetChangesRS().getChangesList())) {
            return null;
        }
        return response;
    }

    private void pushInventoryChange(ChangesPushTask task, GetChangesResponse response) {
        task.pushInventoryChangeStart();
        ChangesDTO changesDTO = response.getGetChangesRS().getChangesList().get(0);
        if (losInventoryPushService.push(changesDTO.getLosInventories().getLosInventoriesList(), task)) {
            String token = response.getGetChangesRS().getChangesList().get(0).getLosInventories().getToken();
            synchronizerWebService.deleteKeys(deleteKeysRQTranslator.translate(task, token));
        }
        task.pushInventoryChangeDone();
    }

    private void pushRateChange(ChangesPushTask task, GetChangesResponse response) {
        task.pushRateChangeStart();
        ChangesDTO changesDTO = response.getGetChangesRS().getChangesList().get(0);
        if (losRatePushService.push(changesDTO.getLosRateChanges().getLosRateChangesList(), task)) {
            String token = response.getGetChangesRS().getChangesList().get(0).getLosRateChanges().getToken();
            synchronizerWebService.deleteKeys(deleteKeysRQTranslator.translate(task, token));
        }
        task.pushRateChangeDone();
    }

    private boolean hasInventoryChange(GetChangesResponse response) {
        ChangesDTO changesDTO = response.getGetChangesRS().getChangesList().get(0);
        return changesDTO != null && changesDTO.getLosInventories() != null && CollectionUtils.isNotEmpty(changesDTO.getLosInventories().getLosInventoriesList());
    }

    private boolean hasRateChange(GetChangesResponse response) {
        ChangesDTO changesDTO = response.getGetChangesRS().getChangesList().get(0);
        return changesDTO != null && changesDTO.getLosRateChanges() != null && CollectionUtils.isNotEmpty(changesDTO.getLosRateChanges().getLosRateChangesList());
    }

    public void setLosInventoryPushService(LOSInventoryPushService losInventoryPushService) {
        this.losInventoryPushService = losInventoryPushService;
    }

    public void setLosRatePushService(LOSRatePushService losRatePushService) {
        this.losRatePushService = losRatePushService;
    }

    public void setGetChangesRQTranslator(Translator<String, String, GetChangesRequest> getChangesRQTranslator) {
        this.getChangesRQTranslator = getChangesRQTranslator;
    }

    public void setDeleteKeysRQTranslator(Translator<ChangesPushTask, String, DeleteKeysRequest> deleteKeysRQTranslator) {
        this.deleteKeysRQTranslator = deleteKeysRQTranslator;
    }

    public void setSynchronizerWebService(SynchronizerRemoteService synchronizerWebService) {
        this.synchronizerWebService = synchronizerWebService;
    }

    public void setPushTaskQueueService(PushTaskQueueService<ChangesPushTask> pushTaskQueueService) {
        this.pushTaskQueueService = pushTaskQueueService;
    }
}
