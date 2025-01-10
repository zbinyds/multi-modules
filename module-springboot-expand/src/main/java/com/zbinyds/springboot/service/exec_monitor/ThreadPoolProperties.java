package com.zbinyds.springboot.service.exec_monitor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置
 *
 * @Author zbinyds
 * @Create 2024-09-14 09:00
 */

@Data
@Component
@ConfigurationProperties(prefix = "thread-pool")
public class ThreadPoolProperties {
    private List<Instance> executors;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Instance {
        /**
         * 线程池名称
         */
        private String executorName;

        /**
         * 核心线程数
         */
        private Integer coreSize;

        /**
         * 最大线程数
         */
        private Integer maxSize;

        /**
         * 队列容量
         */
        private Integer queueCapacity;

        /**
         * 线程存活时间
         */
        private Long keepAliveTime;

        /**
         * 线程存活时间单位
         */
        private TimeUnit keepAliveTimeUnit;

        /**
         * 拒绝策略
         *
         * @see RejectPolicy
         */
        private RejectPolicy rejectPolicy;

        public ThreadPoolExecutorMonitor toThreadPoolExecutorMonitor() {
            return new ThreadPoolExecutorMonitor(this.getCoreSize(), this.getMaxSize(),
                    this.getKeepAliveTime(), this.getKeepAliveTimeUnit(), new LinkedBlockingQueue<>(this.getQueueCapacity()),
                    ThreadPoolExecutorMonitor.monitorThreadFactory(), this.getRejectPolicy().getHandler());
        }
    }

    @Getter
    @AllArgsConstructor
    public enum RejectPolicy {
        ABORT(new ThreadPoolExecutor.AbortPolicy()),
        DISCARD(new ThreadPoolExecutor.DiscardPolicy()),
        DISCARD_OLDEST(new ThreadPoolExecutor.DiscardOldestPolicy()),
        CALLER_RUNS(new ThreadPoolExecutor.CallerRunsPolicy());

        private final RejectedExecutionHandler handler;
    }
}
