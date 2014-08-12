package com.derbysoft.bookit.push.web.listener;

import com.derbysoft.bookit.common.service.job.JobManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

public class WebContextLoaderListener extends ContextLoaderListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebContextLoaderListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        ServletContext application = event.getServletContext();
        application.setAttribute("contextPath", application.getContextPath());
        launchJobs(event.getServletContext());
    }

    private void launchJobs(ServletContext servletContext) {
        try {
            ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            applicationContext.getBean("jobManageService", JobManageService.class).launchAll(applicationContext);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}