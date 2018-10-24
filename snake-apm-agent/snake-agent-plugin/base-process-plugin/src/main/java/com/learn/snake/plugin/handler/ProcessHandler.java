package com.learn.snake.plugin.handler;

import com.learn.snake.model.Span;
import com.learn.snake.plugin.ProcessConfig;

/**
 * @Author :lwy
 * @Date : 2018/10/24 16:19
 * @Description :
 * <p>
 * * 关闭process采集，异常也将不采集
 * * 如果想仅采集异常而不采集process，可以把采样率调成0，不影响异常采集
 * </p>
 */
public class ProcessHandler extends AbstractHandler {

    @Override
    public Span before(String className, String methodName, Object[] allArguments, Object[] extVal) {

        if(!ProcessConfig.init().isEnable()){
            return null;
        }

        return null;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal) {
        return null;
    }
}
