package com.learn.snake.plugin.handler;

import com.learn.snake.common.CollectRatio;
import com.learn.snake.common.SnakeTraceContext;
import com.learn.snake.common.SpanManager;
import com.learn.snake.model.Span;
import com.learn.snake.model.SpanType;
import com.learn.snake.plugin.ProcessConfig;
import com.learn.snake.transmit.TransmitterFactory;
import com.learn.snake.util.LoggerBuilder;
import com.learn.snake.util.SnakeUtils;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.lang.ref.SoftReference;

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

    private static final Logger logger = LoggerBuilder.getLogger(ProcessHandler.class);

    private static final String KEY_ERROR_THROWABLE = "_ERROR_THROWABLE";
    private static final String KEY_BEE_CHILD_ID = "_BEE_CHILD_ID";
    private static final String KEY_ERROR_POINT = "_ERROR_POINT";

    @Override
    public Span before(String className, String methodName, Object[] allArguments, Object[] extVal) {

        if (!ProcessConfig.init().isEnable()) {
            return null;
        }

        //Span的构造
        Span span = SpanManager.createEntrySpan(SpanType.PROCESS);
        //方法开始前执行逻辑
        logBeginTrace(className, methodName, span, logger);
        span.addTag("method", methodName).addTag("clazz", className);
        return span;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal) {

        Span span = SpanManager.getExitSpan();
        if (span == null) {
            return result;
        }

        Throwable childThrowable = (Throwable) span.getTags().get(KEY_ERROR_THROWABLE);
        String childId = (String) span.getTags().get(KEY_BEE_CHILD_ID);
        String childErrorPoint = (String) span.getTags().get(KEY_ERROR_POINT);
        String errorPoint = className + "." + methodName;
        span.removeTag(KEY_ERROR_THROWABLE);
        span.removeTag(KEY_BEE_CHILD_ID);
        span.removeTag(KEY_ERROR_POINT);

        if (!ProcessConfig.init().isEnable()) {
            return null;
        }

        //计算执行时间
        calculateSpend(span);
        //方法执行后记录数据
        logEndTrace(className, methodName, span, logger);
        //耗时阈值检测
        if (span.getSpend() > ProcessConfig.init().getSpend() && CollectRatio.yes()) {
            //发送日志传输
            TransmitterFactory.offerQueue(span);
            //采集参数
            collectParams(allArguments, span.getId());
        }
        //异常处理
        handleError(span.getId(), errorPoint, t, childId, childErrorPoint, childThrowable);
        return result;
    }

    /**
     * 异常处理
     *
     * @param id
     * @param errorPoint
     * @param t
     * @param childId
     * @param childErrorPoint
     * @param childThrowable
     */
    private void handleError(String id, String errorPoint, Throwable t, String childId, String childErrorPoint, Throwable childThrowable) {
        if (t == null && childThrowable == null) {
            return;
        }
        Span currentSpan = SpanManager.getCurrentSpan();
        if (currentSpan == null) {
            if (t == null) {
                sendError(childId, childErrorPoint, childThrowable);
            } else if (childThrowable == null) {
                sendError(id, errorPoint, t);
            } else {
                if (t == childThrowable || t.getCause() == childThrowable) {
                    sendError(id, errorPoint, t);
                } else {
                    sendError(id, errorPoint, t);
                    sendError(childId, childErrorPoint, childThrowable);
                }
            }
        } else {
            if (childThrowable == null) {
                currentSpan.addTag(KEY_ERROR_THROWABLE, t);
                currentSpan.addTag(KEY_BEE_CHILD_ID, id);
                currentSpan.addTag(KEY_ERROR_POINT, errorPoint);
            } else if (t == null) {
                sendError(childId, childErrorPoint, childThrowable);
            } else {
                currentSpan.addTag(KEY_ERROR_THROWABLE, t);
                currentSpan.addTag(KEY_BEE_CHILD_ID, id);
                if (t != childThrowable && t.getCause() != childThrowable) {
                    sendError(childId, childErrorPoint, childThrowable);
                }
            }
        }
    }

    /**
     * Error数据发送
     */
    private void sendError(String id, String errorPoint, Throwable t) {

        if (ProcessConfig.init().checkErrorPoint(errorPoint)) {
            Span err = new Span(SpanType.ERROR);
            //err.fillEnvInfo();
            err.setId(id);
            err.setGid(SnakeTraceContext.getGId());
            err.addTag("desc", formatThrowable(t));
            TransmitterFactory.offerQueue(err);
        }
    }

    private String formatThrowable(Throwable t) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        t.printStackTrace(new java.io.PrintWriter(buf, true));
        String expMessage = buf.toString();
        SnakeUtils.close(buf);
        return expMessage;
    }


    /**
     * 收集参数
     *
     * @param allArguments
     * @param spanId
     */
    private void collectParams(Object[] allArguments, String spanId) {
        if (ProcessConfig.init().isEnableParam() && allArguments != null && allArguments.length > 0) {

            Span span = new Span(SpanType.PARAM);
            span.setId(spanId);
            span.setTime(null);
            span.setPort(null);
            span.setCluster(null);
            span.setIp(null);
            span.setServer(null);

            Object[] params = new Object[allArguments.length];
            for (int i = 0; i < allArguments.length; i++) {
                if (allArguments[i] != null && ProcessConfig.init().isExcludeParamType(allArguments[i].getClass())) {
                    params[i] = "--";
                } else {
                    params[i] = new SoftReference<>(allArguments[i]);
                }
            }
            span.addTag("param", params);
            TransmitterFactory.offerQueue(span);
        }
    }
}
