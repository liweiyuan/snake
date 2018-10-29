package com.learn.snake.httpclient3x.plugin.handler;

import com.learn.snake.common.HeaderKey;
import com.learn.snake.common.SnakeTraceContext;
import com.learn.snake.config.SnakeConfig;
import com.learn.snake.model.Span;
import com.learn.snake.plugin.handler.AbstractHandler;
import com.learn.snake.util.LoggerBuilder;
import org.apache.commons.httpclient.HttpMethod;
import org.slf4j.Logger;

/**
 * @Author :lwy
 * @Date : 2018/10/29 15:49
 * @Description :
 */
public class HttpClient3xHandler extends AbstractHandler {

    private static final Logger logger=LoggerBuilder.getLogger(HttpClient3xHandler.class);

    @Override
    public Span before(String className, String methodName, Object[] allArguments, Object[] extVal) {

        try {
            for (Object allArgument : allArguments) {
                if (allArgument instanceof HttpMethod) {
                    HttpMethod req = (HttpMethod) allArgument;
                    if (req.getRequestHeader(HeaderKey.GID) == null) {
                        req.setRequestHeader(HeaderKey.GID, SnakeTraceContext.getGId());
                        req.setRequestHeader(HeaderKey.PID, SnakeTraceContext.getCurrentId());
                        req.setRequestHeader(HeaderKey.CTAG, SnakeTraceContext.getCTag());
                        req.setRequestHeader(HeaderKey.SRC_CLUSTER, SnakeConfig.init().getCluster());
                        req.setRequestHeader(HeaderKey.SRC_SERVER, SnakeConfig.init().getServer());
                    }
                }
            }
        }catch (Exception e){
            logger.error("HttpClient3xHandler 执行失败",e);
        }
        return null;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal) {
        return result;
    }
}
