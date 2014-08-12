package com.derbysoft.bookit.push.commons.service;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.concurrent.Executor;

public class MailSenderServiceTest {
    MailSenderService mailSenderService = new MailSenderService();
    JavaMailSender javaMailSender;
    Executor mailTaskExecutor;

    @Before
    public void setUp() throws Exception {
        javaMailSender = EasyMock.createMock(JavaMailSender.class);
        mailTaskExecutor = EasyMock.createMock(Executor.class);

        mailSenderService.setMailFromAddress("from");
        mailSenderService.setMailToAddress("to");
        mailSenderService.setMailSender(javaMailSender);
        mailSenderService.setMailTaskExecutor(mailTaskExecutor);
    }

    @Test
    public void testSend() throws Exception {
        mailTaskExecutor.execute(EasyMock.anyObject(Runnable.class));
        EasyMock.expectLastCall();
        mailSenderService.send("title", "content");
    }
}
