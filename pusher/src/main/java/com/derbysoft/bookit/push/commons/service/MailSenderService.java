package com.derbysoft.bookit.push.commons.service;


import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.concurrent.Executor;

public class MailSenderService {
    private static final String SPLIT_CHAR = ",";
    private Executor mailTaskExecutor;

    private JavaMailSender mailSender;

    private String mailFromAddress;

    private String mailToAddress;

    public void send(final String subject, final String content) {
        mailTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                simpleMailMessage.setFrom(mailFromAddress);
                simpleMailMessage.setTo(getAddresses(mailToAddress));
                simpleMailMessage.setSubject(subject);
                simpleMailMessage.setText(content);
                mailSender.send(simpleMailMessage);
            }
        });
    }

    private String[] getAddresses(String addresses) {
        if (StringUtils.isBlank(addresses)) {
            return null;
        }
        if (addresses.contains(SPLIT_CHAR)) {
            return StringUtils.split(addresses, SPLIT_CHAR);
        }
        return new String[]{addresses};
    }

    public void setMailFromAddress(String mailFromAddress) {
        this.mailFromAddress = mailFromAddress;
    }

    public void setMailToAddress(String mailToAddress) {
        this.mailToAddress = mailToAddress;
    }

    public void setMailTaskExecutor(Executor mailTaskExecutor) {
        this.mailTaskExecutor = mailTaskExecutor;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
}
