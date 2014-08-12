package com.derbysoft.bookit.push.translator;

import com.derbysoft.bookit.push.job.support.ChangesPushTask;
import com.derbysoft.synchronizer.remote.dto.DeleteKeysRequest;
import com.derbysoft.synchronizer.remote.dto.GetChangesRequest;
import org.junit.Assert;
import org.junit.Test;
import utils.XMLTestSupport;

public class GetChangeRQTranslatorTest extends XMLTestSupport {
    GetChangesRQTranslator getChangesRQTranslator = new GetChangesRQTranslator();
    DeleteKeysRQTranslator deleteKeysRQTranslator = new DeleteKeysRQTranslator();

    @Test
    public void testTranslate() throws Exception {
        GetChangesRequest request = getChangesRQTranslator.translate("Key-01", "Hotel-01");
        Assert.assertTrue(request.getGetChangesRQ().getHotel().equals("Hotel-01"));
        Assert.assertTrue(request.getGetChangesRQ().getKeysList().get(0).equals("Key-01"));
    }

    @Test
    public void deleteKeyTranslate() throws Exception {
        DeleteKeysRequest request = deleteKeysRQTranslator.translate(ChangesPushTask.build("Hotel-A", "Key-A"), "Token-A");
        Assert.assertTrue(request.getDeleteKeysRQ().getHotel().equals("Hotel-A"));
        Assert.assertTrue(request.getDeleteKeysRQ().getKeyTokensList().get(0).getKey().equals("Key-A"));
        Assert.assertTrue(request.getDeleteKeysRQ().getKeyTokensList().get(0).getTokensList().get(0).equals("Token-A"));
    }
}
