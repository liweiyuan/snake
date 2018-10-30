package com.learn.snake.servlet.plugin.handler;

import com.learn.snake.model.Span;
import com.learn.snake.plugin.handler.AbstractHandler;

/**
 * @Author :lwy
 * @Date : 2018/10/30 15:19
 * @Description :
 */
public class ServletHandler extends AbstractHandler {
    @Override
    public Span before(String className, String methodName, Object[] allArguments, Object[] extVal) {
        return null;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal) {
        return null;
    }
}
