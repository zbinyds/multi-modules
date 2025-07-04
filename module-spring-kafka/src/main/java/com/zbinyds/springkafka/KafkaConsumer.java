package com.zbinyds.springkafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

/**
 * @author zbinyds
 * @since 2025-05-28 10:54
 */

@Slf4j
@Component
public class KafkaConsumer {

    /**
     * 消费消息
     * 监听Test1主题，并且设置并发度为1。并发度指同时处理主题分区的线程数量，建议小于等于分区数，每个分区由单个/多个线程并行处理;
     * 配置消费重试策略，默认重试3次，失败后发送到DLT主题; 设置退避策略，延迟1秒重试，指数增长，类似2^n
     *
     * @param record       消息体
     * @param ack          消息确认机制，用于手动提交ack
     * @param topic        主题
     * @param partition    分区
     * @param offset       偏移量
     * @param receivedTime 接收到消息的时间
     */
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, backoff = @Backoff(delay = 1000L, multiplier = 2.0))
    @KafkaListener(topics = "#{@'Test1'.name}", concurrency = "#{@'Test1'.numPartitions}")
    public void receiveMessage(ConsumerRecord<String, String> record,
                               Acknowledgment ack,
                               @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                               @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                               @Header(KafkaHeaders.OFFSET) long offset,
                               @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long receivedTime) {
        try {
            log.info("接收到消息 [topic={}, partition={}, offset={}, key={}, receivedTime={}]",
                    topic, partition, offset, record.key(), receivedTime);
            // 消费消息
//            doConsume(record.key());

            log.info("消息处理成功 [key={}]", record.key());
            ack.acknowledge();
        } catch (Exception e) {
            log.error("未知异常, 消息处理失败 [key={}], error message: {}", record.key(), e.getMessage());
            // 抛出异常以触发重试机制
            throw e;
        }
    }

}
