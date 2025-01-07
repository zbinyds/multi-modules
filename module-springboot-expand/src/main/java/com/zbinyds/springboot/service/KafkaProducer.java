package com.zbinyds.springboot.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(KafkaProducer.class);
    @Resource(name = "testTopic1")
    private NewTopic testTopic;
    private final KafkaTemplate<String, String> kafkaTemplate;

    // 发送消息方法
    public void sendMessage(String message) {
        log.info("Sending message: {}", message);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(testTopic.name(), null, System.currentTimeMillis(), String.valueOf(message.hashCode()), message);

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(producerRecord);
        future.addCallback(result -> {
            if (result != null) {
                log.info("生产者消息发送成功");
                ProducerRecord<String, String> resultProducerRecord = result.getProducerRecord();

                log.info("topic: {}, value: {}", resultProducerRecord.topic(), resultProducerRecord.value());
                log.info("producerRecord: {}", resultProducerRecord);
            }
        }, ex -> log.error("生产者消息发送失败, message: {}", ex.getMessage(), ex));
    }
}