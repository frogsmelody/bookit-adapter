package com.derbysoft.bookit.common.repository;

import com.derbysoft.bookit.common.ccs.SystemConfig;


public interface SystemConfigRepository {
    SystemConfig load(String key);
}
