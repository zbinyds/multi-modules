package com.zbinyds.learn.validate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author zbinyds
 * @Create 2024-11-19 14:38
 */

@SpringBootApplication(scanBasePackages = {"com.zbinyds.learn.validate"})
public class ValidAppRunner {
    public static void main(String[] args) {
        SpringApplication.run(ValidAppRunner.class);
    }
}
