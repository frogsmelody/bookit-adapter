package com.derbysoft.bookit.common.ccs;

import com.derbysoft.ccs.core.MappingEntry;

public class SystemConfig implements MappingEntry {
    private String name;
    private String value;

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public void setDisabled(boolean b) {

    }

    public SystemConfig() {
    }

    public SystemConfig(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
