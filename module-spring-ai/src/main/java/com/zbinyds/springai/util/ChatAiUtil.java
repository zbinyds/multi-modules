package com.zbinyds.springai.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

/**
 * ai聊天工具类
 *
 * @Author zbinyds
 * @Create 2024-05-21 08:35
 */
@Slf4j
@Component
public class ChatAiUtil implements InitializingBean, DisposableBean {
    private static final ExecutorService CHAT_EXECUTOR = Executors.newFixedThreadPool(10);
    private static final BlockingDeque<Question> QUESTION_DEQUE = new LinkedBlockingDeque<>(1000);
    private static final ExecutorService SINGLE_EXECUTOR = Executors.newSingleThreadExecutor();
    private final ChatClient chatClient;

    public ChatAiUtil(ChatClient.Builder builder) {
        this.chatClient = builder.
                defaultSystem("你是一个IT方向技术精湛的专家，我经常会想你提问一些技术问题，你总是能给我详细的答案。").
                build();
    }

    @Override
    public void afterPropertiesSet() {
        SINGLE_EXECUTOR.submit(() -> {
            while (!SINGLE_EXECUTOR.isShutdown()) {
                try {
                    Question question = QUESTION_DEQUE.take();
                    final SseEmitter emitter = question.getEmitter();
                    if (emitter != null) {
                        CHAT_EXECUTOR.submit(buildAskTaskRunnable(question, emitter));
                    }
                } catch (InterruptedException e) {
                    log.error("question deque take error: ", e);
                }
            }
        });
    }

    @Override
    public void destroy() {
        QUESTION_DEQUE.clear();
        SINGLE_EXECUTOR.shutdown();
        CHAT_EXECUTOR.shutdown();
    }

    public SseEmitter chat(String... questions) {
        if (questions == null || questions.length == 0) {
            return null;
        }
        final SseEmitter sseEmitter = new SseEmitter(0L);
        List<Message> messageList = Arrays.stream(questions).map(UserMessage::new).collect(Collectors.toList());
        Question q = Question.builder().questions(messageList).emitter(sseEmitter).build();
        QUESTION_DEQUE.offer(q);
        return sseEmitter;
    }

    Runnable buildAskTaskRunnable(Question question, SseEmitter emitter) {
        return () -> {
            try {
                Prompt prompt = new Prompt(question.getQuestions());
                Flux<ChatResponse> responseFlux = chatClient.prompt(prompt).stream().chatResponse();
                Iterator<ChatResponse> iterator = responseFlux.toStream().iterator();
                StringBuilder reply = new StringBuilder();
                while (iterator.hasNext()) {
                    ChatResponse chatResponse = iterator.next();
                    String rowRes = chatResponse.getResult().getOutput().getText();
                    log.info(rowRes);
                    reply.append(rowRes);
                    emitter.send(rowRes);
                }
                log.info("question: {}, reply: {}", question, reply);
                emitter.complete();
            } catch (Exception e) {
                log.error("send error", e);
                emitter.completeWithError(e);
            }
        };
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static class Question implements Serializable {
        @Serial
        private static final long serialVersionUID = -2449833202909468008L;
        private SseEmitter emitter;
        private List<Message> questions;
    }
}
