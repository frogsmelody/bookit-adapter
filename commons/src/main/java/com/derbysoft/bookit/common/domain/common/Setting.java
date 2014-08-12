package com.derbysoft.bookit.common.domain.common;

import com.derbysoft.common.domain.PersistenceSupport;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Setting extends PersistenceSupport {
    @Column(unique = true, nullable = false, length = 100)
    private String name;

    private String value;

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
