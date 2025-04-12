package com.zbinyds.localmessage.util;

import lombok.AllArgsConstructor;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author zbinyds@126.com
 * @since 2025-04-04 20:09
 */
public class TransactionUtil {
    /**
     * 事务成功提交后的回调方法<br>
     *
     * @param callback task
     */
    public static void doAfterCommitCallback(Runnable callback) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new DoTransactionAfterCommit(callback));
        }
    }

    /**
     * 事务完成后置回调函数定义
     */
    @AllArgsConstructor
    final static class DoTransactionAfterCommit implements TransactionSynchronization {
        private final Runnable callback;

        @Override
        public void afterCommit() {
            this.callback.run();
        }
    }
}
