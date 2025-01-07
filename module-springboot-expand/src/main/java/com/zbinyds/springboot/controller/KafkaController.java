package com.zbinyds.springboot.controller;

import com.zbinyds.springboot.service.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zbinyds
 * @since 2024-12-28 16:01
 */

@CrossOrigin
@RestController
@RequestMapping("/kafka")
@RequiredArgsConstructor
public class KafkaController {
    private final KafkaProducer kafkaProducer;

    @PostMapping("/send")
    public void sendMessage(String message) {
        kafkaProducer.sendMessage(message);
    }
}
