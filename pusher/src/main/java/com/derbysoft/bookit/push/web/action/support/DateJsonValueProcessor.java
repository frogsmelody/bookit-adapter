package com.derbysoft.bookit.push.web.action.support;
import com.derbysoft.bookit.common.commons.DateTimeUtils;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.util.Date;

public class DateJsonValueProcessor implements JsonValueProcessor {

    public Object processArrayValue(Object arg0, JsonConfig arg1) {
        return process(arg0);
    }

    public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
        return process(arg1);
    }

    private Object process(Object value) {
        return DateTimeUtils.formatDateTime((Date) value);
    }

}
