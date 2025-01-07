package com.zbinyds.springboot.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zbinyds
 * @since 2024-12-30 14:25
 */

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic testTopic() {
        // 指定topic的 分区数量 以及 副本数量
        return new NewTopic("test-topic", 2, (short) 1);
    }

    @Bean
    public NewTopic testTopic1() {
        // 指定topic的 分区数量 以及 副本数量
        return new NewTopic("test-topic1", 1, (short) 1);
    }
}
