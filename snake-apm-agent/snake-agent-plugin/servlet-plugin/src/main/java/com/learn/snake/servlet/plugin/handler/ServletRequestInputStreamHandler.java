package com.learn.snake.servlet.plugin.handler;

import com.learn.snake.common.SnakeTraceContext;
import com.learn.snake.model.Span;
import com.learn.snake.plugin.handler.AbstractHandler;
import com.learn.snake.servlet.plugin.ServletConfig;
import com.learn.snake.servlet.plugin.common.RequestBodyHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.*;
import java.util.Objects;

/**
 * @Author :lwy
 * @Date : 2018/12/7 14:59
 * @Description :
 */
public class ServletRequestInputStreamHandler extends AbstractHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServletRequestInputStreamHandler.class);

    @Override
    public Span before(String className, String methodName, Object[] allArguments, Object[] extVal) {
        return null;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal) {

        if (!ServletConfig.init().isEnableReqBody()) {
            RequestBodyHolder.remove();
            return result;
        }
        InputStream in = (InputStream) result;
        if (in == null) {
            return null;
        }
        try {
            String gid = SnakeTraceContext.getGId();
            if (!gid.equals(RequestBodyHolder.getGid())) {
                logger.debug("read the inputStream");
                RequestBodyHolder.remove();
                StringBuilder sb = new StringBuilder();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                String body = sb.toString();
                RequestBodyHolder.setRequestBody(body);

            }
            logger.debug("snakeRequestBody=" + RequestBodyHolder.getRequestBody());
            byte[] bytes = Objects.requireNonNull(RequestBodyHolder.getRequestBody()).getBytes();
            final ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
            result = new ServletInputStream() {

                @Override
                public int read() throws IOException {
                    return byteIn.read();
                }

                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }

                @Override
                public int read(byte[] b, int off, int len) throws IOException {
                    return byteIn.read(b, off, len);
                }
            };
        } catch (IOException e) {
            logger.error("ioException", e);
            return null;
        }
        return result;
    }
}
