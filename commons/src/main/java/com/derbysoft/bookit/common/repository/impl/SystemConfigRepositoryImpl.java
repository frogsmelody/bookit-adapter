package com.derbysoft.bookit.common.repository.impl;

import com.derbysoft.bookit.common.ccs.SystemConfig;
import com.derbysoft.bookit.common.repository.SystemConfigRepository;
import com.derbysoft.ccs.core.MappingCache;


public class SystemConfigRepositoryImpl implements SystemConfigRepository {
    private MappingCache<SystemConfig> systemConfigMappingCache;

    @Override
    public SystemConfig load(String key) {
        return systemConfigMappingCache.get(key);
    }

    public void setSystemConfigMappingCache(MappingCache<SystemConfig> systemConfigMappingCache) {
        this.systemConfigMappingCache = systemConfigMappingCache;
    }
}
