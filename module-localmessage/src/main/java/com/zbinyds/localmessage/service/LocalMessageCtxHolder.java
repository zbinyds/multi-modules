package com.zbinyds.localmessage.service;

import java.util.Objects;

/**
 * 本地消息上下文持有，通过threadLocal限制aop切面只执行一次
 *
 * @author zbinyds@126.com
 * @since 2025-04-05 13:51
 */
public class LocalMessageCtxHolder {
    private static final ThreadLocal<Boolean> INVOKE_THREAD_LOCAL = new ThreadLocal<>();

    public static boolean isInvoking() {
        return Objects.nonNull(INVOKE_THREAD_LOCAL.get());
    }

    static void setInvoking() {
        INVOKE_THREAD_LOCAL.set(Boolean.TRUE);
    }

    static void invoked() {
        INVOKE_THREAD_LOCAL.remove();
    }
}