package com.derbysoft.bookit.push.web.action.transaction;

import com.derbysoft.bookit.common.translator.Translator;
import com.derbysoft.bookit.push.job.support.ChangesPushTask;
import com.derbysoft.dswitch.remote.hotel.dto.RequestHeader;
import com.derbysoft.synchronizer.dto.GetChangesRQ;
import com.derbysoft.synchronizer.remote.dto.GetChangesRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;

@Component("GetChangesRQTranslator")
public class GetChangesRQTranslator implements Translator<ChangesPushTask, Void, GetChangesRequest> {
    @Value("#{properties['channel.code']}")
    protected String channelCode;

    @Value("#{properties['provider.code']}")
    protected String providerCode;

    @Override
    public GetChangesRequest translate(ChangesPushTask changesPushTask, Void aVoid) {
        GetChangesRequest request = new GetChangesRequest();
        request.setHeader(createHeader());
        GetChangesRQ getChangesRQ = new GetChangesRQ();
        getChangesRQ.setHotel(changesPushTask.getHotelCode());
        ArrayList<String> keys = new ArrayList<String>();
        keys.add(changesPushTask.getKey());
        getChangesRQ.setKeysList(keys);
        request.setGetChangesRQ(getChangesRQ);
        return request;
    }

    private RequestHeader createHeader() {
        RequestHeader requestHeader = new RequestHeader();
        requestHeader.setTaskId(UUID.randomUUID().toString().toLowerCase());
        requestHeader.setSource(channelCode);
        requestHeader.setDestination(providerCode);
        return requestHeader;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }
}
