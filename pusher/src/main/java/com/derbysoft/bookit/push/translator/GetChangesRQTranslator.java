package com.derbysoft.bookit.push.translator;

import com.derbysoft.bookit.common.translator.Translator;
import com.derbysoft.synchronizer.dto.GetChangesRQ;
import com.derbysoft.synchronizer.remote.dto.GetChangesRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class GetChangesRQTranslator extends BaseTranslator implements Translator<String,String,GetChangesRequest>{
    @Override
    public GetChangesRequest translate(String key, String hotelCode) {
        GetChangesRequest request = new GetChangesRequest();
        request.setHeader(createHeader());
        GetChangesRQ getChangesRQ = new GetChangesRQ();
        getChangesRQ.setHotel(hotelCode);
        ArrayList<String> keys = new ArrayList<String>();
        keys.add(key);
        getChangesRQ.setKeysList(keys);
        request.setGetChangesRQ(getChangesRQ);
        return request;
    }
}
