package com.derbysoft.bookit.push.commons;
import com.derbysoft.bookit.common.commons.exception.LoadEnvironmentConfigException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public abstract class EnvironmentUtils {

    private static Properties environmentMappings = new Properties();

    private static transient Log logger = LogFactory.getLog(EnvironmentUtils.class);

    static {
        loadMappings();
    }

    public static String get(String key) {
        return environmentMappings.getProperty(key);
    }

    public static String getLosRatePushQueueCapacity() {
        return get("default_los_rate_push_queue_capacity");
    }

    private static void loadMappings() {
        try {
            environmentMappings = PropertiesLoaderUtils.loadAllProperties("environment.properties");
        } catch (IOException e) {
            String errorMsg = "loading environment.properties suffer error.";
            if (logger.isErrorEnabled()) {
                logger.error(errorMsg, e);
            }
            throw new LoadEnvironmentConfigException(errorMsg, e);
        }
    }
}