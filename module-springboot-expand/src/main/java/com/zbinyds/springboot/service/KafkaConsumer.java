package com.zbinyds.springboot.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @author zbinyds
 * @since 2024-12-28 15:55
 */

@Service
public class KafkaConsumer {
    private final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "#{testTopic.name}", groupId = "test_group1")
    public void consumeMessage(ConsumerRecord<String, String> record) {
        log.info("topic: {}, value: {}", record.topic(), record.value());
        log.info("consumerRecord: {}", record);
    }

    @KafkaListener(topics = "#{testTopic.name}", groupId = "test_group1")
    public void consumeMessage2(ConsumerRecord<String, String> record) {
        log.info("topic2: {}, value2: {}", record.topic(), record.value());
        log.info("consumerRecord2: {}", record);
    }
}
