package com.zbinyds.learn.satoken.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zbinyds
 * @since 2024-12-06 09:29
 */

@Configuration
public class Knife4jConfiguration {
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(
                new Info()
                        .title("接口文档")
                        .description("这是一个接口文档")
                        .version("1.0.0")
                        .contact(new Contact().name("zbinyds").url("www.xxx.com").email("zbinyds@126.com"))
        );
    }
}
