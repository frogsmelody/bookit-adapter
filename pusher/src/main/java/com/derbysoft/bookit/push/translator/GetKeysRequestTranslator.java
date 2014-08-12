package com.derbysoft.bookit.push.translator;

import com.derbysoft.bookit.common.ccs.SystemConfig;
import com.derbysoft.bookit.common.model.SystemConfigKeys;
import com.derbysoft.bookit.common.repository.SystemConfigRepository;
import com.derbysoft.bookit.common.translator.Translator;
import com.derbysoft.synchronizer.dto.GetKeysRQ;
import com.derbysoft.synchronizer.dto.Level;
import com.derbysoft.synchronizer.remote.dto.GetKeysRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("getKeysRequestTranslator")
public class GetKeysRequestTranslator extends BaseTranslator implements Translator<List<String>, Void, GetKeysRequest> {

    private static final Level DEFAULT_LEVEL = Level.RATE;
    @Autowired
    private SystemConfigRepository systemConfigRepository;

    @Override
    public GetKeysRequest translate(List<String> hotelCodes, Void aVoid) {
        GetKeysRequest request = new GetKeysRequest();
        request.setHeader(createHeader());
        GetKeysRQ getKeysRQ = new GetKeysRQ();
        getKeysRQ.setLevel(getRateSyncLevel());
        getKeysRQ.setHotelsList(hotelCodes);
        request.setGetKeysRQ(getKeysRQ);
        return request;
    }

    private Level getRateSyncLevel() {
        SystemConfig systemConfig = systemConfigRepository.load(SystemConfigKeys.LOS_RATE_PULL_LEVEL.getKey());
        if (systemConfig == null) {
            return DEFAULT_LEVEL;
        }
        return matchLevel(systemConfig.getValue());
    }

    private Level matchLevel(String levelConfig) {
        for (Level level : Level.values()) {
            if (StringUtils.equalsIgnoreCase(level.name(), levelConfig)) {
                return level;
            }
        }
        return DEFAULT_LEVEL;
    }

    public void setSystemConfigRepository(SystemConfigRepository systemConfigRepository) {
        this.systemConfigRepository = systemConfigRepository;
    }
}
