package com.learn.snake.plugin.handler;

import com.learn.snake.common.SpanManager;
import com.learn.snake.model.Span;
import com.learn.snake.model.SpanType;
import com.learn.snake.plugin.ProcessConfig;
import com.learn.snake.util.LoggerBuilder;
import org.slf4j.Logger;

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

    private static final Logger logger=LoggerBuilder.getLogger(ProcessHandler.class);

    @Override
    public Span before(String className, String methodName, Object[] allArguments, Object[] extVal) {

        if(!ProcessConfig.init().isEnable()){
            return null;
        }

        //Span的构造
        Span span =SpanManager.createEntrySpan(SpanType.PROCESS);
        logBeginTrace(className,methodName,span,logger);
        span.addTag("method",methodName).addTag("clazz",className);
        return span;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal) {

        Span span=SpanManager.getExitSpan();
        if(span==null){
            return result;
        }

        return null;
    }
}
