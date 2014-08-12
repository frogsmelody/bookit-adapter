package com.derbysoft.bookit.common.repository.impl;

import com.derbysoft.bookit.common.domain.common.Setting;
import com.derbysoft.bookit.common.repository.SettingRepository;
import com.derbysoft.bookit.common.repository.SystemConfigRepository;
import com.derbysoft.common.service.CommonService;

public class SettingRepositoryImpl extends CommonService<Setting> implements SettingRepository {
    @Override
    public Setting load(String key) {
        return load("name", key);
    }
}
