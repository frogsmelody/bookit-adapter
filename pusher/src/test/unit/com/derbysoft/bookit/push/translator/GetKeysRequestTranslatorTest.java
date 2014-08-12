package com.derbysoft.bookit.push.translator;

import com.derbysoft.bookit.common.ccs.SystemConfig;
import com.derbysoft.bookit.common.repository.SystemConfigRepository;
import com.derbysoft.synchronizer.dto.Level;
import com.derbysoft.synchronizer.remote.dto.GetKeysRequest;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.XMLTestSupport;

import java.util.ArrayList;
import java.util.List;

public class GetKeysRequestTranslatorTest extends XMLTestSupport {
    GetKeysRequestTranslator translator = new GetKeysRequestTranslator();
    SystemConfigRepository systemConfigRepository;

    @Before
    public void setUp() throws Exception {
        systemConfigRepository = EasyMock.createMock(SystemConfigRepository.class);
        translator.setSystemConfigRepository(systemConfigRepository);
    }

    @Test
    public void testTranslate() throws Exception {
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setValue("RATE_MONTH");
        EasyMock.expect(systemConfigRepository.load(EasyMock.anyObject(String.class))).andReturn(systemConfig);
        EasyMock.replay(systemConfigRepository);

        List<String> hotelCodes = new ArrayList<String>();
        hotelCodes.add("Hotel-A");
        GetKeysRequest getKeysRequest = translator.translate(hotelCodes, null);
        Assert.assertTrue(getKeysRequest.getGetKeysRQ().getLevel() == Level.RATE_MONTH);
    }
}
