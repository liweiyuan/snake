package com.learn.snake.servlet.plugin.common;

import com.learn.snake.util.LoggerBuilder;
import org.slf4j.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

/**
 * @Author :lwy
 * @Date : 2018/10/31 17:05
 * @Description : 返回的response压缩
 */
public class SnakeHttpResponseWrapper extends HttpServletResponseWrapper {

    private static final Logger logger = LoggerBuilder.getLogger(SnakeHttpResponseWrapper.class);

    private ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    private HttpServletResponse response;

    private PrintWriter writer;

    private boolean isOutputStream = false;

    private boolean isWrite = false;

    /**
     * Constructs a response adaptor wrapping the given response.
     *
     * @param response
     * @throws IllegalArgumentException if the response is null
     */
    public SnakeHttpResponseWrapper(HttpServletResponse response) {
        super(response);
        this.response = response;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        isOutputStream=true;
        return new InnerServletOutputStream(bytes);
    }

    /**
     * 重写父类的 getWriter() 方法，将响应数据缓存在 PrintWriter 中
     */
    @Override
    public PrintWriter getWriter() {
        try {
            isWrite=true;
            writer = new PrintWriter(new OutputStreamWriter(bytes, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("encoding failed", e);
            isWrite=false;
        }
        return writer;
    }


    public void out() {
        if (isOutputStream) {
            isWrite = false; //OutputStream和PrintWriter是互斥的，只要一个就可以
            try {
                OutputStream os = response.getOutputStream();
                os.write(getBytes());
                os.flush();
                os.close();
            } catch (Exception e) {
                logger.error("io exception",e);
            }
        }
        if (isWrite) {
            try {
                PrintWriter writer = response.getWriter();
                System.out.println(new String(getBytes()));
                writer.append(new String(getBytes()));
                writer.flush();
                writer.close();
            } catch (Exception e) {
                logger.error("io exception",e);
            }
        }
    }

    public byte[] getBytes() {
        if (null != writer) {
            writer.close();
            return bytes.toByteArray();
        }

        if (null != bytes) {
            try {
                bytes.flush();
            } catch (IOException e) {
                logger.error("io exception",e);
            }
        }
        return bytes.toByteArray();
    }


    /**
     * 自定义输出
     */
    private class InnerServletOutputStream extends ServletOutputStream {

        private ByteArrayOutputStream ostream ;


        public InnerServletOutputStream(ByteArrayOutputStream ostream) {
            this.ostream = ostream;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }

        @Override
        public void write(int b) throws IOException {
            ostream.write(b);  //将数据写入到ByteArrayOutputStream
        }
    }
}

/**
 *
 * https://blog.csdn.net/qq_33206732/article/details/78623042
 */
