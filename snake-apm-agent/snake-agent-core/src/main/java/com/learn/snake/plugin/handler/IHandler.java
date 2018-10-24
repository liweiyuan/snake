package com.learn.snake.plugin.handler;

import com.learn.snake.model.Span;

/**
 * @Author :lwy
 * @Date : 2018/10/24 16:08
 * @Description : 拦截处理接口
 */
public interface IHandler {

    /**
     * 方法调用前处理
     * @return
     */
    Span before(String className, String methodName, Object[] allArguments, Object[] extVal);
    /**
     * 方法执行完处理
     * @return
     */
    Object after(String className,String methodName, Object[] allArguments, Object result,Throwable t,Object[] extVal);
}
