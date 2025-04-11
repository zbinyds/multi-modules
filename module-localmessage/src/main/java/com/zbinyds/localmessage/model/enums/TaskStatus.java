package com.zbinyds.localmessage.model.enums;

import org.apache.commons.lang3.EnumUtils;

public enum TaskStatus {
    INIT,
    FAIL,
    RETRY;

    public static TaskStatus of(String status) {
        return EnumUtils.getEnum(TaskStatus.class, status, INIT);
    }
}