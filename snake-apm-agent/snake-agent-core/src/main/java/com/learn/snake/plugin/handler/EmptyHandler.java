package com.learn.snake.plugin.handler;

import com.learn.snake.model.Span;
import com.learn.snake.util.LoggerBuilder;
import org.slf4j.Logger;

/**
 * @Author :lwy
 * @Date : 2018/10/24 16:40
 * @Description : 默认空的处理器
 */
public class EmptyHandler extends AbstractHandler {
    private static final Logger logger = LoggerBuilder.getLogger(EmptyHandler.class);

    @Override
    public Span before(String className, String methodName, Object[] allArguments, Object[] extVal) {
        Span span = new Span("empty");
        logBeginTrace(className, methodName, span, logger);
        return span;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal) {
        logEndTrace(className, methodName, new Span("empty"), logger);
        return result;
    }
}
