package com.capstone.udrive;

import com.capstone.udrive.config.AppConfig;
import com.capstone.udrive.constants.Constants;
import com.capstone.udrive.spring.ApplicationContextProvider;
import jakarta.servlet.MultipartConfigElement;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableScheduling
@EnableAsync
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"com.capstone.udrive"})
@MapperScan(basePackages = {"com.capstone.udrive.mappers"})
public class UDriveApplication {

    public static void main(String[] args) {
        SpringApplication.run(UDriveApplication.class, args);
    }

    @Bean
    @DependsOn({"applicationContextProvider"})
    MultipartConfigElement multipartConfigElement() {
        AppConfig appConfig = (AppConfig) ApplicationContextProvider.getBean("appConfig");
        MultipartConfigFactory factory = new MultipartConfigFactory();
        assert appConfig != null;
        factory.setLocation(appConfig.getProjectFolder() + Constants.FILE_FOLDER_TEMP);
        return factory.createMultipartConfig();
    }
}


