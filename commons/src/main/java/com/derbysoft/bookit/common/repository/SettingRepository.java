package com.derbysoft.bookit.common.repository;

import com.derbysoft.bookit.common.domain.common.Setting;


public interface SettingRepository {
    Setting load(String key);

    Setting save(Setting setting);
}
