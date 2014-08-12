package com.derbysoft.bookit.common.commons.exception;


public class SystemConfigNotFoundException extends RuntimeException {
    public SystemConfigNotFoundException(String configKey) {
        super(String.format("System Config Key:[%s]", configKey));
    }
}
