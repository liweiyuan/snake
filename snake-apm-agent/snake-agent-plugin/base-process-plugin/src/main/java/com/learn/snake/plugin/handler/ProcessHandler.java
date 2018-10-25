package com.learn.snake.plugin.handler;

import com.learn.snake.common.SpanManager;
import com.learn.snake.model.Span;
import com.learn.snake.model.SpanType;
import com.learn.snake.plugin.ProcessConfig;
import com.learn.snake.transmit.TransmitterFactory;
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

    private static final String KEY_ERROR_THROWABLE = "_ERROR_THROWABLE";
    private static final String KEY_BEE_CHILD_ID = "_BEE_CHILD_ID";
    private static final String KEY_ERROR_POINT = "_ERROR_POINT";

    @Override
    public Span before(String className, String methodName, Object[] allArguments, Object[] extVal) {

        if(!ProcessConfig.init().isEnable()){
            return null;
        }

        //Span的构造
        Span span =SpanManager.createEntrySpan(SpanType.PROCESS);
        //方法开始前执行逻辑
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

        Throwable childThrowable = (Throwable) span.getTags().get(KEY_ERROR_THROWABLE);
        String childId = (String) span.getTags().get(KEY_BEE_CHILD_ID);
        String childErrorPoint = (String) span.getTags().get(KEY_ERROR_POINT);
        String errorPoint = className + "." + methodName;
        span.removeTag(KEY_ERROR_THROWABLE);
        span.removeTag(KEY_BEE_CHILD_ID);
        span.removeTag(KEY_ERROR_POINT);

        if(!ProcessConfig.init().isEnable()){
            return null;
        }

        //计算执行时间
        calculateSpend(span);
        //方法执行后记录数据
        logEndTrace(className,methodName,span,logger);
        //耗时阈值检测 TODO
        if(span.getSpend()>ProcessConfig.init().getSpend()){
            //发送日志传输
            TransmitterFactory.offerQueue(span);
            //TODO
        }
        //异常处理
        return result;
    }
}
