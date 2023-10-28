package com.capstone.udrive.service.impl;

import com.capstone.udrive.config.AppConfig;
import com.capstone.udrive.service.EmailCodeService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, EmailCodeServiceImpl.class})
class EmailCodeServiceImplTest {

    @Autowired
    private EmailCodeService emailCodeService;

    @Autowired
    private AppConfig appConfig;

    @Test
    void sendEmailCode() {
        try {
            emailCodeService.sendEmailCode(appConfig.getAdminEmails(), 1234);
        } catch (Exception e) {
            e.printStackTrace();
            fail("An exception was thrown, but none was expected.");
        }
    }
}