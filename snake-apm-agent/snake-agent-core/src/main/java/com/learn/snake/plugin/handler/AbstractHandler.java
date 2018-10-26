package com.learn.snake.plugin.handler;

import com.learn.snake.model.Span;
import org.slf4j.Logger;

/**
 * @Author :lwy
 * @Date : 2018/10/24 16:11
 * @Description :
 */
public abstract class AbstractHandler implements IHandler {

    public void logBeginTrace(String className, String methodName, Span span, Logger log) {
        log.info("[begin]{}.{} tc={}", className, methodName, span.getType());
    }

    public void logEndTrace(String className, String methodName, Span span, Logger log) {
        log.info("[end]{}.{} tc={}", className, methodName, span.getType());
    }

    /**
     * 计算花费的时间
     *
     * @param span
     */
    public void calculateSpend(Span span) {
        if (span != null) {
            span.setSpend(System.currentTimeMillis() - span.getTime().getTime());
        }
    }
}
