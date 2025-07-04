package com.zbinyds.springkafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

/**
 * @see org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
 * @author zbinyds
 * @since 2025-05-28 10:57
 */

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaAdmin.NewTopics newTopics() {
        return new KafkaAdmin.NewTopics(
                new NewTopic("Test1", 1, (short) 1),
                new NewTopic("Test2", 3, (short) 1)
        );
    }

}
