package com.derbysoft.bookit.common.commons.log;


import org.apache.commons.lang3.StringUtils;

public enum OperationType {
    RETRIEVE_LOS_RATE,
    PUSH_LOS_RATE;

    public static OperationType fromCode(String code) {
        for (OperationType operationType : OperationType.values()) {
            if (StringUtils.equalsIgnoreCase(operationType.name(), code)) {
                return operationType;
            }
        }
        return null;
    }
}
