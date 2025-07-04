package com.zbinyds.springkafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zbinyds
 * @since 2025-05-28 10:49
 */

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<Object, Object> kafkaTemplate;

    /**
     * 使用KafkaTemplate结合spring Transactional kafkaTransactionManager发送事务消息
     *
     * @param topic   主题
     * @param message 消息体
     */
    @Transactional("kafkaTransactionManager")
    public void sendMessageTransactional1(String topic, Object message) {
        kafkaTemplate.send(topic, message);
    }

    /**
     * 使用KafkaTemplate结合KafkaOperations发送事务消息
     *
     * @param topic   主题
     * @param message 消息体
     */
    public void sendMessageTransactional2(String topic, Object message) {
        kafkaTemplate.executeInTransaction(kafkaOperations -> {
            kafkaOperations.send(topic, message);

            return true;
        });
    }
}
