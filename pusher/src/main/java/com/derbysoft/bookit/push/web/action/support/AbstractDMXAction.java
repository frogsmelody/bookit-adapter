package com.derbysoft.bookit.push.web.action.support;

import com.derbysoft.bookit.common.commons.XMLUtils;
import com.derbysoft.common.web.struts2.support.PaginateActionSupport;
import com.opensymphony.xwork2.ActionContext;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Results(value = {
        @Result(name = AbstractDMXAction.AJAX_RESULT, location = "/WEB-INF/page/admin/AjaxResult.jsp")
})
public abstract class AbstractDMXAction extends PaginateActionSupport {
    public static final String AJAX_RESULT = "AjaxResult";
    public static final String RESULT = "result";
    public static final String MESSAGE = "message";

    private String ajaxResult;
    private Object msg;

    protected String prettyFormat(String logDetail) {
        try {
            return XMLUtils.fromXMLWithString(logDetail);
        } catch (Exception ex) {
            return logDetail;
        }
    }

    protected String populateSucceededJsonResult(Object message, String... excludes) {
        return populateJsonResult(Boolean.TRUE, message, excludes);
    }

    protected String populateFailedJsonResult(Object message, String... excludes) {
        return populateJsonResult(Boolean.FALSE, message, excludes);
    }

    protected String populateFailedJsonResult(String prompt) {
        return populateJsonResult(Boolean.FALSE, prompt);
    }

    protected String populateFailedJsonResult(Throwable throwable) {
        return populateJsonResult(Boolean.FALSE, String.format("%s: %s", throwable.getClass().getSimpleName(),
                ExceptionUtils.toString(throwable)));
    }

    private String populateJsonResult(Boolean result, Object message, String... excludes) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(RESULT, result);
        map.put(MESSAGE, message);
        JsonConfig conf = new JsonConfig();
        conf.setExcludes(excludes);
        conf.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        conf.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor());
        JSONObject jsonObject = JSONObject.fromObject(map, conf);
        ajaxResult = jsonObject.toString();
        this.msg = message;
        return AJAX_RESULT;
    }

    public String getAjaxResult() {
        return ajaxResult;
    }

    protected Long getId() {
        String id = getParameter("id");
        if (id == null || id.trim().equals("")) {
            return null;
        }
        return new Long(id);
    }

    protected String getParameter(String parameterName) {
        Object parameter = ActionContext.getContext().getParameters().get(parameterName);
        if (parameter == null) {
            return null;
        }
        if (String.class.isAssignableFrom(parameter.getClass())) {
            return parameter.toString();
        }
        Object[] parameters = (Object[]) parameter;
        if (parameters.length == 0) {
            return null;
        }
        return parameters[0].toString();
    }

    public Object getMsg() {
        return msg;
    }
}
