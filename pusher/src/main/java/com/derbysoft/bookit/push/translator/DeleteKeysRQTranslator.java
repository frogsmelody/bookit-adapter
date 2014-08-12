package com.derbysoft.bookit.push.translator;

import com.derbysoft.bookit.common.translator.Translator;
import com.derbysoft.bookit.push.job.support.ChangesPushTask;
import com.derbysoft.synchronizer.dto.DeleteKeysRQ;
import com.derbysoft.synchronizer.dto.KeyTokenDTO;
import com.derbysoft.synchronizer.remote.dto.DeleteKeysRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DeleteKeysRQTranslator extends BaseTranslator implements Translator<ChangesPushTask, String, DeleteKeysRequest> {
    @Override
    public DeleteKeysRequest translate(ChangesPushTask task, String token) {
        DeleteKeysRequest request = new DeleteKeysRequest();
        request.setHeader(createHeader());
        DeleteKeysRQ deleteKeysRQ = new DeleteKeysRQ();
        deleteKeysRQ.setHotel(task.getHotelCode());
        ArrayList<KeyTokenDTO> keyTokens = new ArrayList<KeyTokenDTO>();
        KeyTokenDTO keyTokenDTO = new KeyTokenDTO();
        keyTokenDTO.setKey(task.getKey());
        ArrayList<String> tokens = new ArrayList<String>();
        tokens.add(token);
        keyTokenDTO.setTokensList(tokens);
        keyTokens.add(keyTokenDTO);
        deleteKeysRQ.setKeyTokensList(keyTokens);
        request.setDeleteKeysRQ(deleteKeysRQ);
        return request;
    }
}
