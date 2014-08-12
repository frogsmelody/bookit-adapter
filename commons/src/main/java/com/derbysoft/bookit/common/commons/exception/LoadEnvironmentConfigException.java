package com.derbysoft.bookit.common.commons.exception;

import com.derbysoft.common.exception.AbstractRuntimeException;

public class LoadEnvironmentConfigException extends AbstractRuntimeException {
    public LoadEnvironmentConfigException(String message, Exception e) {
        super(message, e);
    }
}
