package com.zbinyds.localmessage.aspect;

import cn.hutool.core.date.DateUtil;
import com.zbinyds.localmessage.annotation.LocalMessageManager;
import com.zbinyds.localmessage.model.dto.LocalMessageDTO;
import com.zbinyds.localmessage.model.enums.TaskStatus;
import com.zbinyds.localmessage.model.po.LocalMessagePO;
import com.zbinyds.localmessage.service.LocalMessageService;
import com.zbinyds.localmessage.service.LocalMessageCtxHolder;
import com.zbinyds.localmessage.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zbinyds@126.com
 * @since 2025-04-04 19:51
 */

@Aspect
@Component
@RequiredArgsConstructor
public class LocalMessageAspect {
    private final LocalMessageService localMessageService;

    @Around(value = "@annotation(localMessageManager)")
    public Object handle(ProceedingJoinPoint joinPoint, LocalMessageManager localMessageManager) throws Throwable {
        if (LocalMessageCtxHolder.isInvoking() || !TransactionSynchronizationManager.isActualTransactionActive()) {
            return joinPoint.proceed();
        }
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        // 方法参数
        List<String> params = Arrays.stream(method.getParameterTypes())
                .map(Class::getName)
                .collect(Collectors.toList());
        LocalMessageDTO snapshot = LocalMessageDTO.builder()
                .className(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .paramNames(JsonUtil.toJson(params))
                .args(JsonUtil.toJson(joinPoint.getArgs()))
                .build();
        LocalMessagePO localMessage = LocalMessagePO.builder()
                .messageType(localMessageManager.messageType())
                .messageId(localMessageManager.messageId())
                .reqSnapshot(JsonUtil.toJson(snapshot))
                .status(TaskStatus.INIT.name())
                .maxRetryTimes(localMessageManager.maxRetryTimes())
                .nextRetryTime(DateUtil.offsetMinute(new Date(), LocalMessageService.RETRY_INTERVAL_MINUTES))
                .build();
        localMessageService.invoke(localMessage, localMessageManager.async());
        return null;
    }
}
