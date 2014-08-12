package com.derbysoft.bookit.push.web.action.webservice;

import com.derbysoft.bookit.push.web.action.support.AbstractDMXAction;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

public class WebServiceAction extends AbstractDMXAction {
    private WebServiceCondition webServiceCondition;

    @Autowired
    private DswitchWebService dswitchWebService;

    @Action("getLosRateChange")
    public String getLosRateChange() {
        try {
            if (webServiceCondition == null) {
                throw new IllegalArgumentException("WebServiceCondition is null");
            }
            return populateSucceededJsonResult(dswitchWebService.getLosRateChange(webServiceCondition));
        } catch (Exception ex) {
            return populateFailedJsonResult(ex);
        }
    }

    @Action("getKeys")
    public String getKeys() {
        try {
            if (webServiceCondition == null) {
                throw new IllegalArgumentException("WebServiceCondition is null");
            }
            return populateSucceededJsonResult(dswitchWebService.getKeys(webServiceCondition));
        } catch (Exception ex) {
            return populateFailedJsonResult(ex);
        }
    }

    @Action("getChanges")
    public String getChanges() {
        try {
            if (webServiceCondition == null) {
                throw new IllegalArgumentException("WebServiceCondition is null");
            }
            return populateSucceededJsonResult(dswitchWebService.getChanges(webServiceCondition));
        } catch (Exception ex) {
            return populateFailedJsonResult(ex);
        }
    }

    public void setDswitchWebService(DswitchWebService dswitchWebService) {
        this.dswitchWebService = dswitchWebService;
    }

    public WebServiceCondition getWebServiceCondition() {
        return webServiceCondition;
    }

    public void setWebServiceCondition(WebServiceCondition webServiceCondition) {
        this.webServiceCondition = webServiceCondition;
    }
}
