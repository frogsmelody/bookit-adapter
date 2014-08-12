package com.derbysoft.bookit.push.web.action.setting;

import com.derbysoft.bookit.common.domain.common.Setting;
import com.derbysoft.bookit.common.repository.SettingRepository;
import com.derbysoft.bookit.push.commons.factory.RatePushThreadPoolFactory;
import com.derbysoft.bookit.push.web.action.setting.support.SettingValues;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SettingActionTest {
    SettingAction settingAction = new SettingAction();
    SettingRepository settingRepository;

    @Before
    public void setUp() throws Exception {
        settingRepository = EasyMock.createMock(SettingRepository.class);
        settingAction.setSettingRepository(settingRepository);
    }

    @Test
    public void invalidInput() {
        SettingValues configValues = new SettingValues();
        configValues.setLosRatePushThreadCount("xxx");
        settingAction.setConfigValues(configValues);
        settingAction.save();
        Assert.assertTrue(settingAction.getAjaxResult().contains("Update failed, key:[ los_rate_push_thread_count ] input an invalid value."));
    }

    @Test
    public void testSave() throws Exception {
        SettingValues configValues = new SettingValues();
        configValues.setLosRatePushThreadCount("234");
        settingAction.setConfigValues(configValues);
        Setting setting = new Setting();
        setting.setValue("20");
        EasyMock.expect(settingRepository.load(EasyMock.anyObject(String.class))).andReturn(setting);
        settingRepository.save(EasyMock.anyObject(Setting.class));
        EasyMock.expectLastCall().andAnswer(new IAnswer<Object>() {
            @Override
            public Object answer() throws Throwable {
                Setting newSetting = (Setting) EasyMock.getCurrentArguments()[0];
                Assert.assertTrue(newSetting.getValue().equals("234"));
                return null;
            }
        });
        EasyMock.replay(settingRepository);
        settingAction.save();
        Assert.assertTrue(settingAction.getAjaxResult().contains("Update succeed !!"));
        Assert.assertTrue(RatePushThreadPoolFactory.getExecutor().getThreadCount() == 234);
    }

    @Test
    public void testList() throws Exception {
        Setting setting = new Setting();
        setting.setValue("20");
        setting.setName("keys");
        EasyMock.expect(settingRepository.load(EasyMock.anyObject(String.class))).andReturn(setting);
        EasyMock.replay(settingRepository);
        settingAction.list();
        Assert.assertTrue(settingAction.getAjaxResult().contains("\"value\":\"20\""));
    }
}
