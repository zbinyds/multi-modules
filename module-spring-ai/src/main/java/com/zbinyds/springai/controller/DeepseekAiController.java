package com.zbinyds.springai.controller;

import com.zbinyds.springai.util.ChatAiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author zbinyds
 * @since 2025-03-11 15:45
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/ds")
@RequiredArgsConstructor
public class DeepseekAiController {
    private final ChatAiUtil chatAiUtil;

    @GetMapping("/chat")
    public SseEmitter chat1(String message) {
        return chatAiUtil.chat(message);
    }
}
