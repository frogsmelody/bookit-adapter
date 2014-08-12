package com.derbysoft.bookit.push.job.job;

import com.derbysoft.bookit.common.translator.Translator;
import com.derbysoft.bookit.push.job.service.LOSInventoryPushService;
import com.derbysoft.bookit.push.job.service.LOSRatePushService;
import com.derbysoft.bookit.push.job.component.PushTaskQueueService;
import com.derbysoft.bookit.push.job.support.ChangesPushTask;
import com.derbysoft.dswitch.dto.common.ErrorDTO;
import com.derbysoft.dswitch.dto.hotel.cds.LOSInventoryDTO;
import com.derbysoft.dswitch.dto.hotel.cds.LOSRateChangeDTO;
import com.derbysoft.synchronizer.dto.ChangesDTO;
import com.derbysoft.synchronizer.dto.GetChangesRS;
import com.derbysoft.synchronizer.dto.LOSInventoriesDTO;
import com.derbysoft.synchronizer.dto.LOSRateChangesDTO;
import com.derbysoft.synchronizer.remote.common.SynchronizerRemoteService;
import com.derbysoft.synchronizer.remote.dto.DeleteKeysRequest;
import com.derbysoft.synchronizer.remote.dto.DeleteKeysResponse;
import com.derbysoft.synchronizer.remote.dto.GetChangesRequest;
import com.derbysoft.synchronizer.remote.dto.GetChangesResponse;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import utils.XMLTestSupport;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class LOSRateChangePushJobTest extends XMLTestSupport {
    LOSRateChangePushJob losRateChangePushJob = new LOSRateChangePushJob();
    Translator<String, String, GetChangesRequest> getChangesRQTranslator;
    PushTaskQueueService<ChangesPushTask> pushTaskQueueService;
    SynchronizerRemoteService synchronizerWebService;
    LOSRatePushService losRatePushService;
    Translator<ChangesPushTask, String, DeleteKeysRequest> deleteKeysRQTranslator;
    LOSInventoryPushService losInventoryPushService;

    @Before
    public void setUp() throws Exception {
        getChangesRQTranslator = EasyMock.createMock(Translator.class);
        pushTaskQueueService = EasyMock.createMock(PushTaskQueueService.class);
        synchronizerWebService = EasyMock.createMock(SynchronizerRemoteService.class);
        losRatePushService = EasyMock.createMock(LOSRatePushService.class);
        deleteKeysRQTranslator = EasyMock.createMock(Translator.class);
        losInventoryPushService = EasyMock.createMock(LOSInventoryPushService.class);

        losRateChangePushJob.setGetChangesRQTranslator(getChangesRQTranslator);
        losRateChangePushJob.setPushTaskQueueService(pushTaskQueueService);
        losRateChangePushJob.setSynchronizerWebService(synchronizerWebService);
        losRateChangePushJob.setLosRatePushService(losRatePushService);
        losRateChangePushJob.setDeleteKeysRQTranslator(deleteKeysRQTranslator);
        losRateChangePushJob.setLosInventoryPushService(losInventoryPushService);
    }

    @Test
    public void emptyRateChange() {
        EasyMock.expect(pushTaskQueueService.takeHotel()).andReturn("Hotel-A");
        ArrayList<ChangesPushTask> changesPushTasks = new ArrayList<ChangesPushTask>();
        changesPushTasks.add(ChangesPushTask.build("Hotel-A", "Key-A"));
        EasyMock.expect(pushTaskQueueService.takeHotelTask("Hotel-A")).andReturn(changesPushTasks);

        EasyMock.expect(getChangesRQTranslator.translate(EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn(new GetChangesRequest());
        EasyMock.replay(getChangesRQTranslator);

        pushTaskQueueService.remove(EasyMock.anyObject(String.class));
        EasyMock.expectLastCall();

        EasyMock.replay(pushTaskQueueService);
        GetChangesResponse response = createErrorResponse();
        GetChangesRS getChangesRS = new GetChangesRS();
        response.setGetChangesRS(getChangesRS);
        EasyMock.expect(synchronizerWebService.getChanges(EasyMock.anyObject(GetChangesRequest.class))).andReturn(response);
        EasyMock.replay(synchronizerWebService);
        losRateChangePushJob.sync();
        EasyMock.verify(pushTaskQueueService, getChangesRQTranslator, synchronizerWebService);
    }

    @Test
    public void pushSucceed() {
        EasyMock.expect(pushTaskQueueService.takeHotel()).andReturn("Hotel-A");

        ArrayList<ChangesPushTask> changesPushTasks = new ArrayList<ChangesPushTask>();
        changesPushTasks.add(ChangesPushTask.build("Hotel-A", "Key-A"));
        EasyMock.expect(pushTaskQueueService.takeHotelTask("Hotel-A")).andReturn(changesPushTasks);

        pushTaskQueueService.remove(EasyMock.anyObject(String.class));
        EasyMock.expectLastCall();

        EasyMock.replay(pushTaskQueueService);
        EasyMock.expect(getChangesRQTranslator.translate(EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn(new GetChangesRequest());
        EasyMock.replay(getChangesRQTranslator);

        EasyMock.expect(synchronizerWebService.getChanges(EasyMock.anyObject(GetChangesRequest.class))).andReturn(createChangesResponse());

        EasyMock.expect(synchronizerWebService.deleteKeys(EasyMock.anyObject(DeleteKeysRequest.class))).andReturn(new DeleteKeysResponse()).atLeastOnce();
        EasyMock.replay(synchronizerWebService);

        EasyMock.expect(losRatePushService.push(EasyMock.anyObject(ArrayList.class), EasyMock.anyObject(ChangesPushTask.class))).andReturn(true);
        EasyMock.replay(losRatePushService);

        EasyMock.expect(losInventoryPushService.push(EasyMock.anyObject(ArrayList.class), EasyMock.anyObject(ChangesPushTask.class))).andReturn(true);
        EasyMock.replay(losInventoryPushService);

        EasyMock.expect(deleteKeysRQTranslator.translate(EasyMock.anyObject(ChangesPushTask.class), EasyMock.anyObject(String.class))).andReturn(new DeleteKeysRequest()).atLeastOnce();
        EasyMock.replay(deleteKeysRQTranslator);

        losRateChangePushJob.sync();
        EasyMock.verify(pushTaskQueueService, getChangesRQTranslator, synchronizerWebService, losInventoryPushService, deleteKeysRQTranslator, losRatePushService);
    }

    private GetChangesResponse createChangesResponse() {
        GetChangesResponse response = new GetChangesResponse();
        GetChangesRS getChangesRS = new GetChangesRS();
        ArrayList<ChangesDTO> changes = new ArrayList<ChangesDTO>();
        ChangesDTO changesDTO = new ChangesDTO();
        changesDTO.setLosRateChanges(createLosRateChangesDTO());
        changesDTO.setLosInventories(createInventoryChangesDTO());
        changes.add(changesDTO);
        getChangesRS.setChangesList(changes);
        response.setGetChangesRS(getChangesRS);
        return response;
    }

    private LOSInventoriesDTO createInventoryChangesDTO() {
        LOSInventoriesDTO losInventoriesDTO = new LOSInventoriesDTO();
        losInventoriesDTO.setToken("Token-02");
        ArrayList<LOSInventoryDTO> losInventories = new ArrayList<LOSInventoryDTO>();
        losInventories.add(new LOSInventoryDTO());
        losInventoriesDTO.setLosInventoriesList(losInventories);
        return losInventoriesDTO;
    }

    private LOSRateChangesDTO createLosRateChangesDTO() {
        LOSRateChangesDTO losRateChanges = new LOSRateChangesDTO();
        losRateChanges.setToken("Token-01");
        ArrayList<LOSRateChangeDTO> rateChanges = new ArrayList<LOSRateChangeDTO>();
        rateChanges.add(new LOSRateChangeDTO());
        losRateChanges.setLosRateChangesList(rateChanges);
        return losRateChanges;
    }

    private GetChangesResponse createErrorResponse() {
        GetChangesResponse response = new GetChangesResponse();
        response.setError(new ErrorDTO());
        return response;
    }
}
