package com.en.ims;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ImsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImsApplication.class, args);
    }

}
