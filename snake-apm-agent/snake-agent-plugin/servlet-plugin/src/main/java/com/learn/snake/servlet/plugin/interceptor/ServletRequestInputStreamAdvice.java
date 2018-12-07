package com.learn.snake.servlet.plugin.interceptor;

import com.learn.snake.plugin.handler.HandlerLoader;
import com.learn.snake.plugin.handler.IHandler;
import com.learn.snake.util.LoggerBuilder;
import net.bytebuddy.asm.Advice;
import org.slf4j.Logger;

import javax.servlet.ServletInputStream;

/**
 * @Author :lwy
 * @Date : 2018/12/7 10:42
 * @Description :
 */
public class ServletRequestInputStreamAdvice {

    /*@Advice.OnMethodEnter
    public static void  enter(@Advice.Local("handler")IHandler handler,
                              @Advice.Origin(value = "#t") String className,
                              @Advice.Origin(value = "#m") String methodName
                              ){
    }*/


    @Advice.OnMethodExit
    public static void exit(@Advice.Return(readOnly = false) ServletInputStream inputStream) {

        Logger logger = LoggerBuilder.getLogger("ServletRequestInputStreamAdvice");
        try {
            IHandler handler = HandlerLoader.loadHandler("com.learn.snake.servlet.plugin.handler.ServletRequestInputStreamHandler");
            inputStream = (ServletInputStream) handler.after(null, null, null, inputStream, null, null);
        } catch (Exception e) {
            logger.error("execute request body failed.", e);
        }
    }
}
