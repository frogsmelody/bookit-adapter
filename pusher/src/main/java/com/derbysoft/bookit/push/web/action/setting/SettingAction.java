package com.derbysoft.bookit.push.web.action.setting;

import com.derbysoft.bookit.common.domain.common.Setting;
import com.derbysoft.bookit.common.model.SystemConfigKeys;
import com.derbysoft.bookit.common.repository.SettingRepository;
import com.derbysoft.bookit.push.commons.factory.RatePushThreadPoolFactory;
import com.derbysoft.bookit.push.web.action.setting.support.SettingValues;
import com.derbysoft.bookit.push.web.action.setting.support.Settings;
import com.derbysoft.bookit.push.web.action.support.AbstractDMXAction;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Author: Jason Wu
 * Date  : 2013-09-23
 */
public class SettingAction extends AbstractDMXAction {

    @Autowired
    private SettingRepository settingRepository;

    private Settings settings = new Settings();
    private SettingValues configValues;

    @Action("list")
    public String list() {
        try {
            settings.setLosRatePushThreadCount(load(SystemConfigKeys.LOS_RATE_PUSH_THREAD_COUNT.getKey()));
            return populateSucceededJsonResult(settings);
        } catch (Exception e) {
            return populateFailedJsonResult(e.getMessage());
        }
    }

    @Action("save")
    public String save() {
        try {
            updateRateChangePushCorePoolSize(configValues.getLosRatePushThreadCount(), SystemConfigKeys.LOS_RATE_PUSH_THREAD_COUNT);
            return populateSucceededJsonResult("Update succeed !!");
        } catch (Exception e) {
            return populateFailedJsonResult(e.getMessage());
        }
    }

    private void updateRateChangePushCorePoolSize(String newValue, SystemConfigKeys systemConfigKeys) {
        update(newValue, systemConfigKeys);
        RatePushThreadPoolFactory.getExecutor().updateThreadCount(Integer.parseInt(newValue));
    }

    private void update(String newValue, SystemConfigKeys systemConfigKeys) {
        validateInput(newValue, systemConfigKeys);
        Setting setting = load(systemConfigKeys.getKey());
        if (setting != null && !StringUtils.equalsIgnoreCase(newValue, setting.getValue())) {
            setting.setValue(newValue);
            settingRepository.save(setting);
        }
    }

    private void validateInput(String newValue, SystemConfigKeys systemConfigKeys) {
        if (StringUtils.isBlank(newValue) || !NumberUtils.isDigits(newValue.trim())) {
            throw new IllegalArgumentException(String.format("Update failed, key:[ %s ] input an invalid value.", systemConfigKeys.getKey()));
        }
    }

    private Setting load(String name) {
        return settingRepository.load(name);
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public SettingValues getConfigValues() {
        return configValues;
    }

    public void setConfigValues(SettingValues configValues) {
        this.configValues = configValues;
    }

    public void setSettingRepository(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }
}
