package com.zbinyds.springboot.controller;

import com.zbinyds.springboot.service.exec_monitor.ThreadPoolExecutorMonitor;
import com.zbinyds.springboot.service.exec_monitor.ThreadPoolExecutorMonitorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zbinyds
 * @since 2025-01-08 15:47
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/exec/monitor")
public class ExecMonitorController {
    @Resource
    private ThreadPoolExecutorMonitorContext threadPoolExecutorMonitorContext;
    @Resource(name = "default")
    private ThreadPoolExecutorMonitor threadPoolExecutorMonitor;

    @GetMapping("/test")
    public String monitorTest() {
        // case 1
        ThreadPoolExecutor executorMonitor = threadPoolExecutorMonitorContext.getExecutorMonitor("default");
        executorMonitor.execute(() -> log.info("test1 success! ==> {}", executorMonitor));

        // case 2
        threadPoolExecutorMonitor.execute(() -> log.info("test2 success! ==> {}", executorMonitor));
        log.info("executor equals: {}", executorMonitor.equals(threadPoolExecutorMonitor));

        return "success";
    }
}
