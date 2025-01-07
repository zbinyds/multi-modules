package com.zbinyds.learn.satoken;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author zbinyds
 * @Create 2024-11-19 14:53
 */

@Slf4j
@MapperScan(basePackages = "com.zbinyds.learn.satoken.mapper")
@SpringBootApplication(scanBasePackages = {"com.zbinyds.learn.satoken"})
public class SaTokenAppRunner {
    public static void main(String[] args) {
        SpringApplication.run(SaTokenAppRunner.class, args);
    }
}
