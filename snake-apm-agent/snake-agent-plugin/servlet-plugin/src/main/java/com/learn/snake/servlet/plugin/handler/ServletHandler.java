package com.learn.snake.servlet.plugin.handler;

import com.alibaba.fastjson.JSON;
import com.learn.snake.common.CollectRatio;
import com.learn.snake.common.HeaderKey;
import com.learn.snake.common.SnakeTraceContext;
import com.learn.snake.common.SpanManager;
import com.learn.snake.model.Span;
import com.learn.snake.model.SpanType;
import com.learn.snake.plugin.handler.AbstractHandler;
import com.learn.snake.servlet.plugin.ServletConfig;
import com.learn.snake.servlet.plugin.common.RequestBodyHolder;
import com.learn.snake.servlet.plugin.common.SnakeHttpResponseWrapper;
import com.learn.snake.transmit.TransmitterFactory;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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
            span.addTag("srcApp", request.getHeader(HeaderKey.SRC_CLUSTER));
            span.addTag("srcInst", request.getHeader(HeaderKey.SRC_SERVER));
            if (ServletConfig.init().isEnableRespBody() && !response.getClass().getSimpleName().equals("SnakeHttpResponseWrapper")) {
                SnakeHttpResponseWrapper wrapper = new SnakeHttpResponseWrapper(response);
                span.addTag("_respWrapper", wrapper);//在ServletAdvice里取出来要清除掉
            }
            return span;
        }
        return null;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal) {

        Span currentSpan = SpanManager.getCurrentSpan();
        if (!ServletConfig.init().isEnable() || CollectRatio.no()) {
            return null;
        }
        //判断当前的Span的类型--request(http)
        if (currentSpan != null && currentSpan.getType().equalsIgnoreCase(SpanType.REQUEST)) {
            Span span = SpanManager.getExitSpan();

            //获取参数列表
            HttpServletRequest request = (HttpServletRequest) allArguments[0];
            HttpServletResponse response = (HttpServletResponse) allArguments[1];

            //添加Span标签
            span.addTag("url", request.getRequestURL());
            span.addTag("remote", request.getRemoteAddr());
            span.addTag("method", request.getMethod());

            //添加class
            calculateSpend(span);
            if (span.getSpend() > ServletConfig.init().getSpend() && CollectRatio.yes()) {

                //添加到response中
                response.addHeader(HeaderKey.GID, span.getGid()); //追踪id
                response.addHeader(HeaderKey.ID, span.getId());

                //传输
                TransmitterFactory.offerQueue(span);
                //采集参数
                collectRequestParameter(span,request);
                //采集body
                collectRequestBody(span,request);
                //采集header
                collectRequestHeader(span,request);
                //采集response body
                collectResponseBody(span,response);
            }
            return result;

        }

        return null;
    }




    /**
     * 采集request参数
     * @param span
     * @param request
     */
    private void collectRequestParameter(Span span, HttpServletRequest request) {
        if(ServletConfig.init().isEnableReqParam()){
            Map<String, String[]> parameterMap = request.getParameterMap();
            Span parameterSpan=new Span(SpanType.REQUEST_PARAM);
            parameterSpan.setId(span.getId());
            parameterSpan.addTag("param",JSON.toJSONString(parameterMap));
            //传输
            TransmitterFactory.offerQueue(parameterSpan);
        }
    }

    /**
     * 采集body参数
     * @param span
     * @param request
     */
    private void collectRequestBody(Span span, HttpServletRequest request) {

        if(ServletConfig.init().isEnableReqBody()){
            try {
                //触发获取body的代码植入
                request.getInputStream();
            }catch (Exception e){
            }
            if(StringUtils.isNotBlank(RequestBodyHolder.getRequestBody())) {
                Span bodySpan = new Span(SpanType.REQUEST_BODY);
                bodySpan.setId(span.getId());
                bodySpan.addTag("body", RequestBodyHolder.getRequestBody());
                TransmitterFactory.offerQueue(bodySpan);
            }
        }
    }

    /**
     * 获取hader参数
     * @param span
     * @param request
     */
    private void collectRequestHeader(Span span, HttpServletRequest request) {

        if(ServletConfig.init().isEnableReqHeaders()){
            Map<String,String> headerMap=new HashMap<>();
            Enumeration<String> enumeration = request.getHeaderNames();
            while (enumeration.hasMoreElements()){
                String key = enumeration.nextElement();
                String value=request.getHeader(key);
                headerMap.put(key,value);
            }
            if(!headerMap.isEmpty()){
                Span headerSpan=new Span(SpanType.REQUEST_HEADERS);
                headerSpan.setId(span.getId());
                headerSpan.addTag("headers",JSON.toJSONString(headerMap));
                TransmitterFactory.offerQueue(headerSpan);
            }
        }
    }

    /**
     * 获取response body
     * @param span
     * @param response
     */
    private void collectResponseBody(Span span, HttpServletResponse response)  {

        if(ServletConfig.init().isEnableRespBody()){
            SnakeHttpResponseWrapper responseWrapper= (SnakeHttpResponseWrapper) response;

            //初始化
            responseWrapper.out();
            Span respSpan = new Span(SpanType.RESPONSE_BODY);
            respSpan.setId(span.getId());
            String body = new String(responseWrapper.getBytes());
            if(StringUtils.isNotBlank(body)){
                respSpan.addTag("body", body);
                TransmitterFactory.offerQueue(respSpan);
            }
        }
    }
}
