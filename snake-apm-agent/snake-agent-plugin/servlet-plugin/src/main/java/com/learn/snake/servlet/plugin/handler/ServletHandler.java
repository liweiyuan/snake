package com.learn.snake.servlet.plugin.handler;

import com.learn.snake.common.HeaderKey;
import com.learn.snake.common.SnakeTraceContext;
import com.learn.snake.common.SpanManager;
import com.learn.snake.model.Span;
import com.learn.snake.model.SpanType;
import com.learn.snake.plugin.handler.AbstractHandler;
import com.learn.snake.servlet.plugin.ServletConfig;
import com.learn.snake.servlet.plugin.common.SnakeHttpResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author :lwy
 * @Date : 2018/10/30 15:19
 * @Description :
 */
public class ServletHandler extends AbstractHandler {
    @Override
    public Span before(String className, String methodName, Object[] allArguments, Object[] extVal) {

        if (!ServletConfig.init().isEnable()) {
            return null;
        }

        HttpServletRequest request = (HttpServletRequest) allArguments[0];
        HttpServletResponse response = (HttpServletResponse) allArguments[1];

        String url = request.getRequestURL().toString();
        if (ServletConfig.init().checkUrlSuffixExclude(url)) {
            return null;
        }
        //构造Span

        Span currentSpan = SpanManager.getCurrentSpan();

        if (currentSpan == null || !currentSpan.getType().equals(SpanType.REQUEST)) {
            SnakeTraceContext.setPId(request.getHeader(HeaderKey.PID));
            SnakeTraceContext.setGid(request.getHeader(HeaderKey.GID));
            SnakeTraceContext.setCTag(request.getHeader(HeaderKey.CTAG));

            Span span = SpanManager.createEntrySpan(SpanType.REQUEST);

            //TODO 这里有点不一样
            span.addTag("srcApp",request.getHeader(HeaderKey.SRC_CLUSTER));
            span.addTag("srcInst",request.getHeader(HeaderKey.SRC_SERVER));
            if(ServletConfig.init().isEnableRespBody() && !response.getClass().getSimpleName().equals("SnakeHttpResponseWrapper")){
                SnakeHttpResponseWrapper wrapper = new SnakeHttpResponseWrapper(response);
                span.addTag("_respWrapper",wrapper);//在ServletAdvice里取出来要清除掉
            }
            return span;
        }
        return null;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal) {
        return null;
    }
}
