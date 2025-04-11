package com.zbinyds.localmessage.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zbinyds.localmessage.mapper.LocalMessageMapper;
import com.zbinyds.localmessage.model.dto.LocalMessageDTO;
import com.zbinyds.localmessage.model.enums.TaskStatus;
import com.zbinyds.localmessage.model.po.LocalMessagePO;
import com.zbinyds.localmessage.util.JsonUtil;
import com.zbinyds.localmessage.util.TransactionUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author zbinyds@126.com
 * @since 2025-04-04 21:09
 */

@Component
@EnableScheduling
@RequiredArgsConstructor
public class LocalMessageService {
    private static final Logger log = LoggerFactory.getLogger(LocalMessageService.class);
    private static final ObjectMapper OBJ_MAPPER = new ObjectMapper();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    public static final int RETRY_INTERVAL_MINUTES = 2;

    private final LocalMessageMapper localMessageMapper;
    private final ApplicationContext applicationContext;

    @Scheduled(cron = "*/5 * * * * ?")
    public void retry() {
        Date now = new Date();
        //查2分钟前的失败数据。避免刚入库的数据被查出来
        DateTime afterTime = DateUtil.offsetMinute(now, -LocalMessageService.RETRY_INTERVAL_MINUTES);

        List<LocalMessagePO> waitRetryLocalMessageList = localMessageMapper.getWaitRetryRecords(now, afterTime);
        for (LocalMessagePO localMessage : waitRetryLocalMessageList) {
            doAsyncInvoke(localMessage);
        }
    }

    public void invoke(LocalMessagePO localMessagePO, boolean async) {
        boolean inTx = TransactionSynchronizationManager.isActualTransactionActive();
        if (!inTx) {
            // 上下文处于非事务状态中，不做处理直接执行即可
            return;
        }
        saveLocalMessage(localMessagePO);

        TransactionUtil.doAfterCommitCallback(() -> {
            // 事务成功提交后回调
            if (async) {
                doAsyncInvoke(localMessagePO);
            } else {
                doInvoke(localMessagePO);
            }
        });
    }

    public void doAsyncInvoke(LocalMessagePO localMessagePO) {
        executorService.execute(() -> doInvoke(localMessagePO));
    }

    public void doInvoke(LocalMessagePO localMessagePO) {
        String reqSnapshot = localMessagePO.getReqSnapshot();
        LocalMessageDTO localMessageDTO = JsonUtil.fromJson(reqSnapshot, LocalMessageDTO.class);

        try {
            LocalMessageCtxHolder.setInvoking();

            Class<?> target = Class.forName(localMessageDTO.getClassName());
            Object bean = applicationContext.getBean(target);

            List<Class<?>> paramTypes = getParamTypes(JsonUtil.fromJsonToList(localMessageDTO.getParamNames(), String.class));
            Method method = ReflectUtil.getMethod(target, localMessageDTO.getMethodName(), paramTypes.toArray(new Class[]{}));
            Object[] args = getArgs(paramTypes, localMessageDTO.getArgs());

            // 执行原目标方法
            method.invoke(bean, args);

            removeLocalMessage(localMessagePO);
        } catch (Throwable e) {
            log.error("LocalMessageService invoke failed, recordId: {}, error: {}", localMessagePO.getId(), e.getMessage(), e);
            retryLocalMessage(localMessagePO, e.getMessage());
        } finally {
            LocalMessageCtxHolder.invoked();
        }
    }

    private void saveLocalMessage(LocalMessagePO localMessagePO) {
        localMessageMapper.insert(localMessagePO);
    }

    private void removeLocalMessage(LocalMessagePO localMessagePO) {
        localMessageMapper.deleteById(localMessagePO.getId());
    }

    private void retryLocalMessage(LocalMessagePO localMessagePO, String errMessage) {
        int retryTimes = localMessagePO.getRetryTimes() + 1;

        LocalMessagePO updatePO = LocalMessagePO.builder()
                .id(localMessagePO.getId())
                .nextRetryTime(getNextRetryTime(retryTimes))
                .failReason(errMessage)
                .build();
        if (retryTimes > localMessagePO.getMaxRetryTimes()) {
            // 超出最大重试次数
            updatePO.setStatus(TaskStatus.FAIL.name());
        } else {
            updatePO.setRetryTimes(retryTimes);
            updatePO.setStatus(TaskStatus.RETRY.name());
        }
        localMessageMapper.updateById(updatePO);
    }

    private List<Class<?>> getParamTypes(List<String> params) {
        return params.stream()
                .map(name -> {
                    try {
                        return Class.forName(name);
                    } catch (ClassNotFoundException e) {
                        log.warn("Parameter class not found: {}", name, e);
                        throw new IllegalArgumentException("Parameter class not found: " + name, e);
                    }
                })
                .collect(Collectors.toList());
    }

    private Object[] getArgs(List<Class<?>> paramTypes, String argsJson) {
        List<Object> args = JsonUtil.fromJsonToList(argsJson, Object.class);
        return IntStream.range(0, paramTypes.size())
                .mapToObj(i -> convertArg(paramTypes.get(i), args.get(i)))
                .toArray();
    }

    private Object convertArg(Class<?> targetType, Object arg) {
        if (targetType.isAssignableFrom(arg.getClass())) {
            return arg;
        }
        try {
            byte[] paramBytes = OBJ_MAPPER.writeValueAsBytes(arg);
            return OBJ_MAPPER.readValue(paramBytes, targetType);
        } catch (IOException e) {
            log.error("Failed to convert argument: {}", arg, e);
            throw new IllegalArgumentException("Argument conversion failed", e);
        }
    }

    /**
     * 根据重试次数计算下一次重试的时间
     * 本方法通过重试次数计算出需要等待的分钟数，从而确定下一次重试的具体时间
     * 这是为了避免短时间内多次重试，通过增加等待时间来逐步减少重试频率
     *
     * @param retryTimes 重试次数，用于计算等待时间
     * @return 下一次重试的时间
     */
    private static Date getNextRetryTime(Integer retryTimes) {
        // 计算等待分钟数，使用重试间隔的指数增长来增加等待时间 2min 4min 8min...
        double waitMinutes = Math.pow(RETRY_INTERVAL_MINUTES, retryTimes);
        return DateUtil.offsetMinute(new Date(), (int) waitMinutes);
    }
}
