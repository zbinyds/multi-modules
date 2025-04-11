package com.zbinyds.localmessage.annotation;

import java.lang.annotation.*;

/**
 * @author zbinyds
 * @since 2025-04-02 13:12
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LocalMessageManager {
    int messageType();

    long messageId();

    int maxRetryTimes() default 3;

    boolean async() default true;
}
